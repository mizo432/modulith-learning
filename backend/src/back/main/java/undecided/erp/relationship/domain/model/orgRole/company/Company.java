package undecided.erp.relationship.domain.model.orgRole.company;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import undecided.erp.relationship.domain.model.orgRole.ICompany;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * Represents a company entity with an identifier and a company code.
 * <p>
 * The class implements the ICompany interface, which extends the OrgRole interface, indicating that
 * Company is a type of organizational role in the system.
 */
@Table(name = "companies")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Company implements ICompany {

  @Id
  @Column(name = "company_id", length = 19, nullable = false)
  @Convert(converter = SnowflakeIdConverter.class)
  private SnowflakeId id;

  @Column(name = "company_code", nullable = false)
  @Convert(converter = CompanyCodeConverter.class)
  private CompanyCode code;

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
    Company company = (Company) o;
    return getId() != null && Objects.equals(getId(), company.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass().hashCode() : getClass().hashCode();
  }
}
