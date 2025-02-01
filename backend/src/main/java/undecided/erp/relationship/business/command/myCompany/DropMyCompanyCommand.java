package undecided.erp.relationship.business.command.myCompany;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.relationship.domain.model.partyRole.orgRole.myCompany.MyCompanyRepository;

@Service
@RequiredArgsConstructor
public class DropMyCompanyCommand {

  private final MyCompanyRepository myCompanyRepository;

  /**
   * 指定されたSnowflakeIdに対応するMyCompanyエンティティを削除します。
   * <p>
   * 該当するエンティティが存在しない場合は、IllegalArgumentExceptionをスローします。
   *
   * @param myCompanyId 削除対象のMyCompanyエンティティの識別子。nullであってはならない。
   * @throws IllegalArgumentException 指定されたIDのMyCompanyエンティティが存在しない場合にスローされます。
   */
  public void execute(@NonNull SnowflakeId myCompanyId) {

    if (myCompanyRepository.existsById(myCompanyId)) {
      myCompanyRepository.deleteById(myCompanyId);
      return;

    }
    throw new IllegalArgumentException("MyCompany not found. myCompanyId: " + myCompanyId);

  }

}
