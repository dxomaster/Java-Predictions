package DTO;

import org.w3c.dom.ranges.Range;

public class PropertyDTO {
    private RangeDTO range;
    private String name;
    private String type;

    public RangeDTO getRange() {
        return range;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    private String value;

    public PropertyDTO(RangeDTO range, String name, String type, String value) {
        this.range = range;
        this.name = name;
        this.type = type;
        this.value = value;
    }
}
