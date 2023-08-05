package engine.factory;

import engine.jaxb.schema.generated.PRDRange;
import engine.world.utils.PropertyType;
import engine.world.utils.Range;

public class RangeFactory {
    public static Range createRange(PRDRange prdRange, PropertyType type) {
        if (type  == PropertyType.DECIMAL)
        {
            Integer from = new Integer((int)prdRange.getFrom());
            Integer to = new Integer((int)prdRange.getTo());
            return new Range(from, to, type);
        }
        else {
            Float from = new Float(prdRange.getFrom());
            Float to = new Float(prdRange.getTo());
            return new Range(from, to, type);
        }

    }
}
