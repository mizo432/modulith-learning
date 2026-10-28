package com.github.mizo432.shared.value;

import java.time.LocalDateTime;

/**
 * {@link LocalDateTime} 型の値を表すインターフェースです。
 *
 * <p>このインターフェースは {@link Value} を拡張し、日付と時刻を不変の値として扱うための
 * 共通の契約を提供します。イベントの発生日時や更新日時などを表す値オブジェクトを実装する際に
 * 使用されます。
 *
 * @see Value
 * @see DateValue
 */
public interface DateTimeValue extends Value<LocalDateTime> {}
