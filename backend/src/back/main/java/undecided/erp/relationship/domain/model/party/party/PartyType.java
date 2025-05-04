package undecided.erp.relationship.domain.model.party.party;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import undecided.erp.shared.entity.EnumConverter;
import undecided.erp.shared.entity.PersistableEnum;

/**
 * Partyクラスは、組織または個人となるパーティを表します。
 */
public enum PartyType implements PersistableEnum {
  /**
   * 個人
   */
  PERSON("1"),
  /** 組織 */
  ORGANIZATION("2"),
  /** 組織部門 */
  ORGANIZATION_UNIT("2"), UNKNOWN("");

  private final String code;

  PartyType(String code) {
    this.code = code;
  }

  private static PartyType valueOfDatabaseValue(String value) {
    return PartyType.valueOfCode(value);
  }

  private static PartyType valueOfCode(String value) {
    for (PartyType partyType : values()) {
      if (partyType.code.equals(value)) {
        return partyType;
      }
    }
    return PartyType.UNKNOWN;
  }

  public boolean isOrganizationUnit() {
    return this == ORGANIZATION_UNIT;
  }

  @Override
  public String getDatabaseValue() {
    return code;
  }

  @Converter(autoApply = true)
  public static class PartyTypeConverter extends EnumConverter<PartyType> implements
      AttributeConverter<PartyType, String> {

    @Override
    protected PartyType toEntityAttribute(Class<PartyType> enumClass, String value) {
      return PartyType.valueOfDatabaseValue(value);

    }

    @Override
    public String convertToDatabaseColumn(PartyType partyType) {
      return partyType.code;
    }

    @Override
    public PartyType convertToEntityAttribute(String code) {
      return PartyType.valueOfCode(code);
    }
  }
}
