package com.github.mizo432.shared.value;

/**
 * 文字列型の値オブジェクトを表すインターフェースです。
 *
 * <p>このインターフェースは {@link SingleValue} を拡張し、文字列（{@code String}）を不変の値として
 * 扱うための共通の契約を提供します。文字列ベースの値オブジェクト（例: 名前、コード、説明など）を
 * 実装する際に使用されます。
 *
 * <p>ジェネリックパラメータ {@code VO} を使用することで、実装クラス自身を返す型安全的な
 * フラuentインターフェースを構築できます。
 *
 * @param <VO> このインターフェースを実装する具体的な値オブジェクトの型
 * @see SingleValue
 * @see StringCode
 */
public interface StringValue<VO extends StringValue<VO>> extends SingleValue<String> {}
