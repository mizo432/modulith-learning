package undecided.erp.relationship.adapters.dao.party;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.party.party.PartyType;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name = "parties")
@NoArgsConstructor
public class PartyTable {

  @Id
  @Column(name = "party_id", length = 19, nullable = false)
  private Long id;

  @Column(name = "party_type", length = 1, nullable = false)
  private PartyType type;

  @Column(name = "simple_name", length = 50, nullable = false)
  private String simpleName;

  public static List<Party> toEntities(List<PartyTable> recs) {
    return recs
        .stream()
        .map((rec) -> Party.reconstruct(rec.getId(), rec.getType(), rec.getSimpleName()))
        .toList();
  }

  public static List<PartyTable> toTableRecs(List<Party> entities) {
    return entities
        .stream()
        .map((entity) -> new PartyTable(entity.getId().getValue(), entity.getType(),
            entity.getSimpleName().getValue()))
        .toList();


  }

  public static PartyTable toTableRec(Party entity) {
    return new PartyTable(
        entity.getId().getValue(),
        entity.getType(),
        entity.getSimpleName().getValue());

  }

  public Party toEntity() {
    return Party.reconstruct(id, type, simpleName);
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
    PartyTable that = (PartyTable) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass().hashCode() : getClass().hashCode();
  }
}
