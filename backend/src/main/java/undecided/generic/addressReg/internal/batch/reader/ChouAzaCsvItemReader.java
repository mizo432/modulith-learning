package undecided.generic.addressReg.internal.batch.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.NonTransientResourceException;
import org.springframework.batch.infrastructure.item.ParseException;
import org.springframework.batch.infrastructure.item.UnexpectedInputException;
import org.springframework.core.io.Resource;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import undecided.generic.addressReg.internal.batch.dto.ChouAzaCsvDto;

/** Super CSV を使用してデジタル庁の町字マスター CSV を読み込む {@link ItemStreamReader} 実装。 */
public class ChouAzaCsvItemReader implements ItemStreamReader<ChouAzaCsvDto> {

  private static final String[] DEFAULT_NAME_MAPPING =
      new String[] {
        "lgCode",
        "machiazaCode",
        "machiazaType",
        "oazaChoName",
        "oazaChoKana",
        "oazaChoRoma",
        "chomeName",
        "chomeKana",
        "chomeNumber",
        "koazaName",
        "koazaKana",
        "koazaRoma",
        "machiazaDist",
        "rsdtAddrFlg",
        "rsdtAddrMtdCode",
        "oazaChoAkaFlg",
        "koazaAkaCode",
        "oazaChoGsiUncmn",
        "koazaGsiUncmn",
        "status",
        "wakeNumFlg",
        "srcCode",
        "effectiveDate",
        "abolitionDate",
        "remarks"
      };

  private static final String HEADER_LG_CODE_JP = "全国地方公共団体コード";
  private static final String HEADER_MACHIAZA_CODE_JP = "町字id";
  private static final String HEADER_MACHIAZA_CODE_ALT_JP = "町字コード";
  private static final String HEADER_MACHIAZA_TYPE_JP = "町字区分コード";
  private static final String HEADER_OAZA_CHO_NAME_JP = "大字・町名";
  private static final String HEADER_OAZA_CHO_KANA_JP = "大字・町名_カナ";
  private static final String HEADER_OAZA_CHO_ROMA_JP = "大字・町名_英字";
  private static final String HEADER_CHOME_NAME_JP = "丁目名";
  private static final String HEADER_CHOME_KANA_JP = "丁目名_カナ";
  private static final String HEADER_CHOME_NUMBER_JP = "丁目名_数字";
  private static final String HEADER_KOAZA_NAME_JP = "小字名";
  private static final String HEADER_KOAZA_KANA_JP = "小字名_カナ";
  private static final String HEADER_KOAZA_ROMA_JP = "小字名_英字";
  private static final String HEADER_MACHIAZA_DIST_JP = "同名町字識別コード";
  private static final String HEADER_RSDT_ADDR_FLG_JP = "住居表示フラグ";
  private static final String HEADER_RSDT_ADDR_MTD_CODE_JP = "住居表示方式コード";
  private static final String HEADER_OAZA_CHO_AKA_FLG_JP = "大字・町名_通称フラグ";
  private static final String HEADER_KOAZA_AKA_CODE_JP = "小字_通称フラグ";
  private static final String HEADER_KOAZA_AKA_CODE_ALT_JP = "小字_通称コード";
  private static final String HEADER_OAZA_CHO_GSI_UNCMN_JP = "大字・町名_電子国土基本図外字";
  private static final String HEADER_KOAZA_GSI_UNCMN_JP = "小字_電子国土基本図外字";
  private static final String HEADER_STATUS_JP = "状態フラグ";
  private static final String HEADER_WAKE_NUM_FLG_JP = "起番フラグ";
  private static final String HEADER_SRC_CODE_JP = "原典資料コード";
  private static final String HEADER_EFFECTIVE_DATE_JP = "効力発生日";
  private static final String HEADER_ABOLITION_DATE_JP = "廃止日";
  private static final String HEADER_REMARKS_JP = "備考";

  @Setter private Resource resource;

  private ICsvBeanReader csvBeanReader;
  private String[] nameMapping;

  public ChouAzaCsvItemReader() {}

  public ChouAzaCsvItemReader(Resource resource) {
    this.resource = resource;
  }

