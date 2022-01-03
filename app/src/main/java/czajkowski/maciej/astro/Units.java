package czajkowski.maciej.astro;

public enum Units {
    IMPERIAL("imperial"), STANDARD("standard"), METRIC("metric");

    private final String desc;

    private Units(String value) {
        this.desc = value;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}