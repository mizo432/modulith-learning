package undecided.generic.documentMgmt.spi;

import java.io.InputStream;
import org.jspecify.annotations.NonNull;

/**
 * 帳票のダウンロード内容。
 *
 * @param content ファイル内容のストリーム
 * @param contentType Content-Type（MIME タイプ）
 * @param fileName ファイル名
 * @param fileSize ファイルサイズ（バイト数）
 */
public record DocumentContent(
    @NonNull InputStream content,
    @NonNull String contentType,
    @NonNull String fileName,
    long fileSize) {}
