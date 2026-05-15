package undecided.shared.common.primitive;

import lombok.RequiredArgsConstructor;
import undecided.shared.functional.TwoFunction;

import java.util.function.Function;
import java.util.function.Predicate;

import static undecided.shared.common.primitive.Ints.CHECK_POSITIVE;
import static undecided.shared.common.primitive.Objects2.CHECK_NOT_NULL;
import static undecided.shared.common.primitive.Objects2.IS_NULL;

public class Strings2 {
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String UNDERSCORE = "_";
    public static final String DASH = "-";
    public static final String COLON = ":";
    public static final String SEMICOLON = ";";
    public static final String QUESTION_MARK = "?";
    public static final String AT = "@";
    public static final String PERIOD = ".";
    public static final String AND = "&";
    public static final String AMPERSAND = "&";
    public static final String PLUS = "+";
    public static final String EMPTY_STRING = "";
    public static final Surround SURROUND = new Surround();
    public static final IsEmpty IS_EMPTY = new IsEmpty();

    /**
     * 文字列が空かどうかを判定するための {@link Predicate} 実装です。
     *
     * <p>このクラスは、与えられた文字列が空文字列または {@code null} である場合に真を返します。 それ以外の場合は偽を返します。
     *
     * <p>主に文字列に対する空チェックを簡潔かつ明示的に行う用途で使用されます。
     */
    public static class IsEmpty implements Predicate<String> {
        /**
         * 指定された文字列が空であるか、または {@code null} であるかを判定します。
         *
         * @param t 判定対象の文字列。{@code null} の場合は空とみなされます。
         * @return 文字列が空または {@code null} の場合は {@code true}、それ以外の場合は {@code false} を返します。
         */
        @Override
        public boolean test(String t) {
            if (t == null) {
                return true;
            }
            return t.isEmpty();
        }
    }

    /**
     * Surround クラスは文字列を指定された開始および終了の文字列で囲む機能を提供します。 このクラスは {@link TwoFunction}
     * を実装し、2つの文字列を受け取って1つの文字列を返します。
     *
     * <p>このクラスの主目的は以下の通りです: - 入力文字列が null の場合でも処理が可能です。 - 囲む文字列が null の場合は空文字列を使用して囲むように扱います。
     *
     * <p>処理の流れ: 1. 囲む文字列が {@code null} の場合は空文字列に置き換えます。 2. 入力文字列が {@code null} の場合は空文字列に置き換えます。 3.
     * 囲む文字列で入力文字列を前後に付加して結合し、結果を返します。
     *
     * <p>例えば、"hello" という文字列と "*" を指定した場合、結果は "*hello*" になります。
     */
    public static class Surround implements TwoFunction<String, String, String> {
        @Override
        public String apply(String str, String surroundString) {
            if (IS_NULL.test(surroundString)) {
                surroundString = EMPTY;
            }
            if (IS_NULL.test(str)) {
                str = EMPTY;
            }

            return surroundString + str + surroundString;
        }
    }

    /**
     * Repeat クラスは、指定された文字列を指定された回数だけ繰り返して結合した文字列を生成する機能を提供します。
     *
     * <p>このクラスは {@link Function} インターフェースを実装しており、入力文字列に対して繰り返し操作を実行します。
     *
     * <p>主な機能: - 入力文字列を指定回数繰り返し、その結果を1つの文字列として結合します。 - コンストラクタで指定された繰り返し回数が正の整数であることを強制します。 - 入力文字列が
     * null でないことを保証します。
     *
     * <p>使用制限: - 繰り返し回数は正の整数である必要があります。それ以外の場合は例外をスローします。 - 入力文字列が null の場合も例外をスローします。
     *
     * <p>例外の取り扱い: - 繰り返し回数が正の整数でない場合は {@link IllegalArgumentException} がスローされます。 - 入力文字列が null の場合は
     * {@link IllegalArgumentException} がスローされます。
     */
    @RequiredArgsConstructor
    public class Repeat implements Function<String, String> {
        private final int times;

        @Override
        public String apply(String s) {
            CHECK_POSITIVE.apply(times, () -> new IllegalArgumentException("times must be positive"));
            CHECK_NOT_NULL.apply(s, () -> new IllegalArgumentException("s must not be null"));
            return String.join("", java.util.Collections.nCopies(times, s));
        }
    }

    public interface IndexOf {
        int apply(String s, String subString);
    }

    public static class IndexOfImpl implements IndexOf {

        @Override
        public int apply(String s, String subString) {
            if (IS_NULL.test(s)) {
                return -1;
            }
            return s.indexOf(subString);
        }
    }
}
