package com.github.mizo432.shared.value;

/**
 * 文字列型のコード値オブジェクトを表すインターフェースです。
 *
 * <p>このインターフェースは {@link StringValue} を拡張し、業務上のコード（例: 国コード、通貨コード、
 * 状態コードなど）を不変の値として扱うための共通の契約を提供します。
 *
 * <p>コード値オブジェクトは通常、特定のドメイン内で一意な識別子として使用され、
 * バリデーションや変換ロジックを伴うことが多くあります。
 *
 * @param <VO> このインターフェースを実装する具体的な値オブジェクトの型
 * @see StringValue
 */
public interface StringCode<VO extends StringCode<VO>> extends StringValue<VO> {}
