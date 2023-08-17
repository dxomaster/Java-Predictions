package engine.world.utils;


import Exception.WARN.WarnException;

import java.util.Random;

public class Property implements java.io.Serializable {
    private boolean isRandomlyGenerated;
    private String name;
    private Range range;
    private PropertyType type;
    private Object value;

    public Property(String name, PropertyType type, Range range, Object value) throws WarnException {
        this.type = type;
        this.name = name;
        this.isRandomlyGenerated = false;
        this.setRange(range);
        this.setValue(value);

    }

    public Property(String name, PropertyType type, Range range) {
        try {
            Random random = new Random();
            this.name = name;
            this.type = type;
            this.isRandomlyGenerated = true;
            this.setRange(range);

            if (range != null) {
                if (type == PropertyType.DECIMAL) {
                    this.setValue((Integer) random.nextInt((Integer) range.getTo() - (Integer) range.getFrom()) + (Integer) range.getFrom());
                } else {
                    this.setValue(random.nextFloat() * ((Float) range.getTo() - (Float) range.getFrom()) + (Float) range.getFrom());
                }
            } else {
                if (this.type == PropertyType.DECIMAL) {
                    this.setValue(random.nextInt());
                } else if (this.type == PropertyType.FLOAT) {
                    this.setValue(random.nextFloat());
                } else if (this.type == PropertyType.BOOLEAN) {
                    this.setValue(random.nextBoolean());
                } else if (this.type == PropertyType.STRING) {
                    this.setValue(generateRandomString());
                }
            }
        } catch (WarnException ignored) {

        }
    }

    public Property(Property property) {
        this.name = property.name;
        this.type = property.type;
        this.isRandomlyGenerated = property.isRandomlyGenerated;
        this.range = property.range;
        this.value = property.value;

    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String rangeString = range != null ? range.toString() : "None";
        return "Property Name: " + name +
                ", Type: " + type.propertyClass.getSimpleName() +
                ", Range: " + rangeString +
                ", Random initialization: " + isRandomlyGenerated;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) throws WarnException {
        if (value.getClass() != this.type.propertyClass) {
            throw new IllegalArgumentException("Error with Property " + this.name + " Value must be of type " + this.type.propertyClass.getSimpleName());
        }
        if (this.type == PropertyType.DECIMAL || this.type == PropertyType.FLOAT) {
            if (range != null && !range.isInRange(value)) {
                throw new WarnException("Error with Property " + this.name + " Value must be in range " + range.getFrom() + " to " + range.getTo());
            }


        }
        this.value = value;
    }

    private String generateRandomString() {
        Random random = new Random();
        int length = random.nextInt(50); // Set the desired length of the random string
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?,_-.() ;";
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }

        return randomString.toString();
    }

    public PropertyType getType() {
        return type;
    }

    public Range getRange() {
        return range;
    }

    private void setRange(Range range) {
        if (range == null) {
            return;
        } else {
            if (this.type == PropertyType.STRING) {
                throw new IllegalArgumentException("String properties cannot have a range");
            }
            if (this.type == PropertyType.BOOLEAN) {
                throw new IllegalArgumentException("Boolean properties cannot have a range");
            }
        }
        this.range = range;
    }
}
