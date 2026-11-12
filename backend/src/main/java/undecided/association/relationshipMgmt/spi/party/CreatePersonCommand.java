package undecided.association.relationshipMgmt.spi.party;

import org.jspecify.annotations.NonNull;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 個人(Person)の作成コマンドインターフェースです。
 *
 * <p>外部モジュールから個人を作成するための公開APIとして機能します。
 */
public interface CreatePersonCommand {

  /**
   * 個人情報を作成して永続化します。
   *
   * <p>引数で渡された個人情報を基にエンティティを作成し、データベースに保存します。
   * 重複するコードが存在する場合は、{@link com.github.mizo432.shared.exception.BusinessException} がスローされます。
   *
   * @param person 作成する個人の情報（null は不可）
   * @return 作成された個人の Snowflake ID
   * @throws com.github.mizo432.shared.exception.BusinessException 引数が null の場合、または重複するコードが存在する場合
   */
  @NonNull
  SnowflakeId create(@NonNull Person person);
}
