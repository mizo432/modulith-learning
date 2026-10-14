package undecided.shared.entity;

import java.util.UUID;
import undecided.shared.value.SingleValue;

public interface UuidValue<VO extends UuidValue<VO>> extends SingleValue<UUID> {}
