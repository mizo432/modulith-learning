package undecided.erp;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import undecided.erp.common.annotation.VisibleForFramework;

@AnalyzeClasses(packages = "undecided.erp", importOptions = ImportOption.DoNotIncludeTests.class)
public class VisibleForFrameworkTest {

  @ArchTest
  private final ArchRule rule = ArchRuleDefinition.methods()
      .that().areAnnotatedWith(VisibleForFramework.class)
      .should()
      .onlyBeCalled()
      .byClassesThat()
      .resideOutsideOfPackage("..domain.model..")
      .because(
          """
              Methods annotated with @VisibleForFramework should only be called from classes
              in the 'domain.model' package.""")
      .as("""
          Methods annotated with @VisibleForFramework should only be called from classes 
          in the 'domain.model' package.""")
      .allowEmptyShould(true);


}
