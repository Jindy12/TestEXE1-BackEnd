package model;

import java.util.List;

public class Cart {

    int cartId;
    int userId;
    private Product product;
    private int quantity;
    private int isSelect; // New property for selection status
    private int colorId;
   

    public Cart(int cartId, int userId, Product product, int quantity, int isSelect, int colorId) {
        this.cartId = cartId;
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
        this.isSelect = isSelect;
        this.colorId = colorId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getIsSelect() {
        return isSelect; // Getter for isSelect
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect; // Setter for isSelect
    }

    public Cart() {
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Cart(int cartId, int userId, Product product, int quantity, int isSelect) {
        this.cartId = cartId;
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
        this.isSelect = isSelect; // Initialize isSelect
    }

   

}
