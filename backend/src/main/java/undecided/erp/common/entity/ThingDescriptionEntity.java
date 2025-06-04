package undecided.erp.common.entity;

public abstract class ThingDescriptionEntity<
        D extends ThingDescriptionEntity<D, T>, T extends ThingEntity<T>>
    extends DescriptionEntity<D, T> {}
