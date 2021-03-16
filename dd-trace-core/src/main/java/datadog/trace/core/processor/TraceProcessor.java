package datadog.trace.core.processor;

import datadog.trace.api.Config;
import datadog.trace.core.DDSpan;
import datadog.trace.core.DDSpanContext;
import datadog.trace.core.processor.rule.URLAsResourceNameRule;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TraceProcessor {

  private static final Logger log = LoggerFactory.getLogger(TraceProcessor.class);
  final Rule[] DEFAULT_RULES = new Rule[] {new URLAsResourceNameRule()};

  private final List<Rule> rules;

  public TraceProcessor() {

    rules = new ArrayList<>(DEFAULT_RULES.length);
    for (final Rule rule : DEFAULT_RULES) {
      if (isEnabled(rule)) {
        rules.add(rule);
      }
    }
  }

  private static boolean isEnabled(final Rule rule) {
    boolean enabled = Config.get().isRuleEnabled(rule.getClass().getSimpleName());
    for (final String alias : rule.aliases()) {
      enabled &= Config.get().isRuleEnabled(alias);
    }
    for (final String featureAlias : rule.featureAliases()) {
      if (!Config.get().isRuleEnabled(featureAlias)) {
        rule.disableFeature(featureAlias);
      }
    }
    if (!enabled) {
      log.debug("{} disabled", rule.getClass().getSimpleName());
    }
    return enabled;
  }

  public interface Rule {
    String[] aliases();

    String[] featureAliases();

    void disableFeature(String feature);

    void processSpan(DDSpanContext span);
  }

  public List<DDSpan> onTraceComplete(final List<DDSpan> trace) {
    for (final DDSpan span : trace) {
      applyRules(span);
    }

    // TODO: apply DDTracer's TraceInterceptors
    return trace;
  }

  private void applyRules(final DDSpan span) {
    if (rules.size() > 0) {
      for (final Rule rule : rules) {
        rule.processSpan(span.context());
      }
    }
  }
}
