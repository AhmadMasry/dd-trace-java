[ {
  "type" : "test_session_end",
  "version" : 1,
  "content" : {
    "test_session_id" : ${content_test_session_id},
    "service" : "test-maven-service",
    "name" : "maven.test_session",
    "resource" : "Maven Smoke Tests Project",
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
      "test_session.name" : "mvn -B test",
      "ci.workspace_path" : ${content_meta_ci_workspace_path},
      "language" : "jvm",
      "env" : "integration-test",
      "library_version" : ${content_meta_library_version},
      "component" : "maven",
      "test.code_coverage.enabled" : "true",
      "test.toolchain" : ${content_meta_test_toolchain},
      "span.kind" : "test_session_end",
      "runtime-id" : ${content_meta_runtime_id},
      "test.command" : "mvn -B test",
      "test.framework_version" : "2.4.0-M2-groovy-4.0",
      "test.framework" : "spock",
      "runtime.name" : ${content_meta_runtime_name},
      "runtime.vendor" : ${content_meta_runtime_vendor},
      "runtime.version" : ${content_meta_runtime_version},
      "os.architecture" : ${content_meta_os_architecture},
      "os.platform" : ${content_meta_os_platform},
      "os.version" : ${content_meta_os_version}
    }
  }
}, {
  "type" : "test_module_end",
  "version" : 1,
  "content" : {
    "test_session_id" : ${content_test_session_id},
    "test_module_id" : ${content_test_module_id},
    "service" : "test-maven-service",
    "name" : "maven.test_module",
    "resource" : "Maven Smoke Tests Project maven-surefire-plugin default-test",
    "start" : ${content_start_2},
    "duration" : ${content_duration_2},
    "error" : 0,
    "metrics" : {
      "_dd.host.vcpu_count" : ${content_metrics__dd_host_vcpu_count_2}
    },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_2},
      "test.type" : "test",
      "test.module" : "Maven Smoke Tests Project maven-surefire-plugin default-test",
      "test.status" : "pass",
      "test_session.name" : "mvn -B test",
      "ci.workspace_path" : ${content_meta_ci_workspace_path},
      "env" : "integration-test",
      "library_version" : ${content_meta_library_version},
      "component" : "maven",
      "test.code_coverage.enabled" : "true",
      "span.kind" : "test_module_end",
      "test.execution" : "maven-surefire-plugin:test:default-test",
      "test.command" : "mvn -B test",
      "test.framework_version" : "2.4.0-M2-groovy-4.0",
      "test.framework" : "spock",
      "runtime-id" : ${content_meta_runtime_id},
      "language" : "jvm",
      "runtime.name" : ${content_meta_runtime_name},
      "runtime.vendor" : ${content_meta_runtime_vendor},
      "runtime.version" : ${content_meta_runtime_version},
      "os.architecture" : ${content_meta_os_architecture},
      "os.platform" : ${content_meta_os_platform},
      "os.version" : ${content_meta_os_version}
    }
  }
}, {
  "type" : "span",
  "version" : 1,
  "content" : {
    "trace_id" : ${content_test_session_id},
    "span_id" : ${content_span_id},
    "parent_id" : ${content_parent_id},
    "service" : "test-maven-service",
    "name" : "Maven_Smoke_Tests_Project_gmavenplus_plugin_default",
    "resource" : "Maven_Smoke_Tests_Project_gmavenplus_plugin_default",
    "start" : ${content_start_3},
    "duration" : ${content_duration_3},
    "error" : 0,
    "metrics" : { },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_3},
      "execution" : "default",
      "project" : "Maven Smoke Tests Project",
      "library_version" : ${content_meta_library_version},
      "env" : "integration-test",
      "plugin" : "gmavenplus-plugin",
      "runtime-id" : ${content_meta_runtime_id},
      "language" : "jvm",
      "runtime.name" : ${content_meta_runtime_name},
      "runtime.vendor" : ${content_meta_runtime_vendor},
      "runtime.version" : ${content_meta_runtime_version},
      "os.architecture" : ${content_meta_os_architecture},
      "os.platform" : ${content_meta_os_platform},
      "os.version" : ${content_meta_os_version}
    }
  }
}, {
  "type" : "span",
  "version" : 1,
  "content" : {
    "trace_id" : ${content_test_session_id},
    "span_id" : ${content_span_id_2},
    "parent_id" : ${content_parent_id},
    "service" : "test-maven-service",
    "name" : "Maven_Smoke_Tests_Project_gmavenplus_plugin_default",
    "resource" : "Maven_Smoke_Tests_Project_gmavenplus_plugin_default",
    "start" : ${content_start_4},
    "duration" : ${content_duration_4},
    "error" : 0,
    "metrics" : { },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_4},
      "execution" : "default",
      "project" : "Maven Smoke Tests Project",
      "library_version" : ${content_meta_library_version},
      "env" : "integration-test",
      "plugin" : "gmavenplus-plugin",
      "runtime-id" : ${content_meta_runtime_id},
      "language" : "jvm",
      "runtime.name" : ${content_meta_runtime_name},
      "runtime.vendor" : ${content_meta_runtime_vendor},
      "runtime.version" : ${content_meta_runtime_version},
      "os.architecture" : ${content_meta_os_architecture},
      "os.platform" : ${content_meta_os_platform},
      "os.version" : ${content_meta_os_version}
    }
  }
}, {
  "type" : "span",
  "version" : 1,
  "content" : {
    "trace_id" : ${content_test_session_id},
    "span_id" : ${content_span_id_3},
    "parent_id" : ${content_parent_id},
    "service" : "test-maven-service",
    "name" : "Maven_Smoke_Tests_Project_maven_compiler_plugin_default_compile",
    "resource" : "Maven_Smoke_Tests_Project_maven_compiler_plugin_default_compile",
    "start" : ${content_start_5},
    "duration" : ${content_duration_5},
    "error" : 0,
    "metrics" : { },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_5},
      "execution" : "default-compile",
      "project" : "Maven Smoke Tests Project",
      "library_version" : ${content_meta_library_version},
      "env" : "integration-test",
      "plugin" : "maven-compiler-plugin",
      "runtime-id" : ${content_meta_runtime_id},
      "language" : "jvm",
      "runtime.name" : ${content_meta_runtime_name},
      "runtime.vendor" : ${content_meta_runtime_vendor},
      "runtime.version" : ${content_meta_runtime_version},
      "os.architecture" : ${content_meta_os_architecture},
      "os.platform" : ${content_meta_os_platform},
      "os.version" : ${content_meta_os_version}
    }
  }
}, {
  "type" : "span",
  "version" : 1,
  "content" : {
    "trace_id" : ${content_test_session_id},
    "span_id" : ${content_span_id_4},
    "parent_id" : ${content_parent_id},
    "service" : "test-maven-service",
    "name" : "Maven_Smoke_Tests_Project_maven_compiler_plugin_default_testCompile",
    "resource" : "Maven_Smoke_Tests_Project_maven_compiler_plugin_default_testCompile",
    "start" : ${content_start_6},
    "duration" : ${content_duration_6},
    "error" : 0,
    "metrics" : { },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_6},
      "execution" : "default-testCompile",
      "project" : "Maven Smoke Tests Project",
      "library_version" : ${content_meta_library_version},
      "env" : "integration-test",
      "plugin" : "maven-compiler-plugin",
      "runtime-id" : ${content_meta_runtime_id},
      "language" : "jvm",
      "runtime.name" : ${content_meta_runtime_name},
      "runtime.vendor" : ${content_meta_runtime_vendor},
      "runtime.version" : ${content_meta_runtime_version},
      "os.architecture" : ${content_meta_os_architecture},
      "os.platform" : ${content_meta_os_platform},
      "os.version" : ${content_meta_os_version}
    }
  }
}, {
  "type" : "span",
  "version" : 1,
  "content" : {
    "trace_id" : ${content_test_session_id},
    "span_id" : ${content_span_id_5},
    "parent_id" : ${content_parent_id},
    "service" : "test-maven-service",
    "name" : "Maven_Smoke_Tests_Project_maven_resources_plugin_default_resources",
    "resource" : "Maven_Smoke_Tests_Project_maven_resources_plugin_default_resources",
    "start" : ${content_start_7},
    "duration" : ${content_duration_7},
    "error" : 0,
    "metrics" : { },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_7},
      "execution" : "default-resources",
      "project" : "Maven Smoke Tests Project",
      "library_version" : ${content_meta_library_version},
      "env" : "integration-test",
      "plugin" : "maven-resources-plugin",
      "runtime-id" : ${content_meta_runtime_id},
      "language" : "jvm",
      "runtime.name" : ${content_meta_runtime_name},
      "runtime.vendor" : ${content_meta_runtime_vendor},
      "runtime.version" : ${content_meta_runtime_version},
      "os.architecture" : ${content_meta_os_architecture},
      "os.platform" : ${content_meta_os_platform},
      "os.version" : ${content_meta_os_version}
    }
  }
}, {
  "type" : "span",
  "version" : 1,
  "content" : {
    "trace_id" : ${content_test_session_id},
    "span_id" : ${content_span_id_6},
    "parent_id" : ${content_parent_id},
    "service" : "test-maven-service",
    "name" : "Maven_Smoke_Tests_Project_maven_resources_plugin_default_testResources",
    "resource" : "Maven_Smoke_Tests_Project_maven_resources_plugin_default_testResources",
    "start" : ${content_start_8},
    "duration" : ${content_duration_8},
    "error" : 0,
    "metrics" : { },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_8},
      "execution" : "default-testResources",
      "project" : "Maven Smoke Tests Project",
      "library_version" : ${content_meta_library_version},
      "env" : "integration-test",
      "plugin" : "maven-resources-plugin",
      "runtime-id" : ${content_meta_runtime_id},
      "language" : "jvm",
      "runtime.name" : ${content_meta_runtime_name},
      "runtime.vendor" : ${content_meta_runtime_vendor},
      "runtime.version" : ${content_meta_runtime_version},
      "os.architecture" : ${content_meta_os_architecture},
      "os.platform" : ${content_meta_os_platform},
      "os.version" : ${content_meta_os_version}
    }
  }
}, {
  "type" : "test_suite_end",
  "version" : 1,
  "content" : {
    "test_session_id" : ${content_test_session_id},
    "test_module_id" : ${content_test_module_id},
    "test_suite_id" : ${content_test_suite_id},
    "service" : "test-maven-service",
    "name" : "junit.test_suite",
    "resource" : "test_successful_maven_run_junit_platform_runner.src.test.groovy.SampleSpockTest",
    "start" : ${content_start_9},
    "duration" : ${content_duration_9},
    "error" : 0,
    "metrics" : {
      "_dd.host.vcpu_count" : ${content_metrics__dd_host_vcpu_count_3},
      "process_id" : ${content_metrics_process_id_2},
      "_dd.profiling.enabled" : 0,
      "_dd.trace_span_attribute_schema" : 0
    },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_9},
      "test.type" : "test",
      "_dd.tracer_host" : ${content_meta__dd_tracer_host},
      "test.module" : "Maven Smoke Tests Project maven-surefire-plugin default-test",
      "test.status" : "pass",
      "test_session.name" : "mvn -B test",
      "ci.workspace_path" : ${content_meta_ci_workspace_path},
      "language" : "jvm",
      "env" : "integration-test",
      "library_version" : ${content_meta_library_version},
      "component" : "junit",
      "span.kind" : "test_suite_end",
      "test.suite" : "test_successful_maven_run_junit_platform_runner.src.test.groovy.SampleSpockTest",
      "runtime-id" : ${content_meta_runtime_id_2},
      "test.framework_version" : "2.4.0-M2-groovy-4.0",
      "test.framework" : "spock",
      "runtime.name" : ${content_meta_runtime_name},
      "runtime.vendor" : ${content_meta_runtime_vendor},
      "runtime.version" : ${content_meta_runtime_version},
      "os.architecture" : ${content_meta_os_architecture},
      "os.platform" : ${content_meta_os_platform},
      "os.version" : ${content_meta_os_version}
    }
  }
}, {
  "type" : "test",
  "version" : 2,
  "content" : {
    "trace_id" : ${content_trace_id},
    "span_id" : ${content_span_id_7},
    "parent_id" : ${content_parent_id_2},
    "test_session_id" : ${content_test_session_id},
    "test_module_id" : ${content_test_module_id},
    "test_suite_id" : ${content_test_suite_id},
    "service" : "test-maven-service",
    "name" : "junit.test",
    "resource" : "test_successful_maven_run_junit_platform_runner.src.test.groovy.SampleSpockTest.test should pass",
    "start" : ${content_start_10},
    "duration" : ${content_duration_10},
    "error" : 0,
    "metrics" : {
      "_dd.host.vcpu_count" : ${content_metrics__dd_host_vcpu_count_4},
      "process_id" : ${content_metrics_process_id_2},
      "_dd.profiling.enabled" : 0,
      "_dd.trace_span_attribute_schema" : 0
    },
    "meta" : {
      "_dd.p.tid" : ${content_meta__dd_p_tid_10},
      "test.type" : "test",
      "_dd.tracer_host" : ${content_meta__dd_tracer_host},
      "test.source.method" : "test should pass()V",
      "test.module" : "Maven Smoke Tests Project maven-surefire-plugin default-test",
      "test.status" : "pass",
      "test_session.name" : "mvn -B test",
      "ci.workspace_path" : ${content_meta_ci_workspace_path},
      "language" : "jvm",
      "env" : "integration-test",
      "library_version" : ${content_meta_library_version},
      "component" : "junit",
      "test.name" : "test should pass",
      "span.kind" : "test",
      "test.suite" : "test_successful_maven_run_junit_platform_runner.src.test.groovy.SampleSpockTest",
      "runtime-id" : ${content_meta_runtime_id_2},
      "test.framework_version" : "2.4.0-M2-groovy-4.0",
      "test.framework" : "spock",
      "runtime.name" : ${content_meta_runtime_name},
      "runtime.vendor" : ${content_meta_runtime_vendor},
      "runtime.version" : ${content_meta_runtime_version},
      "os.architecture" : ${content_meta_os_architecture},
      "os.platform" : ${content_meta_os_platform},
      "os.version" : ${content_meta_os_version}
    }
  }
} ]