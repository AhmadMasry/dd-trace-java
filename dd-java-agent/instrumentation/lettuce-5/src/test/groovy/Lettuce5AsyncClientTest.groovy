import static datadog.trace.instrumentation.lettuce5.LettuceInstrumentationUtil.AGENT_CRASHING_COMMAND_PREFIX

import com.redis.testcontainers.RedisContainer
import datadog.trace.agent.test.naming.VersionedNamingTestBase
import datadog.trace.agent.test.utils.PortUtils
import datadog.trace.api.Config
import datadog.trace.api.DDSpanTypes
import datadog.trace.bootstrap.instrumentation.api.Tags
import io.lettuce.core.ClientOptions
import io.lettuce.core.ConnectionFuture
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisFuture
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulConnection
import io.lettuce.core.api.async.RedisAsyncCommands
import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.protocol.AsyncCommand
import org.testcontainers.containers.wait.strategy.Wait
import spock.lang.Shared
import spock.util.concurrent.AsyncConditions

import java.util.concurrent.CancellationException
import java.util.concurrent.CompletionException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Function

abstract class Lettuce5AsyncClientTest extends VersionedNamingTestBase {
  public static final int DB_INDEX = 0
  // Disable autoreconnect so we do not get stray traces popping up on server shutdown
  public static final ClientOptions CLIENT_OPTIONS = ClientOptions.builder().autoReconnect(false).build()

  @Shared
  int port
  @Shared
  int incorrectPort
  @Shared
  String dbAddr
  @Shared
  String dbAddrNonExistent
  @Shared
  String dbUriNonExistent
  @Shared
  String embeddedDbUri

