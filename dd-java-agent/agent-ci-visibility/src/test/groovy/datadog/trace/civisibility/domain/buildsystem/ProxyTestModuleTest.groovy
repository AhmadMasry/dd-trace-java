package datadog.trace.civisibility.domain.buildsystem

import datadog.trace.api.Config
import datadog.trace.api.DDTraceId
import datadog.trace.bootstrap.instrumentation.api.AgentSpan
import datadog.trace.civisibility.config.EarlyFlakeDetectionSettings
import datadog.trace.api.civisibility.config.TestIdentifier
import datadog.trace.api.civisibility.coverage.CoverageStore
import datadog.trace.api.civisibility.telemetry.CiVisibilityMetricCollector
import datadog.trace.civisibility.codeowners.Codeowners
import datadog.trace.civisibility.config.ExecutionSettings
import datadog.trace.civisibility.coverage.percentage.child.ChildProcessCoverageReporter
import datadog.trace.civisibility.decorator.TestDecorator
import datadog.trace.civisibility.ipc.SignalClient
import datadog.trace.civisibility.source.MethodLinesResolver
import datadog.trace.civisibility.source.SourcePathResolver
import datadog.trace.civisibility.test.ExecutionStrategy
import datadog.trace.test.util.DDSpecification

class ProxyTestModuleTest extends DDSpecification {

  def "test total retries limit is applied across test cases"() {
    def executionSettings = Stub(ExecutionSettings)
    executionSettings.getEarlyFlakeDetectionSettings() >> EarlyFlakeDetectionSettings.DEFAULT
    executionSettings.isFlakyTestRetriesEnabled() >> true
    executionSettings.getFlakyTests() >> null

    def config = Stub(Config)
    config.getCiVisibilityFlakyRetryCount() >> 2 // this counts all executions of a test case (first attempt is counted too)
    config.getCiVisibilityTotalFlakyRetryCount() >> 2 // this counts retries across all tests (first attempt is not a retry, so it is not counted)

    def executionStrategy = new ExecutionStrategy(config, executionSettings)

    def traceId = Stub(DDTraceId)
    traceId.toLong() >> 123

    def moduleSpanContext = Stub(AgentSpan.Context)
    moduleSpanContext.getSpanId() >> 456
    moduleSpanContext.getTraceId() >> traceId

    given:
    def proxyTestModule = new ProxyTestModule(
      moduleSpanContext,
      "test-module",
      executionStrategy,
      config,
      Stub(CiVisibilityMetricCollector),
      Stub(TestDecorator),
      Stub(SourcePathResolver),
      Stub(Codeowners),
      Stub(MethodLinesResolver),
      Stub(CoverageStore.Factory),
      Stub(ChildProcessCoverageReporter),
      GroovyMock(SignalClient.Factory)
      )

    when:
    def retryPolicy1 = proxyTestModule.retryPolicy(new TestIdentifier("suite", "test-1", null))

    then:
    retryPolicy1.retry(false, 1L) // 2nd test execution, 1st retry globally
    !retryPolicy1.retry(false, 1L) // asking for 3rd test execution - local limit reached

    when:
    def retryPolicy2 = proxyTestModule.retryPolicy(new TestIdentifier("suite", "test-2", null))

    then:
    retryPolicy2.retry(false, 1L) // 2nd test execution, 2nd retry globally (since previous test was retried too)
    !retryPolicy2.retry(false, 1L) // asking for 3rd test execution - local limit reached

    when:
    def retryPolicy3 = proxyTestModule.retryPolicy(new TestIdentifier("suite", "test-3", null))

    then:
    !retryPolicy3.retry(false, 1L) // asking for 3rd retry globally - global limit reached
  }
}
