package engine.world.utils;


import java.util.Random;

public class Property<T> {
    private final boolean isRandomlyGenerated;
    private final String name;
    private final Range<T> range;
    private T value;

    public Property(String name, Range<T> range, T value) {
        this.name = name;
        this.range = range;
        this.setValue(value);
        this.isRandomlyGenerated = false;
    }

    public Property(String name, Range<T> range) {
        Random random = new Random();
        this.name = name;
        this.range = range;
        if (range != null)
            this.setValue(range.generateRandomValueInRange());
        if (value.getClass() == Integer.class) {
            this.setValue((T) Integer.valueOf(random.nextInt()));
        } else if (value.getClass() == Float.class) {
            this.setValue((T) Float.valueOf(random.nextFloat()));
        } else if (value.getClass() == String.class) {
            this.setValue((T) generateRandomString());
        } else {
            this.setValue((T) Boolean.valueOf(random.nextBoolean()));
        }
        this.isRandomlyGenerated = true;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Property{" +
                ", name='" + name + '\'' +
                ", type=" + value.getClass().getSimpleName() +
                ", range=" + range +
                "isRandomlyGenerated=" + isRandomlyGenerated +
                '}';
    }

    public void setValue(T value) {
        if (value.getClass() == Integer.class || value.getClass() == Float.class) {
            if (range != null && !range.isInRange(value)) {
                throw new IllegalArgumentException("Value must be in range " + range.getFrom() + " to " + range.getTo());
            }

        }
        this.value = value;
    }

    public T getValue() {
        return value;
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

}
