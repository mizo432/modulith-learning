package undecided.generic.documentMgmt.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 帳票ストレージ（MinIO / S3互換）への接続設定。
 *
 * @param endpoint S3 互換 API のエンドポイント
 * @param region リージョン
 * @param accessKey アクセスキー
 * @param secretKey シークレットキー
 * @param bucket 帳票ファイルを保存するバケット名
 */
@ConfigurationProperties("document-storage")
record DocumentStorageProperties(
    String endpoint, String region, String accessKey, String secretKey, String bucket) {}
