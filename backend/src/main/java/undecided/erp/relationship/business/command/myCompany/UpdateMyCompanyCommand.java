package undecided.erp.relationship.business.command.myCompany;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.relationship.domain.model.partyRole.orgRole.myCompany.MyCompany;
import undecided.erp.relationship.domain.model.partyRole.orgRole.myCompany.MyCompanyRepository;

/**
 * 指定されたIDで識別される会社の情報をシステム内で更新します。
 * <p>
 * このクラスは、MyCompanyエンティティのインスタンスを管理および更新するための サービス層の実装を提供します。基盤となるデータリポジトリを使用して操作を行います。
 */
@Service
@RequiredArgsConstructor
public class UpdateMyCompanyCommand {

  /**
   * MyCompanyエンティティを管理するリポジトリオブジェクト。
   * <p>
   * このフィールドは、MyCompanyエンティティに対するCRUD操作やカスタム操作を実行するために使用されます。
   */
  private final MyCompanyRepository myCompanyRepository;

  /**
   * 指定されたIDで識別される会社の情報をシステム内で更新します。
   * <p>
   * 会社が存在しない場合は例外をスローします。
   *
   * @param myCompanyId 更新対象の会社を一意に識別するID
   * @param myCompany 更新する会社の詳細を含むオブジェクト
   * @throws IllegalArgumentException 指定されたIDの会社が存在しない場合
   */
  public void execute(@NonNull SnowflakeId myCompanyId, @NonNull MyCompany myCompany) {
    if (myCompanyRepository.existsById(myCompanyId)) {
      myCompany.setMyCompanyId(myCompanyId);
      myCompanyRepository.save(myCompany);
      return;

    }
    throw new IllegalArgumentException("MyCompany not found. myCompanyId: " + myCompanyId);

  }
}
