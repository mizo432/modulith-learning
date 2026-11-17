package com.github.mizo432.shared.sample.jpa.feature.schema;

/**
 * Schema分離パターンで使用されるスキーマ名を定義するインターフェースです。
 *
 * <p>各モジュールや機能領域ごとにスキーマを分離することで、 データの論理的なグループ化とアクセス制御を実現します。
 */
public interface SchemaSampleEf {
  /** undecided_efスキーマの名前 */
  String NAME = "undecided_ef";
}
