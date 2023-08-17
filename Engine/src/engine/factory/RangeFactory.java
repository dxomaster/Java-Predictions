package engine.factory;

import engine.jaxb.schema.generated.PRDRange;
import engine.world.utils.PropertyType;
import engine.world.utils.Range;

public class RangeFactory {
    public static Range createRange(PRDRange prdRange, PropertyType type) {
        if (type == PropertyType.DECIMAL) {
            Integer from = (int) prdRange.getFrom();
            Integer to = (int) prdRange.getTo();
            return new Range(from, to, type);
        } else {
            Float from = (float) prdRange.getFrom();
            Float to = (float) prdRange.getTo();
            return new Range(from, to, type);
        }

    }
}
