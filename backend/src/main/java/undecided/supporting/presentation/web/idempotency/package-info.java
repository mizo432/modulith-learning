@NamedInterface
package undecided.supporting.presentation.web.idempotency;

import org.springframework.modulith.NamedInterface;

/**
 * 冪等性キーを処理するためのプレゼンテーション層の共通機能を提供するパッケージです。
 *
 * <p>POSTおよびPATCHメソッドのリクエストで、クライアントから送信された冪等性キー（Idempotency-Key）を管理し、 重複リクエストを検出して適切なレスポンスを返却します。
 */
