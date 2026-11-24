package undecided.generic.documentMgmt.spi;

import java.io.InputStream;
import undecided.supporting.snowflake.SnowflakeId;

/** 帳票のファイル実体を保存するストレージの操作。 */
public interface DocumentStorageService {

  /**
   * ファイルを保存する。
   *
   * @param documentId 帳票ID
   * @param version 帳票のバージョン
   * @param fileName ファイル名
   * @param contentType Content-Type
   * @param content ファイルの内容
   * @param contentLength ファイルのバイト数
   * @return 保存先のキー（{@code {documentId}/v{version}/{fileName}}）
   */
  String upload(
      SnowflakeId documentId,
      DocumentVersion version,
      String fileName,
      String contentType,
      InputStream content,
      long contentLength);

  /**
   * ファイルを取得する。
   *
   * @param key 保存先のキー
   * @return ファイルの内容
   */
  InputStream download(String key);

  /**
   * ファイルを物理削除する。
   *
   * @param key 保存先のキー
   */
  void delete(String key);
}
