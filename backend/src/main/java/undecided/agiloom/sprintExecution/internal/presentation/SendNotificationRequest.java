package undecided.agiloom.sprintExecution.internal.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
import undecided.supporting.notification.spi.NotificationType;

/**
 * 通知送信のリクエストDTOです。
 * <p>
 * UC-SP-013 通知を送信する に基づき、通知先、通知内容、通知種類を入力します。
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {

    /**
     * 通知の受信者ID（必須）
     */
    @NotNull(message = "通知先は必須です")
    private UUID recipientId;

    /**
     * 通知の送信元となるリソースのID（任意）
     */
    private UUID sourceId;

    /**
     * 通知のタイトル（必須）
     */
    @NotBlank(message = "通知タイトルは必須です")
    @Size(max = 200, message = "通知タイトルは200文字以内で入力してください")
    private String title;

    /**
     * 通知の本文（必須）
     */
    @NotBlank(message = "通知内容は必須です")
    @Size(max = 2000, message = "通知内容は2000文字以内で入力してください")
    private String content;

    /**
     * 通知の種類（必須）
     */
    @NotNull(message = "通知種類は必須です")
    private NotificationType type;
}
