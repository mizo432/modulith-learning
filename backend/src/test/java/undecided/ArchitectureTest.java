package undecided;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.Tag;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.shared.annotation.DoNotCall;
import undecided.shared.annotation.VisibleForTesting;

@Tag("small")
class ArchitectureTest {

  @AnalyzeClasses(
      packages = "undecided",
      importOptions = {ImportOption.DoNotIncludeTests.class})
  @Tag("small")
  static class EntityLayerTest {
    @ArchTest
    static final ArchRule entity_classes_should_reside_in_spi_package =
        classes()
            .that()
            .areAnnotatedWith(Entity.class)
            .and()
            .haveNameNotMatching(".*BankMasterImport")
            .and()
            .haveNameNotMatching(".*BranchMasterImport")
            .and()
            .haveNameNotMatching(".*SnowflakeNode")
            .should()
            .resideInAPackage("..spi..")
            .because("Entityクラスはドメイン層(spiパッケージ)に配置されるべきです");
  }

  @AnalyzeClasses(
      packages = "undecided",
      importOptions = {ImportOption.DoNotIncludeTests.class})
  @Tag("small")
  static class RepositoryLayerTest {
    @ArchTest
    static final ArchRule repository_classes_should_reside_in_internal_package =
        classes()
            .that()
            .areAssignableTo(Repository.class)
            .should()
            .resideInAnyPackage("..internal..", "..intenal..")
            .because("Repositoryクラスはアプリケーション層(internalパッケージ)に配置されるべきです");
  }

  @AnalyzeClasses(
      packages = "undecided",
      importOptions = {ImportOption.DoNotIncludeTests.class})
  @Tag("small")
  static class ServiceLayerTest {
    @ArchTest
    static final ArchRule service_classes_should_reside_in_internal_package =
        classes()
            .that()
            .areAnnotatedWith(Service.class)
            .should()
            .resideInAPackage("..internal..")
            .because("Serviceクラスはアプリケーション層(internalパッケージ)に配置されるべきです");
  }

  @AnalyzeClasses(
      packages = "undecided",
      importOptions = {ImportOption.DoNotIncludeTests.class})
  @Tag("small")
  static class ControllerLayerTest {
    @ArchTest
    static final ArchRule rest_controllers_should_reside_in_internal_package =
        classes()
            .that()
            .areAnnotatedWith(RestController.class)
            .should()
            .resideInAnyPackage("..internal..", "..intenal..")
            .because("RESTコントローラーはプレゼンテーション層(internalパッケージ)に配置されるべきです");
  }

  @AnalyzeClasses(
      packages = "undecided",
      importOptions = {ImportOption.DoNotIncludeTests.class})
  @Tag("small")
  static class AnnotationTest {
    @ArchTest
    static final ArchRule do_not_call_methods_should_be_deprecated =
        methods()
            .that()
            .areAnnotatedWith(DoNotCall.class)
            .should()
            .beAnnotatedWith(Deprecated.class)
            .because("DoNotCallアノテーションが付与されたメソッドは@Deprecatedも付与されるべきです");

    @ArchTest
    static final ArchRule visible_for_testing_methods_should_be_static =
        methods()
            .that()
            .areAnnotatedWith(VisibleForTesting.class)
            .should()
            .beStatic()
            .because("VisibleForTestingアノテーションが付与されたメソッドはstaticであるべきです");
  }

  @AnalyzeClasses(
      packages = "undecided",
      importOptions = {ImportOption.DoNotIncludeTests.class})
  @Tag("small")
  static class CQRSTest {
    @ArchTest
    static final ArchRule rest_controllers_should_not_mix_query_and_command =
        classes()
            .that()
            .areAnnotatedWith(RestController.class)
            .should(new NoMixedQueryCommandCondition())
            .because("CQRSパターンに従い、RestControllerはQueryとCommandを混在させてはいけません");
  }

  private static class NoMixedQueryCommandCondition extends ArchCondition<JavaClass> {
    NoMixedQueryCommandCondition() {
      super("not mix query and command methods");
    }

    @Override
    public void check(JavaClass javaClass, ConditionEvents events) {
      boolean hasQuery = false;
      boolean hasCommand = false;

      for (JavaMethod method : javaClass.getMethods()) {
        if (method.isAnnotatedWith(GetMapping.class)) {
          hasQuery = true;
        }
        if (method.isAnnotatedWith(PostMapping.class)
            || method.isAnnotatedWith(PutMapping.class)
            || method.isAnnotatedWith(DeleteMapping.class)
            || method.isAnnotatedWith(PatchMapping.class)) {
          hasCommand = true;
        }
      }

      if (hasQuery && hasCommand) {
        events.add(
            SimpleConditionEvent.violated(
                javaClass, javaClass + " は Query(GET) と Command(POST/PUT/DELETE/PATCH) を混在させています"));
      }
    }
  }
}
