package undecided.erp.relationship.business.command.myCompany;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;

import org.springframework.stereotype.Service;
import undecided.erp.relationship.domain.model.partyRole.orgRole.myCompany.MyCompany;
import undecided.erp.relationship.domain.model.partyRole.orgRole.myCompany.MyCompanyRepository;

/**
 * CreateMyCompanyCommandクラスは、MyCompanyエンティティを処理および永続化するためのビジネスロジックを提供します。
 * <p>
 * このクラスは、MyCompanyエンティティの作成を管理するためのコマンドサービスとして機能します。 例えば、新たな会社を登録する際に、指定されたデータを検証した上で保存操作を行います。
 */
@Service
public class CreateMyCompanyCommand {

  /**
   * MyCompanyエンティティを管理するためのリポジトリインターフェースへの依存性を保持します。
   * <p>
   * このフィールドは、MyCompanyエンティティの永続化や取得操作を行うために使用されます。
   * CreateMyCompanyCommandクラスのビジネスロジックにおいて、データ層にアクセスする役割を担います。 Spring Frameworkにより依存性注入されます。
   */
  private final MyCompanyRepository myCompanyRepository;

  /**
   * コンストラクタは CreateMyCompanyCommand クラスのインスタンスを初期化します。
   *
   * @param myCompanyRepository MyCompanyエンティティを管理するためのリポジトリ。null であってはなりません。
   */
  public CreateMyCompanyCommand(MyCompanyRepository myCompanyRepository) {
    this.myCompanyRepository = myCompanyRepository;
  }


  /**
   * 指定された MyCompany エンティティを処理して保存するためのコマンドを実行します。
   * <p>
   * 渡された MyCompany オブジェクトが null でないことを確認した上で永続化を行います。
   *
   * @param myCompany 処理および保存する MyCompany エンティティ。null であってはなりません。
   * @return 保存された MyCompany エンティティ。
   * @throws IllegalArgumentException 渡された MyCompany が null の場合にスローされます。
   */
  public MyCompany execute(MyCompany myCompany) {
    checkNotNull(myCompany,
        () -> new IllegalArgumentException("MyCompany must not be null"));
    return myCompanyRepository.save(myCompany);

  }
}
