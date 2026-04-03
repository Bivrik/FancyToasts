package net.bivrik.fancytoasts.client.gui;

public enum WidgetWidthType {
    BIG(4, 310),
    MEDIUM(2, 150),
    SMALL(1, 70);

    private final int weight;
    private final int width;

    WidgetWidthType(int weight, int width) {
        this.weight = weight;
        this.width = width;
    }

    public int getWeight() {
        return weight;
    }

    public int getWidth() {
        return width;
    }
}
