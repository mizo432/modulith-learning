package undecided.erp.addressMgmt.adapters.dao.machiAza;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import undecided.erp.addressMgmt.model.machiAza.MachiAzaType;

@Converter
public class MachiAzaTypeType implements AttributeConverter<MachiAzaType, String> {

  @Override
  public String convertToDatabaseColumn(MachiAzaType machiAzaStatus) {
    return machiAzaStatus.getCode();
  }

  @Override
  public MachiAzaType convertToEntityAttribute(String id) {
    return MachiAzaType.valueOfCode(id);
  }
}
