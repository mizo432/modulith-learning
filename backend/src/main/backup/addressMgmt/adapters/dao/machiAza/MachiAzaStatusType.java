package undecided.erp.addressMgmt.adapters.dao.machiAza;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import undecided.erp.addressMgmt.domain.model.machiAza.MachiAzaStatus;

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
