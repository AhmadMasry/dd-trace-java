package datadog.trace.core.monitor

import com.timgroup.statsd.StatsDClient
import datadog.trace.util.test.DDSpecification
import org.junit.Assert

import static java.util.concurrent.TimeUnit.MILLISECONDS

class TimingTest extends DDSpecification {

  def "timer times stuff" () {
    setup:
    System.out.println(System.getProperty("java.class.path"))
    StatsDClient statsd = Mock(StatsDClient)
    Monitoring monitoring = new Monitoring(statsd, 100, MILLISECONDS)
    def timer = monitoring.newTimer("my_timer")
    when:
    Recording recording = timer.start()
    Thread.sleep(200)
    recording.close()
    then:
    1 * statsd.gauge("my_timer", { it > MILLISECONDS.toNanos(200) }, "stat:avg")
    1 * statsd.gauge("my_timer", { it > MILLISECONDS.toNanos(200) }, "stat:p50")
    1 * statsd.gauge("my_timer", { it > MILLISECONDS.toNanos(200) }, "stat:p99")
    1 * statsd.gauge("my_timer", { it > MILLISECONDS.toNanos(200) }, "stat:max")
    0 * _
  }

  def "threadlocal timer times stuff" () {
    setup:
    StatsDClient statsd = Mock(StatsDClient)
    Monitoring monitoring = new Monitoring(statsd, 100, MILLISECONDS)
    def timer = monitoring.newThreadLocalTimer("my_timer")
    when:
    Recording recording = timer.start()
    Thread.sleep(200)
    recording.close()
    then:
    1 * statsd.gauge("my_timer", { it > MILLISECONDS.toNanos(200) }, {it[0] == "stat:avg" && it[1].startsWith("thread:")})
    1 * statsd.gauge("my_timer", { it > MILLISECONDS.toNanos(200) }, {it[0] == "stat:p50" && it[1].startsWith("thread:")})
    1 * statsd.gauge("my_timer", { it > MILLISECONDS.toNanos(200) }, {it[0] == "stat:p99" && it[1].startsWith("thread:")})
    1 * statsd.gauge("my_timer", { it > MILLISECONDS.toNanos(200) }, {it[0] == "stat:max" && it[1].startsWith("thread:")})
    0 * _
  }

  def "reset timer" () {
    setup:
    StatsDClient statsd = Mock(StatsDClient)
    Monitoring monitoring = new Monitoring(statsd, 100, MILLISECONDS)
    def timer = timerCreator(monitoring)
    when:
    timer.start()
    long min = System.nanoTime()
    timer.reset()
    then:
    if (timer instanceof ThreadLocalRecording) {
      timer.tls.get().start >= min
    } else {
      timer.start >= min
    }

    where:
    timerCreator << [{it.newTimer("my_timer")}, { it.newThreadLocalTimer("my_timer") }]
  }

  def "disabled monitoring produces no ops" () {
    expect:
    Monitoring.DISABLED.newTimer("foo") instanceof NoOpRecording
    Monitoring.DISABLED.newTimer("foo", "tag") instanceof NoOpRecording
    Monitoring.DISABLED.newThreadLocalTimer("foo") instanceof NoOpRecording
  }

  def "no ops are safe to use" () {
    expect:
    try {
      recording.start().stop()
      recording.reset()
    } catch (Throwable t) {
      Assert.fail(t.getMessage())
    }

    where:
    recording << [Monitoring.DISABLED.newTimer("foo"),
                  Monitoring.DISABLED.newTimer("foo", "tag"),
                  Monitoring.DISABLED.newThreadLocalTimer("foo")]
  }
}
