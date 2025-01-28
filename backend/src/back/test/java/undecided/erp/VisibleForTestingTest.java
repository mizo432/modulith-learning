package undecided.erp;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import undecided.erp.common.annotation.VisibleForTesting;

@AnalyzeClasses(packages = "undecided.erp")
public class VisibleForTestingTest {

  @ArchTest
  private final ArchRule rule = ArchRuleDefinition.methods()
      .that().areAnnotatedWith(VisibleForTesting.class)
      .should().onlyBeCalled()
      .byClassesThat()
      .haveSimpleNameEndingWith("Test")
      .allowEmptyShould(true);


}
