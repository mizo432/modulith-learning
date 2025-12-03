package undecided.erp.depertment.spi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DepartmentTree のテスト")
class DepartmentTreeTest {

  @Nested
  @DisplayName("add メソッドのテスト")
  class AddMethodTests {

    @Test
    @DisplayName("新しい部門を追加すると正しく親子関係が構築される")
    void shouldAddNewDepartment() {
      // given
      DepartmentTree departmentTree = new DepartmentTree();
      String parentOrganizationId = "1000000000";
      UUID organizationUuid = UUID.randomUUID();
      Organization organization =
          new Organization(
              "2000000000", organizationUuid, "部門A", null, null, null, null, null, null) {
            @Override
            public String getParentOrganizationId() {
              return parentOrganizationId;
            }
          };

      // when
      departmentTree.add(organization);

      // then
      Department parentDepartment = departmentTree.departmentIdMap.get(parentOrganizationId);
      assertThat(parentDepartment).isNotNull();
      assertThat(parentDepartment.getChildDparents()).hasSize(1);

      Department childDepartment =
          departmentTree.departmentIdMap.get(organization.getOrganizationId());
      assertThat(childDepartment).isNotNull();
      assertThat(childDepartment).isIn(parentDepartment.getChildDparents());
      assertThat(childDepartment.getParentDepartment()).isEqualTo(parentDepartment);
    }

    @Test
    @DisplayName("既存の部門とその子の追加は、再作成せずに既存の部門を利用する")
    void shouldReuseExistingDepartments() {
      // given
      DepartmentTree departmentTree = new DepartmentTree();
      UUID organizationUuid = UUID.randomUUID();
      UUID childOrganizationUuid = UUID.randomUUID();
      String parentOrganizationId = "1000000000";
      String childOrganizationId = "2000000000";

      Organization organization =
          new Organization(
              childOrganizationId,
              childOrganizationUuid,
              "部門A",
              null,
              null,
              null,
              null,
              null,
              null) {
            @Override
            public String getParentOrganizationId() {
              return parentOrganizationId;
            }
          };

      Department existingParent = Department.createDummy(parentOrganizationId);
      departmentTree.departmentIdMap.put(parentOrganizationId, existingParent);

      // when
      departmentTree.add(organization);

      // then
      assertThat(departmentTree.departmentIdMap.get(parentOrganizationId)).isSameAs(existingParent);
      assertThat(existingParent.getChildDparents()).hasSize(1);

      Department childDepartment = departmentTree.departmentIdMap.get(childOrganizationId);
      assertThat(childDepartment).isNotNull();
      assertThat(childDepartment).isIn(existingParent.getChildDparents());
    }

    @Test
    @DisplayName("組織に親部門がない場合、ダミーの親部門が作成される")
    void shouldCreateDummyParentDepartmentIfNotPresent() {
      // given
      DepartmentTree departmentTree = new DepartmentTree();
      String parentOrganizationId = "1000000000";
      UUID organizationUuid = UUID.randomUUID();
      Organization organization =
          new Organization(
              "2000000000", organizationUuid, "部門A", null, null, null, null, null, null) {
            @Override
            public String getParentOrganizationId() {
              return parentOrganizationId;
            }
          };

      // when
      departmentTree.add(organization);

      // then
      assertThat(departmentTree.departmentIdMap).containsKey(parentOrganizationId);
      Department dummyParent = departmentTree.departmentIdMap.get(parentOrganizationId);
      assertThat(dummyParent).isNotNull();
      assertThat(dummyParent.getChildDparents()).isNotEmpty();
    }
  }
}
