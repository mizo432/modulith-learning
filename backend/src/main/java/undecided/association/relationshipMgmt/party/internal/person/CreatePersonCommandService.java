package undecided.association.relationshipMgmt.party.internal.person;

import com.github.mizo432.shared.exception.BusinessException;
import com.github.mizo432.shared.message.ResultMessage;
import com.github.mizo432.shared.message.ResultMessages;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.spi.party.CreatePersonCommand;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 個人(Person)の作成コマンドを処理するサービスです。
 *
 * <p>{@link PersonFactory}を使用して個人エンティティを作成し、 {@link PersonRepository}に永続化します。
 */
@Service
public class CreatePersonCommandService implements CreatePersonCommand {

  private final PersonRepository personRepository;
  private final PersonFactory personFactory;

  public CreatePersonCommandService(
      PersonRepository personRepository, PersonFactory personFactory) {
    this.personRepository = personRepository;
    this.personFactory = personFactory;
  }

  /**
   * 個人情報を作成して永続化します。
   *
   * <p>引数で渡された個人情報を基に {@link PersonFactory} でエンティティを作成し、 {@link PersonRepository} に保存します。
   * 重複するコードが存在する場合は、{@link BusinessException} がスローされます。
   *
   * @param person 作成する個人の情報（null は不可）
   * @return 作成された個人の Snowflake ID
   * @throws BusinessException 引数が null の場合、または重複するコードが存在する場合
   */
  @Transactional(rollbackFor = Exception.class)
  @Override
  @NonNull
  public SnowflakeId create(@NonNull Person person) {
    if (person == null) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText("個人情報はnullにできません")));
    }
    try {
      Person p = personFactory.create(person);
      Person saved = personRepository.save(p);
      return saved.getPartyId();
    } catch (DataIntegrityViolationException e) {
      throw new BusinessException(
          ResultMessages.error()
              .add(ResultMessage.fromText("指定されたコードを持つ個人は既に存在します: " + person.getCode())),
          e);
    }
  }
}