  @Override
  public void open(@NonNull ExecutionContext executionContext) throws ItemStreamException {
    if (resource == null) {
      throw new ItemStreamException("Resource must not be null");
    }
    try {
      BufferedReader reader =
          new BufferedReader(
              new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
      csvBeanReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE);

      String[] headers = csvBeanReader.getHeader(true);
      if (headers == null || headers.length == 0) {
        nameMapping = DEFAULT_NAME_MAPPING;
      } else {
        nameMapping = resolveNameMapping(headers);
      }
    } catch (IOException e) {
      throw new ItemStreamException("Failed to open CSV resource: " + resource.getDescription(), e);
    }
  }

  private String[] resolveNameMapping(String[] headers) {
    String[] mapping = new String[headers.length];
    for (int i = 0; i < headers.length; i++) {
      String h = headers[i] != null ? headers[i].trim() : "";
      if (HEADER_LG_CODE_JP.equals(h)
          || "lgCode".equalsIgnoreCase(h)
          || "lg_code".equalsIgnoreCase(h)) {
        mapping[i] = "lgCode";
      } else if (HEADER_MACHIAZA_CODE_JP.equals(h)
          || HEADER_MACHIAZA_CODE_ALT_JP.equals(h)
          || "machiazaCode".equalsIgnoreCase(h)
          || "machiaza_code".equalsIgnoreCase(h)
          || "machiazaId".equalsIgnoreCase(h)
          || "machiaza_id".equalsIgnoreCase(h)
          || "townId".equalsIgnoreCase(h)
          || "town_id".equalsIgnoreCase(h)) {
        mapping[i] = "machiazaCode";
      } else if (HEADER_MACHIAZA_TYPE_JP.equals(h)
          || "machiazaType".equalsIgnoreCase(h)
          || "machiaza_type".equalsIgnoreCase(h)) {
        mapping[i] = "machiazaType";
      } else if (HEADER_OAZA_CHO_NAME_JP.equals(h)
          || "oazaChoName".equalsIgnoreCase(h)
          || "oaza_cho_name".equalsIgnoreCase(h)) {
        mapping[i] = "oazaChoName";
      } else if (HEADER_OAZA_CHO_KANA_JP.equals(h)
          || "oazaChoKana".equalsIgnoreCase(h)
          || "oaza_cho_kana".equalsIgnoreCase(h)) {
        mapping[i] = "oazaChoKana";
      } else if (HEADER_OAZA_CHO_ROMA_JP.equals(h)
          || "oazaChoRoma".equalsIgnoreCase(h)
          || "oaza_cho_roma".equalsIgnoreCase(h)) {
        mapping[i] = "oazaChoRoma";
      } else if (HEADER_CHOME_NAME_JP.equals(h)
          || "chomeName".equalsIgnoreCase(h)
          || "chome_name".equalsIgnoreCase(h)) {
        mapping[i] = "chomeName";
      } else if (HEADER_CHOME_KANA_JP.equals(h)
          || "chomeKana".equalsIgnoreCase(h)
          || "chome_kana".equalsIgnoreCase(h)) {
        mapping[i] = "chomeKana";
      } else if (HEADER_CHOME_NUMBER_JP.equals(h)
          || "chomeNumber".equalsIgnoreCase(h)
          || "chome_number".equalsIgnoreCase(h)) {
        mapping[i] = "chomeNumber";
      } else if (HEADER_KOAZA_NAME_JP.equals(h)
          || "koazaName".equalsIgnoreCase(h)
          || "koaza_name".equalsIgnoreCase(h)) {
        mapping[i] = "koazaName";
      } else if (HEADER_KOAZA_KANA_JP.equals(h)
          || "koazaKana".equalsIgnoreCase(h)
          || "koaza_kana".equalsIgnoreCase(h)) {
        mapping[i] = "koazaKana";
      } else if (HEADER_KOAZA_ROMA_JP.equals(h)
          || "koazaRoma".equalsIgnoreCase(h)
          || "koaza_roma".equalsIgnoreCase(h)) {
        mapping[i] = "koazaRoma";
      } else if (HEADER_MACHIAZA_DIST_JP.equals(h)
          || "machiazaDist".equalsIgnoreCase(h)
          || "machiaza_dist".equalsIgnoreCase(h)) {
        mapping[i] = "machiazaDist";
      } else if (HEADER_RSDT_ADDR_FLG_JP.equals(h)
          || "rsdtAddrFlg".equalsIgnoreCase(h)
          || "rsdt_addr_flg".equalsIgnoreCase(h)) {
        mapping[i] = "rsdtAddrFlg";
      } else if (HEADER_RSDT_ADDR_MTD_CODE_JP.equals(h)
          || "rsdtAddrMtdCode".equalsIgnoreCase(h)
          || "rsdt_addr_mtd_code".equalsIgnoreCase(h)) {
        mapping[i] = "rsdtAddrMtdCode";
      } else if (HEADER_OAZA_CHO_AKA_FLG_JP.equals(h)
          || "oazaChoAkaFlg".equalsIgnoreCase(h)
          || "oaza_cho_aka_flg".equalsIgnoreCase(h)) {
        mapping[i] = "oazaChoAkaFlg";
      } else if (HEADER_KOAZA_AKA_CODE_JP.equals(h)
          || HEADER_KOAZA_AKA_CODE_ALT_JP.equals(h)
          || "koazaAkaCode".equalsIgnoreCase(h)
          || "koaza_aka_code".equalsIgnoreCase(h)) {
        mapping[i] = "koazaAkaCode";
      } else if (HEADER_OAZA_CHO_GSI_UNCMN_JP.equals(h)
          || "oazaChoGsiUncmn".equalsIgnoreCase(h)
          || "oaza_cho_gsi_uncmn".equalsIgnoreCase(h)) {
        mapping[i] = "oazaChoGsiUncmn";
      } else if (HEADER_KOAZA_GSI_UNCMN_JP.equals(h)
          || "koazaGsiUncmn".equalsIgnoreCase(h)
          || "koaza_gsi_uncmn".equalsIgnoreCase(h)) {
        mapping[i] = "koazaGsiUncmn";
      } else if (HEADER_STATUS_JP.equals(h) || "status".equalsIgnoreCase(h)) {
        mapping[i] = "status";
      } else if (HEADER_WAKE_NUM_FLG_JP.equals(h)
          || "wakeNumFlg".equalsIgnoreCase(h)
          || "wake_num_flg".equalsIgnoreCase(h)) {
        mapping[i] = "wakeNumFlg";
      } else if (HEADER_SRC_CODE_JP.equals(h)
          || "srcCode".equalsIgnoreCase(h)
          || "src_code".equalsIgnoreCase(h)) {
        mapping[i] = "srcCode";
      } else if (HEADER_EFFECTIVE_DATE_JP.equals(h)
          || "effectiveDate".equalsIgnoreCase(h)
          || "effective_date".equalsIgnoreCase(h)) {
        mapping[i] = "effectiveDate";
      } else if (HEADER_ABOLITION_DATE_JP.equals(h)
          || "abolitionDate".equalsIgnoreCase(h)
          || "abolition_data".equalsIgnoreCase(h)
          || "abolition_date".equalsIgnoreCase(h)) {
        mapping[i] = "abolitionDate";
      } else if (HEADER_REMARKS_JP.equals(h) || "remarks".equalsIgnoreCase(h)) {
        mapping[i] = "remarks";
      } else {
        mapping[i] = null;
      }
    }
    return mapping;
  }

  @Override
  public @Nullable ChouAzaCsvDto read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (csvBeanReader == null) {
      throw new ItemStreamException("Reader is not open. Call open() first.");
    }
    return csvBeanReader.read(ChouAzaCsvDto.class, nameMapping);
  }

  @Override
  public void update(@NonNull ExecutionContext executionContext) throws ItemStreamException {
    // No-op
  }

  @Override
  public void close() throws ItemStreamException {
    if (csvBeanReader != null) {
      try {
        csvBeanReader.close();
      } catch (IOException e) {
        throw new ItemStreamException("Failed to close CSV reader", e);
      } finally {
        csvBeanReader = null;
      }
    }
  }
}
