package undecided.erp.addressMgmt.infra.dao.machiAza;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import undecided.erp.addressMgmt.model.machiAza.MachiAzaStatus;

@Converter
public class MachiAzaStatusType implements AttributeConverter<MachiAzaStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(MachiAzaStatus machiAzaStatus) {
    return machiAzaStatus.getId();
  }

  @Override
  public MachiAzaStatus convertToEntityAttribute(Integer id) {
    return MachiAzaStatus.valueOfId(id);
  }
}
