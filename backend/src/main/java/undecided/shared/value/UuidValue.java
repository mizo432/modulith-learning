package undecided.shared.value;

import java.util.UUID;

/**
 * UUID型の値オブジェクトを表すインターフェースです。
 *
 * <p>このインターフェースは {@link SingleValue} を拡張し、UUID（{@code java.util.UUID}）を
 * 不変の値として扱うための共通の契約を提供します。エンティティの識別子や一意なキーを
 * 表す値オブジェクトを実装する際に使用されます。
 *
 * @param <VO> このインターフェースを実装する具体的な値オブジェクトの型
 * @see SingleValue
 * @see UuidV7IdValue
 */
public interface UuidValue<VO extends UuidValue<VO>> extends SingleValue<UUID> {}
