package engine.factory;

import engine.jaxb.schema.generated.PRDRange;
import engine.world.utils.Range;

public class RangeFactory {
    public static Range createRange(PRDRange prdRange) {
        return new Range(prdRange.getFrom(), prdRange.getTo());
    }
}
