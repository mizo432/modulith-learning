package undecided.authorization.domain.model.user;

/**
 * ユーザータイプを表す列挙型
 * <p>
 * システムにおける3種類のユーザータイプを定義します：
 * <ul>
 *   <li>EMPLOYEE: 社員</li>
 *   <li>BUSINESS_PARTNER_EMPLOYEE: ビジネスパートナー社員</li>
 *   <li>INDIVIDUAL_BUSINESS_PARTNER: ビジネスパートナー（個人）</li>
 * </ul>
 */
public enum UserType {
  /**
   * 社員
   */
  EMPLOYEE,

  /**
   * ビジネスパートナー社員
   */
  BUSINESS_PARTNER_EMPLOYEE,

  /**
   * ビジネスパートナー（個人）
   */
  INDIVIDUAL_BUSINESS_PARTNER
}
