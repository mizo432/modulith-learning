package undecided.erp.depertment.spi;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;
import static undecided.erp.common.primitive.Objects2.isNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.NonNull;

/**
 * 部署ツリーを管理するクラスです。
 *
 * <pre>
 * このクラスは、組織の所属関係を階層構造として表現し、操作を行います。
 * 組織情報を基に親子関係を構築し、部署オブジェクトを管理します。
 * </pre>
 */
public class DepartmentTree {
  Map<String, Department> departmentIdMap = new HashMap<>();
  Map<UUID, Department> departmentUuidMap = new HashMap<>();
  Map<Integer, Department> changeRequestNumberMap = new HashMap<>();

  /**
   * 指定された組織を部署ツリーに追加します。
   *
   * @param organization 追加対象の組織オブジェクト。組織IDを基に対応する部署が存在するかを確認し、存在しない場合は新規で作成されます。
   *     また、親組織IDを基に親部署との関係が構築されます。
   */
  public void add(@NonNull Organization organization) {
    Department parentDepartment = resolveParent(organization.getParentOrganizationId());
    Department department = departmentIdMap.get(organization.getOrganizationId());
    if (isNull(department)) {
      department = Department.create(organization);
      departmentUuidMap.put(department.getDepertmentUuid(), department);
      departmentIdMap.put(organization.getOrganizationId(), department);
    }
    linkParentAndChild(parentDepartment, department);
  }

  /**
   * 複数の組織を一度に追加します。
   *
   * @param organizations 追加する組織のリスト。各組織は階層構造内に追加され、 必要に応じて親組織が作成または更新されます。
   */
  public void addAllOrganizations(@NonNull Collection<Organization> organizations) {
    checkNotNull(organizations, () -> new IllegalArgumentException("organizations is null"));
    organizations.forEach(this::add);
  }

  /**
   * 指定された変更リクエストを部署ツリーに追加します。 変更リクエストに基づいて対応する部署が存在しない場合、新規に作成されます。
   * また、変更リクエストの親組織IDを基に親部門との関係が構築されます。
   *
   * @param changeRequest 追加する変更リクエストオブジェクト。変更リクエスト番号、組織ID、 親組織IDなどの情報を含みます。nullではない必要があります。
   *     組織IDに基づいて既存の部署との関連付けが行われるか、 部署が新規作成されます。
   */
  public void add(@NonNull ChangeRequest changeRequest) {
    Department parentDepartment = resolveParent(changeRequest.getParentOrganizationId());
    Department department = departmentIdMap.get(changeRequest.getOrganizationId());

    if (isNull(department)) {
      department = Department.create(changeRequest);
      changeRequestNumberMap.put(changeRequest.getChengeRequestNumber(), department);
      departmentIdMap.put(changeRequest.getOrganizationId(), department);
    } else {
      department.addChangeRequestSummary(changeRequest);
    }
    linkParentAndChild(parentDepartment, department);
  }

  /**
   * 指定された複数の変更リクエストを部署ツリーに一括で追加します。 各変更リクエストに基づいて処理を行い、既存部署への関連付けまたは新規部署の作成を行います。
   *
   * @param changeRequests 追加する変更リクエストのコレクション。nullを許容しません。 各変更リクエストには組織ID、親組織ID、変更リクエスト番号などの
   *     必須情報が含まれている必要があります。
   */
  public void addAllChangeRequest(@NonNull Collection<ChangeRequest> changeRequests) {
    checkNotNull(changeRequests, () -> new IllegalArgumentException("changeRequests is null"));
    changeRequests.forEach(this::add);
  }

  /**
   * 指定された親部署と子部署をリンクします。
   *
   * <p>親部署に子部署を追加し、子部署に親部署を設定することによってツリー構造を構築します。
   *
   * @param parent 親部署オブジェクト。子部署を追加する対象の部署。
   * @param child 子部署オブジェクト。親部署にリンクされる部署。
   */
  private void linkParentAndChild(Department parent, Department child) {
    parent.addChild(child);
    child.setParentDepartment(parent);
  }

  /**
   * 指定された親組織IDに基づいて親部署を解決します。
   *
   * <p>親組織IDに対応する部署が存在しない場合は、新しい部署を作成します。 これにより、親部署が明示的に設定されていない場合でも、部署階層構造が維持されます。
   *
   * @param parentOrganizationId 親組織を特定するための組織ID。nullではない必要があります。
   * @return 親組織IDに対応する部署オブジェクト。対応する部署が存在しない場合は、新規に作成された部署オブジェクトを返します。
   */
  private Department resolveParent(String parentOrganizationId) {
    return departmentIdMap.computeIfAbsent(parentOrganizationId, Department::createDummy);
  }
}
