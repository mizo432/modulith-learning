package undecided.erp.addressMgmt.infra.dao.machiAza;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import undecided.erp.addressMgmt.model.machiAza.MachiAzaStatus;
import undecided.erp.addressMgmt.model.machiAza.MachiAzaType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "machi_azas", schema = "address_info")
public class MachiAzaRecord {

  @Id
  @Column(name = "machi_aza_id", nullable = false)
  private Long id;

  @Column(name = "city_id", nullable = false)
  private Long cityId;
  @Column(name = "lg_code", nullable = false)
  private String lgCode;
  @Column(name = "machiaza_code", nullable = false)
  private String machiazaCode;
  @Column(name = "machiaza_type")
  @Enumerated(EnumType.ORDINAL)
  private MachiAzaType type;
  @Column(name = "oaza_cho_name")
  private String oazaChoName;
  @Column(name = "oaza_cho_kana")
  private String oazaChoKana;
  @Column(name = "oaza_cho_roma")
  private String oazaChoRoma;
  @Column(name = "chome_name")
  private String chomeName;
  @Column(name = "chome_kana")
  private String chomeKana;
  @Column(name = "chome_number")
  private String chomeNumber;
  @Column(name = "koaza_name")
  private String koazaName;
  @Column(name = "koaza_kana")
  private String koazaKana;
  @Column(name = "koaza_roma")
  private String koazaRoma;
  @Column(name = "machiaza_dist")
  private String machiazaDist;
  @Column(name = "rsdt_addr_flg")
  private Boolean rsdtAddrFlg;
  @Column(name = "rsdt_addr_mtd_code")
  private String rsdtAddrMtdCode;
  @Column(name = "oaza_cho_aka_flg")
  private Boolean oazaChoAkaFlg;
  @Column(name = "koaza_aka_code")
  private String koazaAkaCode;
  @Column(name = "oaza_cho_gsi_uncmn")
  private String oazaChoGsiUncmn;
  @Column(name = "koaza_gsi_uncmn")
  private String koazaGsiUncmn;
  @Column(name = "status_flg")
  @Enumerated(EnumType.ORDINAL)
  private MachiAzaStatus status;
  @Column(name = "wake_num_flg")
  private Boolean wakeNumFlg;
  @Column(name = "src_code")
  private String srcCode;
  private LocalDate efctDate;
  private LocalDate abltDate;
  private String remarks;


}