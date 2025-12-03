package undecided.erp.depertment.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.NonNull;

public class Department {

  private String departmentId;
  private UUID depertmentUuid;
  private Department parent;
  private List<Department> childDparents = new ArrayList<>();
  private List<ChangeRequestSummary> changeRequestSummaries = new ArrayList<>();

  public Department(String departmentId) {
    if (departmentId == null) {
      throw new IllegalArgumentException("parentOrganizationId cannot be null");
    }
    this.departmentId = departmentId;
  }

  public Department(String departmentId, UUID depertmentUuid) {
    this.departmentId = departmentId;
    this.depertmentUuid = depertmentUuid;
  }

  public static Department create(Organization organization) {
    return new Department(organization.getOrganizationId(), organization.getOrganizationUuid());
  }

  public static Department createDummy(String parentOrganizationId) {
    return new Department(parentOrganizationId);
  }

  public static Department create(@NonNull ChangeRequest changeRequest) {
    Department result = new Department(changeRequest.getOrganizationId());
    result.changeRequestSummaries(ChangeRequestSummary.create(changeRequest));
    return result;
  }

  private void changeRequestSummaries(ChangeRequestSummary changeRequestSummary) {
    changeRequestSummaries.add(changeRequestSummary);
  }

  public void addChild(Department newDepartment) {
    childDparents.add(newDepartment);
  }

  public UUID getDepertmentUuid() {
    return depertmentUuid;
  }

  Department getParentDepartment() {
    return parent;
  }

  public void setParentDepartment(Department parentDepartment) {
    this.parent = parentDepartment;
  }

  public List<Department> getChildDparents() {
    return childDparents;
  }

  public void addChangeRequestSummary(@NonNull ChangeRequest changeRequest) {
    changeRequestSummaries.add(ChangeRequestSummary.create(changeRequest));
  }
}
