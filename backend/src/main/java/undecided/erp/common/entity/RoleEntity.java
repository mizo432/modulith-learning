package undecided.erp.common.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Immutable;

@MappedSuperclass
@Getter
@Setter
@Immutable
public abstract class RoleEntity<R extends RoleEntity<R>> extends BusinessEntity<R> {

}
