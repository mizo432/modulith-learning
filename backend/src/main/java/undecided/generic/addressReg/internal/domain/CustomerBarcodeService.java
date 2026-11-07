package undecided.generic.addressReg.internal.domain;

import java.util.List;
import java.util.Map;

/**
 * カスタマバーコードを生成するドメインサービス。
 *
 * <p>日本郵政の仕様に準拠し、郵便番号と住所表示番号からカスタマバーコード文字列を生成します。
 * カスタマバーコードのフォーマットは以下の通りです：
 * スタートコード(1桁) + 郵便番号(7桁) + 住所表示番号(13桁) + チェックデジット(1桁) + ストップコード(1桁) = 合計23桁
 *
 * <p>仕様参照:
 * https://www.post.japanpost.jp/service/search/zipcode/zipmanual/p17.html
 */
public final class CustomerBarcodeService {

  private static final int POSTAL_CODE_LENGTH = 7;
  private static final int ADDRESS_NUMBER_LENGTH = 13;
  private static final int TOTAL_DATA_LENGTH = POSTAL_CODE_LENGTH + ADDRESS_NUMBER_LENGTH;
  private static final char START_CODE = '(';
  private static final char STOP_CODE = ')';
  private static final String CC4 = "CC4";

  /**
   * バーコード用キャラクタからチェック用数字へのマッピング。
   * Map.ofEntries()を使用して11エントリを超えるマップを定義します。
   */
  private static final Map<String, Integer> CHECK_DIGIT_MAP = Map.ofEntries(
      Map.entry("0", 0), Map.entry("1", 1), Map.entry("2", 2), Map.entry("3", 3), Map.entry("4", 4),
      Map.entry("5", 5), Map.entry("6", 6), Map.entry("7", 7), Map.entry("8", 8), Map.entry("9", 9),
      Map.entry("CC1", 10), Map.entry("CC2", 11), Map.entry("CC3", 12), Map.entry("CC4", 13),
      Map.entry("CC5", 14), Map.entry("CC6", 15), Map.entry("CC7", 16), Map.entry("CC8", 17),
      Map.entry("-", 18)
  );

  private CustomerBarcodeService() {
    // Utility class
  }

  /**
   * 郵便番号と住所表示番号からカスタマバーコードを生成します。
   *
   * @param postalCode 郵便番号（ハイフンなしの7桁、例: "1000013"）
   * @param addressNumber 住所表示番号（例: "1-3-2-503"）
   * @return カスタマバーコード文字列（例: "(10000131-3-2-503CC4CC4CC4CC4CC49)"）
   */
  public static String generate(String postalCode, String addressNumber) {
    if (postalCode == null || postalCode.isBlank()) {
      throw new IllegalArgumentException("郵便番号は必須です");
    }

    // 郵便番号からハイフンを除去
    String cleanPostalCode = postalCode.replace("-", "").trim();
    if (cleanPostalCode.length() != POSTAL_CODE_LENGTH) {
      throw new IllegalArgumentException("郵便番号はハイフンなしで7桁である必要があります: " + postalCode);
    }

    // 住所表示番号の処理
    String cleanAddressNumber = addressNumber != null ? addressNumber.trim() : "";

    // 住所表示番号が13桁を超える場合は truncation
    if (cleanAddressNumber.length() > ADDRESS_NUMBER_LENGTH) {
      cleanAddressNumber = cleanAddressNumber.substring(0, ADDRESS_NUMBER_LENGTH);
    }

    // 住所表示番号が13桁に満たない場合はCC4で埋める
    while (cleanAddressNumber.length() < ADDRESS_NUMBER_LENGTH) {
      // CC4は2桁として扱う
      if (cleanAddressNumber.length() + 2 <= ADDRESS_NUMBER_LENGTH) {
        cleanAddressNumber += CC4;
      } else {
        cleanAddressNumber += "C"; // 1桁だけ必要な場合はCのみ
      }
    }

    // データ部分の連結（郵便番号 + 住所表示番号）
    String dataPart = cleanPostalCode + cleanAddressNumber;

    // チェックデジットの計算
    int checkDigit = calculateCheckDigit(dataPart);

    // カスタマバーコードの組み立て
    return START_CODE + dataPart + checkDigit + STOP_CODE;
  }

