package pl.allegro.atl.adapters.description;

public class Description {
    private final String description;

    public Description(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Description{" +
                "description='" + description + '\'' +
                '}';
    }
}
