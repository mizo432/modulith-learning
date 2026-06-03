package undecided.shared.common.primitive;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;


public class Lists2 {
    private Lists2() {
        
    }

    public static final Supplier<List<T>> NEW_ARRAYLIST = ArrayList::new;

    public static final Function<T, List<T>> TO_LIST = List::of;

    public static final Function<List<? extends T>, List<T>> UNMODIFIABLE_LIST = Collections::unmodifiableList;
}
