package undecided.erp.relMgmt.infra.dao.partyTable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import undecided.erp.relMgmt.model.party.party.Party;
import undecided.erp.relMgmt.model.party.party.PartyType;

/**
 * PartyRecordは、システム内のパーティを表すクラスです。
 * <p>
 * これは、パーティデータをデータベーステーブルにマップするために使用されます。このクラスには、 パーティ情報を管理するためのフィールドとメソッドが含まれています。
 */
@Entity
@Table(schema = "relationship", name = "parties"
)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PartyRecord {

  @Id
  @Column(name = "party_id")
  private Long id;

  @Column(name = "party_type")
  @Enumerated(EnumType.STRING)
  private PartyType type;

  private String govtAssignedId;

  public Party toEntity() {
    return Party.reconstruct(id, type, govtAssignedId);
  }

  public static PartyRecord fromEntity(Party entity) {
    return new PartyRecord(entity.getId().getValue(), entity.getType(),
        entity.getAttribute().getGovtAssignedId().getValue());
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    PartyRecord that = (PartyRecord) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass().hashCode() : getClass().hashCode();
  }
}