  /**
   * チェックデジットを計算します。
   * 各キャラクタをチェック用数字に置き換え、合計が19の倍数となるように生成します。
   *
   * @param dataPart 郵便番号と住所表示番号を連結した文字列
   * @return チェックデジット（0-9の数字）
   */
  private static int calculateCheckDigit(String dataPart) {
    int sum = 0;
    for (int i = 0; i < dataPart.length(); i++) {
      char c = dataPart.charAt(i);
      String charStr = String.valueOf(c);

      // CC1-CC8の制御コードをチェック
      if (i + 2 < dataPart.length()) {
        String threeChar = dataPart.substring(i, Math.min(i + 3, dataPart.length()));
        if (CHECK_DIGIT_MAP.containsKey(threeChar)) {
          sum += CHECK_DIGIT_MAP.get(threeChar);
          i += 2; // CCxは3文字なのでスキップ
          continue;
        }
        String twoChar = dataPart.substring(i, Math.min(i + 2, dataPart.length()));
        if (CHECK_DIGIT_MAP.containsKey(twoChar)) {
          sum += CHECK_DIGIT_MAP.get(twoChar);
          i += 1; // 2文字なのでスキップ
          continue;
        }
      }

      // 通常のキャラクタ
      if (CHECK_DIGIT_MAP.containsKey(charStr)) {
        sum += CHECK_DIGIT_MAP.get(charStr);
      }
    }

    // 合計が19の倍数となるチェックデジットを計算
    int remainder = sum % 19;
    int checkDigit = (remainder == 0) ? 0 : 19 - remainder;

    // チェックデジットが10以上になる場合は、9を返す（仕様により0-9のみ）
    return Math.min(checkDigit, 9);
  }

  /**
   * 住所から住所表示番号を抽出します。
   * 日本郵政の仕様に従い、住所B（町域名以降）からバーコードに必要な文字情報を抜き出します。
   *
   * @param addressB 町域名以降の住所（例: "1丁目3番2号 郵便プラザ503室"）
   * @return 住所表示番号（例: "1-3-2-503"）
   */
  public static String extractAddressNumber(String addressB) {
    if (addressB == null || addressB.isBlank()) {
      return "";
    }

    String result = addressB;

    // 1. 小文字を大文字に置き換え
    result = result.toLowerCase().toUpperCase();

    // 2. 不要な文字を除去
    result = result.replace("&", "")
        .replace("/", "")
        .replace("・", "")
        .replace(".", "");

    // 3. 漢数字を算用数字に変換（特定文字の前にある場合）
    result = convertKanjiNumbers(result);

    // 4. 算用数字、ハイフン、連続していないアルファベット1文字を抜き出し
    //    漢字・かな・カタカナ・ブランク・2文字以上連続したアルファベットはハイフンに置き換え
    result = extractBarcodeChars(result);

    // 5. ハイフンの連続を1つにまとめる
    while (result.contains("--")) {
      result = result.replace("--", "-");
    }

    // 6. 先頭のハイフンを除去
    while (result.startsWith("-")) {
      result = result.substring(1);
    }

    // 7. 末尾のハイフンを除去
    while (result.endsWith("-")) {
      result = result.substring(0, result.length() - 1);
    }

    // 8. アルファベット文字の前後のハイフンを除去
    result = removeHyphensAroundAlpha(result);

    return result;
  }

