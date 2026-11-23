package undecided.generic.documentMgmt.internal;

import java.io.InputStream;

/**
 * 帳票ダウンロードの結果。
 *
 * @param content ファイルの内容
 * @param contentType Content-Type（MIME タイプ）
 * @param fileName ファイル名
 * @param fileSize ファイルサイズ（バイト数）
 */
record DocumentDownloadResult(
    InputStream content, String contentType, String fileName, long fileSize) {}
