package undecided.generic.calendarReg.internal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import undecided.generic.calendarReg.spi.Holiday;

/** 祝日エンティティに対するデータベースアクセスを提供するリポジトリインターフェース。 */
@Repository
public interface HolidayRepository extends CrudRepository<Holiday, Long> {

  /**
   * 祝日日付を指定して祝日エンティティを検索します。
   *
   * @param holidayDate 祝日日付
   * @return 該当する祝日エンティティの Optional
   */
  Optional<Holiday> findByHolidayDate(@NonNull LocalDate holidayDate);

  /**
   * 指定された期間内のすべての祝日を日付昇順で取得します。
   *
   * @param from 開始日
   * @param to 終了日
   * @return 祝日エンティティのリスト
   */
  @Query(
      "SELECT h FROM Holiday h WHERE h.holidayDate >= :from AND h.holidayDate <= :to ORDER BY"
          + " h.holidayDate ASC")
  List<Holiday> findBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
