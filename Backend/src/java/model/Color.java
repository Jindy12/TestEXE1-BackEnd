package model;

/**
 *
 * @author tuana
 */
public class Color {

    private int colorId;
    private String productId;
    private String colorName;

    public Color() {
    }

    public Color(int colorId, String productId, String colorName) {
        this.colorId = colorId;
        this.productId = productId;
        this.colorName = colorName;
    }

    public Color(int colorId, String colorName) {
        this.colorId = colorId;
        this.colorName = colorName;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

}
