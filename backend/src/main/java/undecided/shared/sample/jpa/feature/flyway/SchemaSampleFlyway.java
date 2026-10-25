package undecided.shared.sample.jpa.feature.flyway;

/**
 * Flyway・DDL連携サンプルで使用されるスキーマ名を定義するインターフェースです。
 *
 * <p>このサンプルは、FlywayによるDDL管理とJPAエンティティの連携パターンを示します。
 */
public interface SchemaSampleFlyway {
  /** undecided_efスキーマの名前 */
  String NAME = "undecided_ef";
}