  /**
   * 漢数字を算用数字に変換します。
   * 特定文字（丁目、丁、番地、番、号、地割、線、の、ノ）の前にある漢数字を変換します。
   * 長い漢数字から優先して変換することで、「十一丁目」→「11丁目」と正しく変換します。
   */
  private static String convertKanjiNumbers(String input) {
    // 長い漢数字から優先して変換するため、文字列の長さでソート
    List<String[]> kanjiMappings = List.of(
        new String[]{"二十三", "23"}, new String[]{"二十二", "22"}, new String[]{"二十一", "21"},
        new String[]{"二十九", "29"}, new String[]{"二十八", "28"}, new String[]{"二十七", "27"},
        new String[]{"二十六", "26"}, new String[]{"二十五", "25"}, new String[]{"二十四", "24"},
        new String[]{"二十三", "23"}, new String[]{"二十二", "22"}, new String[]{"二十一", "21"},
        new String[]{"十九", "19"}, new String[]{"十八", "18"}, new String[]{"十七", "17"},
        new String[]{"十六", "16"}, new String[]{"十五", "15"}, new String[]{"十四", "14"},
        new String[]{"十三", "13"}, new String[]{"十二", "12"}, new String[]{"十一", "11"},
        new String[]{"三十", "30"}, new String[]{"二十", "20"},
        new String[]{"十", "10"},
        new String[]{"九", "9"}, new String[]{"八", "8"}, new String[]{"七", "7"},
        new String[]{"六", "6"}, new String[]{"五", "5"}, new String[]{"四", "4"},
        new String[]{"三", "3"}, new String[]{"二", "2"}, new String[]{"一", "1"}
    );

    String[] specificWords = {"丁目", "番地", "番", "号", "地割", "線", "の", "ノ", "丁 "};

    for (String specificWord : specificWords) {
      // 長い漢数字から優先して処理
      for (String[] mapping : kanjiMappings) {
        String kanji = mapping[0];
        String number = mapping[1];
        String key = kanji + specificWord;
        String value = number + specificWord;
        input = input.replace(key, value);
      }
    }

    return input;
  }

  /**
   * バーコードに必要な文字情報を抜き出します。
   */
  private static String extractBarcodeChars(String input) {
    StringBuilder result = new StringBuilder();
    boolean prevWasAlpha = false;
    int consecutiveAlphaCount = 0;

    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);

      if (Character.isDigit(c)) {
        result.append(c);
        prevWasAlpha = false;
        consecutiveAlphaCount = 0;
      } else if (c == '-') {
        result.append(c);
        prevWasAlpha = false;
        consecutiveAlphaCount = 0;
      } else if (Character.isLetter(c)) {
        consecutiveAlphaCount++;
        if (consecutiveAlphaCount == 1) {
          // 単独のアルファベット1文字
          // 算用数字に続く"F"は除外（ただしF以降に抜き出し対象がある場合はハイフンに置き換え）
          if (c == 'F' && result.length() > 0 && Character.isDigit(result.charAt(result.length() - 1))) {
            // F以降に数字やハイフンがあるか確認
            boolean hasMoreTarget = false;
            for (int j = i + 1; j < input.length(); j++) {
              char next = input.charAt(j);
              if (Character.isDigit(next) || next == '-') {
                hasMoreTarget = true;
                break;
              }
            }
            if (hasMoreTarget) {
              result.append('-');
            }
          } else {
            result.append(c);
          }
        } else {
          // 2文字以上連続したアルファベットはそのまま
          result.append(c);
        }
        prevWasAlpha = true;
      } else {
        // 漢字・かな・カタカナ・ブランク等
        if (prevWasAlpha || result.length() > 0) {
          result.append('-');
        }
        prevWasAlpha = false;
        consecutiveAlphaCount = 0;
      }
    }

    return result.toString();
  }

  /**
   * アルファベット文字の前後のハイフンを除去します。
   */
  private static String removeHyphensAroundAlpha(String input) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (c == '-') {
        // 前後がアルファベットの場合はハイフンをスキップ
        boolean prevIsAlpha = result.length() > 0 && Character.isLetter(result.charAt(result.length() - 1));
        boolean nextIsAlpha = i + 1 < input.length() && Character.isLetter(input.charAt(i + 1));
        if (prevIsAlpha || nextIsAlpha) {
          continue;
        }
      }
      result.append(c);
    }
    return result.toString();
  }
}