  @Shared
  RedisContainer redisServer = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME)
  .waitingFor(Wait.forListeningPort())

  @Shared
  Map<String, String> testHashMap = [
    firstname: "John",
    lastname : "Doe",
    age      : "53"
  ]

  RedisClient redisClient
  StatefulConnection connection
  RedisAsyncCommands<String, ?> asyncCommands
  RedisCommands<String, ?> syncCommands

  def setupSpec() {
    redisServer.start()
    println "Using redis: $redisServer.redisURI"
    port = redisServer.firstMappedPort
    incorrectPort = PortUtils.randomOpenPort()
    dbAddr = redisServer.getHost() + ":" + port + "/" + DB_INDEX
    dbAddrNonExistent = redisServer.getHost() + ":" + incorrectPort + "/" + DB_INDEX
    dbUriNonExistent = "redis://" + dbAddrNonExistent
    embeddedDbUri = "redis://" + dbAddr
  }

  def setup() {
    redisClient = RedisClient.create(embeddedDbUri)
    redisClient.setOptions(CLIENT_OPTIONS)

    connection = redisClient.connect()
    asyncCommands = connection.async()
    syncCommands = connection.sync()

    syncCommands.set("TESTKEY", "TESTVAL")

    // 1 set + 1 connect trace
    TEST_WRITER.waitForTraces(2)
    TEST_WRITER.clear()
  }

  def cleanup() {
    connection.close()
  }

  def cleanupSpec() {
    redisServer.stop()
  }

  def "connect using get on ConnectionFuture"() {
    setup:
    RedisClient testConnectionClient = RedisClient.create(embeddedDbUri)
    testConnectionClient.setOptions(CLIENT_OPTIONS)

    when:
    ConnectionFuture connectionFuture = testConnectionClient.connectAsync(StringCodec.UTF8,
      new RedisURI(redisServer.getHost(), port, 3, TimeUnit.SECONDS))
    StatefulConnection connection = connectionFuture.get()
    TEST_WRITER.waitForTraces(1)

    then:
    connection != null
    assertTraces(1) {
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName "CONNECT:" + dbAddr
          errored false

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" port
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
    }

    cleanup:
    connection.close()
  }

  def "connect exception inside the connection future"() {
    setup:
    RedisClient testConnectionClient = RedisClient.create(dbUriNonExistent)
    testConnectionClient.setOptions(CLIENT_OPTIONS)

    when:
    ConnectionFuture connectionFuture = testConnectionClient.connectAsync(StringCodec.UTF8,
      new RedisURI(redisServer.getHost(), incorrectPort, 3, TimeUnit.SECONDS))
    StatefulConnection connection = connectionFuture.get()

    then:
    connection == null
    thrown ExecutionException
    assertTraces(1) {
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName "CONNECT:" + dbAddrNonExistent
          errored true

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" incorrectPort
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            errorTags CompletionException, String
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
    }
  }

  def "set command using Future get with timeout"() {
    setup:
    RedisFuture<String> redisFuture = asyncCommands.set("TESTSETKEY", "TESTSETVAL")
    String res = redisFuture.get(3, TimeUnit.SECONDS)

    expect:
    res == "OK"
    assertTraces(1) {
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName "SET"
          errored false

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" port
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
    }
  }

  def "get command chained with thenAccept"() {
    setup:
    def conds = new AsyncConditions()
    Consumer<String> consumer = new Consumer<String>() {
        @Override
        void accept(String res) {
          conds.evaluate {
            assert res == "TESTVAL"
          }
        }
      }

    when:
    RedisFuture<String> redisFuture = asyncCommands.get("TESTKEY")
    redisFuture.thenAccept(consumer)

    then:
    conds.await()
    assertTraces(1) {
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName "GET"
          errored false

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" port
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
    }
  }

  // to make sure instrumentation's chained completion stages won't interfere with user's, while still
  // recording metrics
  def "get non existent key command with handleAsync and chained with thenApply"() {
    setup:
    def conds = new AsyncConditions()
    final String successStr = "KEY MISSING"
    BiFunction<String, Throwable, String> firstStage = new BiFunction<String, Throwable, String>() {
        @Override
        String apply(String res, Throwable throwable) {
          conds.evaluate {
            assert res == null
            assert throwable == null
          }
          return (res == null ? successStr : res)
        }
      }
    Function<String, Object> secondStage = new Function<String, Object>() {
        @Override
        Object apply(String input) {
          conds.evaluate {
            assert input == successStr
          }
          return null
        }
      }

    when:
    RedisFuture<String> redisFuture = asyncCommands.get("NON_EXISTENT_KEY")
    redisFuture.handleAsync(firstStage).thenApply(secondStage)

    then:
    conds.await()
    assertTraces(1) {
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName "GET"
          errored false

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" port
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
    }
  }

  def "command with no arguments using a biconsumer"() {
    setup:
    def conds = new AsyncConditions()
    BiConsumer<String, Throwable> biConsumer = new BiConsumer<String, Throwable>() {
        @Override
        void accept(String keyRetrieved, Throwable throwable) {
          conds.evaluate {
            assert keyRetrieved != null
          }
        }
      }

    when:
    RedisFuture<String> redisFuture = asyncCommands.randomkey()
    redisFuture.whenCompleteAsync(biConsumer)

    then:
    conds.await()
    assertTraces(1) {
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName "RANDOMKEY"
          errored false

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" port
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
    }
  }

  def "hash set and then nest apply to hash getall"() {
    setup:
    def conds = new AsyncConditions()

    when:
    RedisFuture<String> hmsetFuture = asyncCommands.hmset("TESTHM", testHashMap)
    hmsetFuture.thenApplyAsync(new Function<String, Object>() {
        @Override
        Object apply(String setResult) {
          TEST_WRITER.waitForTraces(1) // Wait for 'hmset' trace to get written
          conds.evaluate {
            assert setResult == "OK"
          }
          RedisFuture<Map<String, String>> hmGetAllFuture = asyncCommands.hgetall("TESTHM")
          hmGetAllFuture.exceptionally(new Function<Throwable, Map<String, String>>() {
              @Override
              Map<String, String> apply(Throwable throwable) {
                println("unexpected:" + throwable.toString())
                throwable.printStackTrace()
                assert false
                return null
              }
            })
          hmGetAllFuture.thenAccept(new Consumer<Map<String, String>>() {
              @Override
              void accept(Map<String, String> hmGetAllResult) {
                conds.evaluate {
                  assert testHashMap == hmGetAllResult
                }
              }
            })
          return null
        }
      })

    then:
    conds.await()
    assertTraces(2) {
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName "HMSET"
          errored false

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" port
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName "HGETALL"
          errored false

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" port
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
    }
  }

  def "command completes exceptionally"() {
    setup:
    // turn off auto flush to complete the command exceptionally manually
    asyncCommands.setAutoFlushCommands(false)
    def conds = new AsyncConditions()
    RedisFuture redisFuture = asyncCommands.del("key1", "key2")
    boolean completedExceptionally = ((AsyncCommand) redisFuture).completeExceptionally(new IllegalStateException("TestException"))
    redisFuture.exceptionally({ throwable ->
      conds.evaluate {
        assert throwable != null
        assert throwable instanceof IllegalStateException
        assert throwable.getMessage() == "TestException"
      }
      throw throwable
    })

    when:
    // now flush and execute the command
    asyncCommands.flushCommands()
    redisFuture.get()

    then:
    conds.await()
    completedExceptionally == true
    thrown Exception
    assertTraces(1) {
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName "DEL"
          errored true

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" port
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            errorTags(IllegalStateException, "TestException")
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
    }
  }

  def "cancel command before it finishes"() {
    setup:
    asyncCommands.setAutoFlushCommands(false)
    def conds = new AsyncConditions()
    RedisFuture redisFuture = asyncCommands.sadd("SKEY", "1", "2")
    redisFuture.whenCompleteAsync({ res, throwable ->
      conds.evaluate {
        assert throwable != null
        assert throwable instanceof CancellationException
      }
    })

    when:
    boolean cancelSuccess = redisFuture.cancel(true)
    asyncCommands.flushCommands()

    then:
    conds.await()
    cancelSuccess == true
    assertTraces(1) {
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName "SADD"
          errored false

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" port
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            "db.command.cancelled" true
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
    }
  }

  def "debug segfault command (returns void) with no argument should produce span"() {
    setup:
    asyncCommands.debugSegfault()

    expect:
    assertTraces(1) {
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName AGENT_CRASHING_COMMAND_PREFIX + "DEBUG"
          errored false

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" port
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
    }
  }


  def "shutdown command (returns void) should produce a span"() {
    setup:
    asyncCommands.shutdown(false)

    expect:
    assertTraces(1) {
      trace(1) {
        span {
          serviceName service()
          operationName operation()
          spanType DDSpanTypes.REDIS
          resourceName "SHUTDOWN"
          errored false

          tags {
            "$Tags.COMPONENT" "redis-client"
            "$Tags.SPAN_KIND" Tags.SPAN_KIND_CLIENT
            "$Tags.PEER_HOSTNAME" redisServer.getHost()
            "$Tags.PEER_PORT" port
            "$Tags.DB_TYPE" "redis"
            "db.redis.dbIndex" 0
            peerServiceFrom(Tags.PEER_HOSTNAME)
            defaultTags()
          }
        }
      }
    }
  }
}

class Lettuce5SyncClientV0Test extends Lettuce5AsyncClientTest {

  @Override
  int version() {
    return 0
  }

  @Override
  String service() {
    return "redis"
  }

  @Override
  String operation() {
    return "redis.query"
  }
}

class Lettuce5SyncClientV1ForkedTest extends Lettuce5AsyncClientTest {

  @Override
  int version() {
    return 1
  }

  @Override
  String service() {
    return Config.get().getServiceName()
  }

  @Override
  String operation() {
    return "redis.command"
  }
}


class Lettuce5AsyncProfilingForkedTest extends Lettuce5AsyncClientTest {

  @Override
  protected void configurePreAgent() {

    super.configurePreAgent()
    injectSysConfig('dd.profiling.enabled', 'true')
  }

  @Override
  int version() {
    return 0
  }

  @Override
  String service() {
    return "redis"
  }

  @Override
  String operation() {
    return "redis.query"
  }
}
