[ {
  "type" : "test_session_end",
  "version" : 1,
  "content" : {
    "test_session_id" : ${content_test_session_id},
    "service" : "worker.org.gradle.process.internal.worker.gradleworkermain",
    "name" : "junit.test_session",
    "resource" : "junit-4.13",
    "start" : ${content_start},
    "duration" : ${content_duration},
    "error" : 0,
    "metrics" : {
      "_dd.host.vcpu_count" : ${content_metrics__dd_host_vcpu_count},
      "process_id" : ${content_metrics_process_id},
      "_dd.profiling.enabled" : 0,
      "_dd.trace_span_attribute_schema" : 0
    },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid},
      "test.type" : "test",
      "_dd.tracer_host" : ${content_meta__dd_tracer_host},
      "test.status" : "pass",
      "test_session.name" : "session-name",
      "language" : "jvm",
      "env" : "none",
      "dummy_ci_tag" : "dummy_ci_tag_value",
      "library_version" : ${content_meta_library_version},
      "component" : "junit",
      "_dd.profiling.ctx" : "test",
      "span.kind" : "test_session_end",
      "runtime-id" : ${content_meta_runtime_id},
      "test.command" : "junit-4.13",
      "test.framework_version" : ${content_meta_test_framework_version},
      "test.framework" : "junit4"
    }
  }
}, {
  "type" : "test_module_end",
  "version" : 1,
  "content" : {
    "test_session_id" : ${content_test_session_id},
    "test_module_id" : ${content_test_module_id},
    "service" : "worker.org.gradle.process.internal.worker.gradleworkermain",
    "name" : "junit.test_module",
    "resource" : "junit-4.13",
    "start" : ${content_start_2},
    "duration" : ${content_duration_2},
    "error" : 0,
    "metrics" : {
      "_dd.host.vcpu_count" : ${content_metrics__dd_host_vcpu_count_2}
    },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_2},
      "test.type" : "test",
      "test.module" : "junit-4.13",
      "test.status" : "pass",
      "test_session.name" : "session-name",
      "env" : "none",
      "dummy_ci_tag" : "dummy_ci_tag_value",
      "library_version" : ${content_meta_library_version},
      "component" : "junit",
      "span.kind" : "test_module_end",
      "test.framework_version" : ${content_meta_test_framework_version},
      "test.framework" : "junit4"
    }
  }
}, {
  "type" : "test_suite_end",
  "version" : 1,
  "content" : {
    "test_session_id" : ${content_test_session_id},
    "test_module_id" : ${content_test_module_id},
    "test_suite_id" : ${content_test_suite_id},
    "service" : "worker.org.gradle.process.internal.worker.gradleworkermain",
    "name" : "junit.test_suite",
    "resource" : "org.example.TestSucceedBeforeParamAfterParam",
    "start" : ${content_start_3},
    "duration" : ${content_duration_3},
    "error" : 0,
    "metrics" : {
      "_dd.host.vcpu_count" : ${content_metrics__dd_host_vcpu_count_3},
      "test.source.end" : 19,
      "test.source.start" : 11
    },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_3},
      "test.type" : "test",
      "test.source.file" : "dummy_source_path",
      "test.module" : "junit-4.13",
      "test.status" : "pass",
      "test_session.name" : "session-name",
      "env" : "none",
      "dummy_ci_tag" : "dummy_ci_tag_value",
      "test.codeowners" : "[\"owner1\",\"owner2\"]",
      "library_version" : ${content_meta_library_version},
      "component" : "junit",
      "span.kind" : "test_suite_end",
      "test.suite" : "org.example.TestSucceedBeforeParamAfterParam",
      "test.framework_version" : ${content_meta_test_framework_version},
      "test.framework" : "junit4"
    }
  }
}, {
  "type" : "test",
  "version" : 2,
  "content" : {
    "trace_id" : ${content_trace_id},
    "span_id" : ${content_span_id},
    "parent_id" : ${content_parent_id},
    "test_session_id" : ${content_test_session_id},
    "test_module_id" : ${content_test_module_id},
    "test_suite_id" : ${content_test_suite_id},
    "service" : "worker.org.gradle.process.internal.worker.gradleworkermain",
    "name" : "junit.test",
    "resource" : "org.example.TestSucceedBeforeParamAfterParam.parameterized_test_succeed",
    "start" : ${content_start_4},
    "duration" : ${content_duration_4},
    "error" : 0,
    "metrics" : {
      "process_id" : ${content_metrics_process_id},
      "_dd.profiling.enabled" : 0,
      "_dd.trace_span_attribute_schema" : 0,
      "_dd.host.vcpu_count" : ${content_metrics__dd_host_vcpu_count_4},
      "test.source.end" : 18,
      "test.source.start" : 12
    },
    "meta" : {
      "_dd.tracer_host" : ${content_meta__dd_tracer_host},
      "test.source.file" : "dummy_source_path",
      "test.source.method" : "parameterized_test_succeed()V",
      "test.module" : "junit-4.13",
      "test.status" : "pass",
      "language" : "jvm",
      "test.codeowners" : "[\"owner1\",\"owner2\"]",
      "library_version" : ${content_meta_library_version},
      "test.name" : "parameterized_test_succeed",
      "span.kind" : "test",
      "test.suite" : "org.example.TestSucceedBeforeParamAfterParam",
      "runtime-id" : ${content_meta_runtime_id},
      "test.type" : "test",
      "test_session.name" : "session-name",
      "env" : "none",
      "dummy_ci_tag" : "dummy_ci_tag_value",
      "test.parameters" : "{\"metadata\":{\"test_name\":\"parameterized_test_succeed[0]\"}}",
      "component" : "junit",
      "_dd.profiling.ctx" : "test",
      "test.framework_version" : ${content_meta_test_framework_version},
      "test.framework" : "junit4"
    }
  }
}, {
  "type" : "test",
  "version" : 2,
  "content" : {
    "trace_id" : ${content_trace_id_2},
    "span_id" : ${content_span_id_2},
    "parent_id" : ${content_parent_id},
    "test_session_id" : ${content_test_session_id},
    "test_module_id" : ${content_test_module_id},
    "test_suite_id" : ${content_test_suite_id},
    "service" : "worker.org.gradle.process.internal.worker.gradleworkermain",
    "name" : "junit.test",
    "resource" : "org.example.TestSucceedBeforeParamAfterParam.parameterized_test_succeed",
    "start" : ${content_start_5},
    "duration" : ${content_duration_5},
    "error" : 0,
    "metrics" : {
      "process_id" : ${content_metrics_process_id},
      "_dd.profiling.enabled" : 0,
      "_dd.trace_span_attribute_schema" : 0,
      "_dd.host.vcpu_count" : ${content_metrics__dd_host_vcpu_count_5},
      "test.source.end" : 18,
      "test.source.start" : 12
    },
    "meta" : {
      "_dd.tracer_host" : ${content_meta__dd_tracer_host},
      "test.source.file" : "dummy_source_path",
      "test.source.method" : "parameterized_test_succeed()V",
      "test.module" : "junit-4.13",
      "test.status" : "pass",
      "language" : "jvm",
      "test.codeowners" : "[\"owner1\",\"owner2\"]",
      "library_version" : ${content_meta_library_version},
      "test.name" : "parameterized_test_succeed",
      "span.kind" : "test",
      "test.suite" : "org.example.TestSucceedBeforeParamAfterParam",
      "runtime-id" : ${content_meta_runtime_id},
      "test.type" : "test",
      "test_session.name" : "session-name",
      "env" : "none",
      "dummy_ci_tag" : "dummy_ci_tag_value",
      "test.parameters" : "{\"metadata\":{\"test_name\":\"parameterized_test_succeed[1]\"}}",
      "component" : "junit",
      "_dd.profiling.ctx" : "test",
      "test.framework_version" : ${content_meta_test_framework_version},
      "test.framework" : "junit4"
    }
  }
}, {
  "type" : "span",
  "version" : 1,
  "content" : {
    "trace_id" : ${content_test_session_id},
    "span_id" : ${content_span_id_3},
    "parent_id" : ${content_test_suite_id},
    "service" : "worker.org.gradle.process.internal.worker.gradleworkermain",
    "name" : "setup",
    "resource" : "setup",
    "start" : ${content_start_6},
    "duration" : ${content_duration_6},
    "error" : 0,
    "metrics" : { },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_4},
      "test.callback" : "BeforeParam",
      "library_version" : ${content_meta_library_version},
      "env" : "none"
    }
  }
}, {
  "type" : "span",
  "version" : 1,
  "content" : {
    "trace_id" : ${content_test_session_id},
    "span_id" : ${content_span_id_4},
    "parent_id" : ${content_test_suite_id},
    "service" : "worker.org.gradle.process.internal.worker.gradleworkermain",
    "name" : "setup",
    "resource" : "setup",
    "start" : ${content_start_7},
    "duration" : ${content_duration_7},
    "error" : 0,
    "metrics" : { },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_5},
      "test.callback" : "BeforeParam",
      "library_version" : ${content_meta_library_version},
      "env" : "none"
    }
  }
}, {
  "type" : "span",
  "version" : 1,
  "content" : {
    "trace_id" : ${content_test_session_id},
    "span_id" : ${content_span_id_5},
    "parent_id" : ${content_test_suite_id},
    "service" : "worker.org.gradle.process.internal.worker.gradleworkermain",
    "name" : "tearDown",
    "resource" : "tearDown",
    "start" : ${content_start_8},
    "duration" : ${content_duration_8},
    "error" : 0,
    "metrics" : { },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_6},
      "test.callback" : "AfterParam",
      "library_version" : ${content_meta_library_version},
      "env" : "none"
    }
  }
}, {
  "type" : "span",
  "version" : 1,
  "content" : {
    "trace_id" : ${content_test_session_id},
    "span_id" : ${content_span_id_6},
    "parent_id" : ${content_test_suite_id},
    "service" : "worker.org.gradle.process.internal.worker.gradleworkermain",
    "name" : "tearDown",
    "resource" : "tearDown",
    "start" : ${content_start_9},
    "duration" : ${content_duration_9},
    "error" : 0,
    "metrics" : { },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_7},
      "test.callback" : "AfterParam",
      "library_version" : ${content_meta_library_version},
      "env" : "none"
    }
  }
} ]