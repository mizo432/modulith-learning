package undecided;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;
import undecided.shared.annotation.DoNotCall;
import undecided.shared.annotation.VisibleForTesting;

@Tag("small")
class ModulithTest {

  ApplicationModules modules = ApplicationModules.of(ModulithDemoApplication.class);

  @Test
  void verifyPackageConformity() {
    System.out.println(new File(".").getAbsolutePath());

    System.out.println(modules.toString());
    modules.verify();
  }

  @Test
  void createModulithsDocumentation() {
    new Documenter(modules).writeModulesAsPlantUml().writeIndividualModulesAsPlantUml();
  }

  @Test
  void verifyDoNotCallAnnotationHasDeprecated() throws ClassNotFoundException {
    // DoNotCall アノテーションが付与されたメソッドは必ず @Deprecated も付与されていることを確認
    Class<?>[] classesWithDoNotCall = {
      undecided.shared.primitiveOld.Sets2.class,
      undecided.shared.primitiveOld.UnmodifiableIterator.class
    };

    for (Class<?> clazz : classesWithDoNotCall) {
      Arrays.stream(clazz.getDeclaredMethods())
          .filter(method -> method.isAnnotationPresent(DoNotCall.class))
          .forEach(
              method -> {
                assertThat(method.isAnnotationPresent(Deprecated.class))
                    .as(
                        "DoNotCall アノテーションが付与されたメソッド '%s.%s' は @Deprecated も付与されているべきです",
                        method.getDeclaringClass().getSimpleName(), method.getName())
                    .isTrue();
              });
    }
  }

  @Test
  void verifyVisibleForTestingAnnotationIsStatic() throws ClassNotFoundException {
    // VisibleForTesting アノテーションが付与されたメソッドは static であることを確認
    Class<?>[] classesWithVisibleForTesting = {undecided.shared.entity.ValueObject.class};

    for (Class<?> clazz : classesWithVisibleForTesting) {
      Arrays.stream(clazz.getDeclaredMethods())
          .filter(method -> method.isAnnotationPresent(VisibleForTesting.class))
          .forEach(
              method -> {
                assertThat(Modifier.isStatic(method.getModifiers()))
                    .as(
                        "VisibleForTesting アノテーションが付与されたメソッド '%s.%s' は static であるべきです",
                        method.getDeclaringClass().getSimpleName(), method.getName())
                    .isTrue();
              });
    }
  }

  @Test
  void verifyRestControllerCQRSSeparation() throws ClassNotFoundException {
    // CQRSパターンに従い、RestControllerはQueryとCommandを混在させていないことを確認
    // QueryControllerは@GetMappingのみ、CommandControllerは@PostMapping/@PutMapping/@DeleteMapping/@PatchMappingのみを使用すべき
    Class<?>[] controllerClasses = {
      // Department module
      undecided.association.relationshipMgmt.orgRole.internal.department.DepartmentQueryApi.class,
      undecided.association.relationshipMgmt.orgRole.internal.department.DepartmentCommandApi.class,
      // User module
      undecided.generic.accountMgmt.internal.user.UserQueryApi.class,
      undecided.generic.accountMgmt.internal.user.UserCommandApi.class,
      // Organization module (Query only)
      undecided.association.relationshipMgmt.orgRole.internal.organization.OrganizationApi.class,
      // Role module (Query only)
      undecided.association.relationshipMgmt.partyRole.intenal.role.RoleApi.class,
      undecided.association.relationshipMgmt.partyRole.intenal.role.RoleAssignmentsForEmpApi.class,
      undecided.association.relationshipMgmt.partyRole.intenal.role.RoleAssignmentsForOrgApi.class,
      // Employee module (Query only)
      undecided.association.relationshipMgmt.personRole.internal.employee.EmployeeApi.class,
      // Prefecture module (Query only)
      undecided.generic.addressReg.internal.presentation.api.PrefectureApi.class,
      // Greeting module (Query only)
      undecided.generic.greeting.internal.GreetingApi.class,
      undecided.generic.greeting.internal.Greeting1Api.class,
      undecided.generic.greeting.internal.Greeting2Api.class,
      undecided.generic.greeting.internal.Greeting3Api.class,
      undecided.generic.greeting.internal.Greeting4Api.class,
      undecided.generic.greeting.internal.Greeting5Api.class,
      undecided.generic.greeting.internal.Greeting6Api.class,
      undecided.generic.greeting.internal.Greeting7Api.class
    };

    for (Class<?> controllerClass : controllerClasses) {
      boolean hasGetMapping = false;
      boolean hasCommandMapping = false;

      for (Method method : controllerClass.getDeclaredMethods()) {
        if (method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class)) {
          hasGetMapping = true;
        }
        if (method.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class)
            || method.isAnnotationPresent(org.springframework.web.bind.annotation.PutMapping.class)
            || method.isAnnotationPresent(
                org.springframework.web.bind.annotation.DeleteMapping.class)
            || method.isAnnotationPresent(
                org.springframework.web.bind.annotation.PatchMapping.class)) {
          hasCommandMapping = true;
        }
      }

      assertThat(!(hasGetMapping && hasCommandMapping))
          .as(
              "RestController '%s' は Query(GET) と Command(POST/PUT/DELETE/PATCH) を混在させてはいけません。CQRSパターンに従って分離してください。",
              controllerClass.getSimpleName())
          .isTrue();
    }
  }
}
