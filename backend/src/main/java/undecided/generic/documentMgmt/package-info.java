/**
 * 帳票管理モジュール。
 *
 * <p>ファイル実体を MinIO（S3互換）に保存し、メタデータとバージョンを管理する。バックログなど他領域との紐付けは、利用側の領域が関連付けテーブルで持つ。
 */
@ApplicationModule(type = Type.OPEN)
package undecided.generic.documentMgmt;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.ApplicationModule.Type;
