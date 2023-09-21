package DTO;

public class PropertyDTO {
    private final RangeDTO range;
    private final String name;
    private final String type;

    public boolean isRandomlyGenerated() {
        return isRandomlyGenerated;
    }

    private final boolean isRandomlyGenerated;

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

    public PropertyDTO(RangeDTO range, String name, String type, String value, boolean isRandomlyGenerated) {
        this.range = range;
        this.name = name;
        this.type = type;
        this.value = value;
        this.isRandomlyGenerated = isRandomlyGenerated;
    }

    public void setValue(String newPropertyValue) {
        this.value = newPropertyValue;
    }

}
