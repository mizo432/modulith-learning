package undecided.generic.documentMgmt.spi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * テストリスト項目8, 9: バージョン値オブジェクトのテスト
 *
 * <p>- 項目8: 初期値が1であるとき、初期バージョンと判定される
 * <p>- 項目9: インクリメントしたとき、次の値が返される
 */
@Tag("small")
@DisplayName("DocumentVersionの単体テスト")
class DocumentVersionTest {

  @Test
  @DisplayName("初期バージョンの値が1で、初期バージョンと判定されること")
  void shouldBeInitialWhenValueIsOne() {
    DocumentVersion initial = DocumentVersion.initial();

    assertThat(initial.value()).isEqualTo(DocumentVersion.INITIAL_VALUE).isEqualTo(1);
    assertThat(initial.isInitial()).isTrue();
    assertThat(new DocumentVersion(2).isInitial()).isFalse();
  }

  @Test
  @DisplayName("インクリメントしたとき次の値のバージョンが返されること")
  void shouldReturnNextVersionWhenIncremented() {
    assertThat(new DocumentVersion(1).increment()).isEqualTo(new DocumentVersion(2));
    assertThat(new DocumentVersion(2).increment()).isEqualTo(new DocumentVersion(3));
  }
}
