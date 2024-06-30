package undecided.erp.relationship.domain.model.party.addressUse;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.shared.date.ApplicableDate;
import undecided.erp.shared.entity.SnowflakeId;

public interface AddressUseHistoryRepository extends
    CrudRepository<AddressUse, SnowflakeId<AddressUse>> {

  /**
   * パーティーIDにより、AddressUseオブジェクトのリストを選択します。
   *
   * @param partyId パーティーのID
   * @return 与えられたパーティーIDに関連付けられたAddressUseオブジェクトのリスト
   */
  List<AddressUse> findByPartyId(SnowflakeId<Party> partyId);

  /**
   * partyIdとtargetDateに基づいてAddressUseオブジェクトを検索します。
   *
   * @param partyId パーティーのID。
   * @param targetDate 検索対象の日付。
   * @return 与えられたpartyIdとtargetDateに関連するAddressUseオブジェクトを含む場合にはOptional<AddressUse>オブジェクトを返します。該当するAddressUseオブジェクトが見つからない場合はOptional.empty()を返します。
   */
  default Optional<AddressUse> findByPartyIdAndTargetDate(SnowflakeId<Party> partyId,
      ApplicableDate targetDate) {
    return findByPartyIdAndInterval_BeginDateLessThanEqualAndInterval_EndDateGreaterThanEqual(
        partyId, targetDate, targetDate);
  }

  /**
   * パーティID、Interval開始日が指定始日以上、およびInterval終了日が指定終了日以下のOptional<AddressUse>を検索します。
   *
   * @param partyId パーティのID。
   * @param beginDate インターバルの開始日。
   * @param endDate インターバルの終了日。
   * @return 指定されたパーティIDとインターバルに関連するOptional<AddressUse>オブジェクト、それが存在しない場合はOptional.empty()を返します。
   */
  Optional<AddressUse> findByPartyIdAndInterval_BeginDateLessThanEqualAndInterval_EndDateGreaterThanEqual(
      SnowflakeId<Party> partyId, ApplicableDate beginDate,
      ApplicableDate endDate);

}
