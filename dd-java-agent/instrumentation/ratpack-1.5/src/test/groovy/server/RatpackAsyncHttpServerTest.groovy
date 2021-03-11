package server

import datadog.trace.agent.test.base.HttpServerTest
import ratpack.exec.Promise
import ratpack.groovy.test.embed.GroovyEmbeddedApp
import ratpack.test.embed.EmbeddedApp

import static datadog.trace.agent.test.base.HttpServerTest.ServerEndpoint.ERROR
import static datadog.trace.agent.test.base.HttpServerTest.ServerEndpoint.EXCEPTION
import static datadog.trace.agent.test.base.HttpServerTest.ServerEndpoint.FORWARDED
import static datadog.trace.agent.test.base.HttpServerTest.ServerEndpoint.QUERY_PARAM
import static datadog.trace.agent.test.base.HttpServerTest.ServerEndpoint.REDIRECT
import static datadog.trace.agent.test.base.HttpServerTest.ServerEndpoint.SUCCESS

class RatpackAsyncHttpServerTest extends RatpackHttpServerTest {

  @Override
  EmbeddedApp startServer(int bindPort) {
    def ratpack = GroovyEmbeddedApp.ratpack {
      serverConfig {
        port bindPort
        address InetAddress.getByName('localhost')
      }
      bindings {
        bind TestErrorHandler
      }
      handlers {
        prefix(SUCCESS.rawPath()) {
          all {
            Promise.sync {
              SUCCESS
            } then { HttpServerTest.ServerEndpoint endpoint ->
              controller(endpoint) {
                context.response.status(endpoint.status).send(endpoint.body)
              }
            }
          }
        }
        prefix(FORWARDED.rawPath()) {
          all {
            Promise.sync {
              FORWARDED
            } then { HttpServerTest.ServerEndpoint endpoint ->
              controller(endpoint) {
                context.response.status(endpoint.status).send(request.headers.get("x-forwarded-for"))
              }
            }
          }
        }
        prefix(QUERY_PARAM.rawPath()) {
          all {
            Promise.sync {
              QUERY_PARAM
            } then { HttpServerTest.ServerEndpoint endpoint ->
              controller(endpoint) {
                context.response.status(endpoint.status).send(request.query)
              }
            }
          }
        }
        prefix(REDIRECT.rawPath()) {
          all {
            Promise.sync {
              REDIRECT
            } then { HttpServerTest.ServerEndpoint endpoint ->
              controller(endpoint) {
                context.redirect(endpoint.body)
              }
            }
          }
        }
        prefix(ERROR.rawPath()) {
          all {
            Promise.sync {
              ERROR
            } then { HttpServerTest.ServerEndpoint endpoint ->
              controller(endpoint) {
                context.response.status(endpoint.status).send(endpoint.body)
              }
            }
          }
        }
        prefix(EXCEPTION.rawPath()) {
          all {
            Promise.sync {
              EXCEPTION
            } then { HttpServerTest.ServerEndpoint endpoint ->
              controller(endpoint) {
                throw new Exception(endpoint.body)
              }
            }
          }
        }
      }
    }
    ratpack.server.start()

    assert ratpack.address.port == bindPort
    assert ratpack.server.bindHost == 'localhost'
    return ratpack
  }
}
