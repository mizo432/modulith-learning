package com.github.mizo432.shared.value;

import java.time.LocalDate;

/**
 * {@link LocalDate} 型の値を表すインターフェースです。
 *
 * <p>このインターフェースは {@link Value} を拡張し、日付（年月日）を不変の値として扱うための 共通の契約を提供します。
 */
public interface DateValue extends Value<LocalDate> {}
