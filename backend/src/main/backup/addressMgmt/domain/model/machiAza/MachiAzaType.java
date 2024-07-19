package undecided.erp.addressMgmt.domain.model.machiAza;

import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.Getter;

/**
 * 町字区分
 * <p>
 * 収録する町字の区分。(1:大字・町　2:丁目　3:小字　4:大字・町/丁目/小字なし 5:道路方式の住居表示における道路名)
 */
@SuppressWarnings("NonAsciiCharacters")
@Getter
public enum MachiAzaType {
  /** 1: 大字・町 */
  大字町("1"),
  /** 2: 丁目 */
  丁目("2"),
  /** 3: 小字 */
  小字("3"),
  /** 4: 大字・町/丁目/小字なし */
  大字町または丁目または小字なし("4"),
  /** 5: 道路方式の住居表示における道路名 */
  道路方式の住居表示における道路名("5");

  private final String code;

  MachiAzaType(String code) {
    this.code = code;
  }

  /**
   * 指定されたコードを持つMachiAzaType列挙型の定数を返します。
   *
   * @param code 探したいMachiAzaType列挙型の定数のコード
   * @return 指定したコードを持つMachiAzaType列挙型の定数
   * @throws NoSuchElementException 指定されたコードを持つMachiAzaType列挙型の定数が見つからない場合
   */
  public static MachiAzaType valueOfCode(String code) {
    return Arrays.stream(values())
        .filter(machiAzaStatus -> machiAzaStatus.getCode().equals(code))
        .findFirst()
        .orElseThrow(() -> noMatchingEnumException(code));

  }

  private static NoSuchElementException noMatchingEnumException(String code) {
    return new NoSuchElementException("No MachiAzaType matching the code: " + code);
  }
}
