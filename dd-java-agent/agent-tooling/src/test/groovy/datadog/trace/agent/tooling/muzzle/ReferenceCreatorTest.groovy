package datadog.trace.agent.tooling.muzzle

import datadog.trace.test.util.DDSpecification
import spock.lang.Ignore

import static TestAdviceClasses.InstanceofAdvice
import static TestAdviceClasses.LdcAdvice
import static TestAdviceClasses.MethodBodyAdvice

class ReferenceCreatorTest extends DDSpecification {
  def "method body creates references"() {
    setup:
    Map<String, Reference> references = ReferenceCreator.createReferencesFrom(MethodBodyAdvice.name, this.class.classLoader)

    expect:
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$A') != null
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$B') != null
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$SomeInterface') != null
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$SomeImplementation') != null
    references.keySet().size() == 4

    // interface flags
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$B').flags.contains(Reference.Flag.NON_INTERFACE)
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$SomeInterface').flags.contains(Reference.Flag.INTERFACE)

    // class access flags
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$A').flags.contains(Reference.Flag.PACKAGE_OR_HIGHER)
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$B').flags.contains(Reference.Flag.PACKAGE_OR_HIGHER)

    // method refs
    Set<Reference.Method> bMethods = references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$B').methods
    findMethod(bMethods, "aMethod", "(Ljava/lang/String;)Ljava/lang/String;") != null
    findMethod(bMethods, "aMethodWithPrimitives", "(Z)V") != null
    findMethod(bMethods, "aStaticMethod", "()V") != null
    findMethod(bMethods, "aMethodWithArrays", "([Ljava/lang/String;)[Ljava/lang/Object;") != null

    findMethod(bMethods, "aMethod", "(Ljava/lang/String;)Ljava/lang/String;").flags.contains(Reference.Flag.NON_STATIC)
    findMethod(bMethods, "aStaticMethod", "()V").flags.contains(Reference.Flag.STATIC)

    // field refs
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$B').fields.isEmpty()
    Set<Reference.Field> aFieldRefs = references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$A').fields
    findField(aFieldRefs, "b").flags.contains(Reference.Flag.PACKAGE_OR_HIGHER)
    findField(aFieldRefs, "b").flags.contains(Reference.Flag.NON_STATIC)
    findField(aFieldRefs, "staticB").flags.contains(Reference.Flag.PACKAGE_OR_HIGHER)
    findField(aFieldRefs, "staticB").flags.contains(Reference.Flag.STATIC)
    aFieldRefs.size() == 2
  }

  def "protected ref test"() {
    setup:
    Map<String, Reference> references = ReferenceCreator.createReferencesFrom(MethodBodyAdvice.B2.name, this.class.classLoader)

    expect:
    Set<Reference.Method> bMethods = references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$B').methods
    findMethod(bMethods, "protectedMethod", "()V") != null
    findMethod(bMethods, "protectedMethod", "()V").flags.contains(Reference.Flag.PROTECTED_OR_HIGHER)
  }

  def "ldc creates references"() {
    setup:
    Map<String, Reference> references = ReferenceCreator.createReferencesFrom(LdcAdvice.name, this.class.classLoader)

    expect:
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$A') != null
  }

  def "interface impl creates references"() {
    setup:
    Map<String, Reference> references = ReferenceCreator.createReferencesFrom(MethodBodyAdvice.SomeImplementation.name, this.class.classLoader)

    expect:
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$SomeInterface') != null
    references.size() == 1
  }

  def "child class creates references"() {
    setup:
    Map<String, Reference> references = ReferenceCreator.createReferencesFrom(MethodBodyAdvice.A2.name, this.class.classLoader)

    expect:
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$A') != null
    references.size() == 1
  }

  def "instanceof creates references"() {
    setup:
    Map<String, Reference> references = ReferenceCreator.createReferencesFrom(InstanceofAdvice.name, this.class.classLoader)

    expect:
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$A') != null
  }

  // TODO: remove ignore when we drop java 7 support.
  @Ignore
  def "invokedynamic creates references"() {
    setup:
    Map<String, Reference> references = ReferenceCreator.createReferencesFrom(TestAdviceClasses.InDyAdvice.name, this.class.classLoader)

    expect:
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$HasMethod') != null
    references.get('datadog.trace.agent.tooling.muzzle.TestAdviceClasses$MethodBodyAdvice$B') != null
  }

  private static Reference.Method findMethod(Set<Reference.Method> methods, String methodName, String methodDesc) {
    for (Reference.Method method : methods) {
      if (method == new Reference.Method(methodName, methodDesc)) {
        return method
      }
    }
    return null
  }

  private static Reference.Field findField(Set<Reference.Field> fields, String fieldName) {
    for (Reference.Field field : fields) {
      if (field.name.equals(fieldName)) {
        return field
      }
    }
    return null
  }
}
