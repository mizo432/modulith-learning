package undecided.notification.internal;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import undecided.notification.spi.NotificationEntity;
import undecided.supporting.snowflake.SnowflakeId;

/** 通知レコードのリポジトリ（NTスキーマ）。 */
interface NotificationRepository extends CrudRepository<NotificationEntity, SnowflakeId> {

  /** 指定された受信者の未読通知を、作成日時の降順で取得する。 */
  List<NotificationEntity> findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(SnowflakeId recipientId);

  /** 指定された受信者の通知が存在するかを確認する。 */
  boolean existsByRecipientId(SnowflakeId recipientId);
}
