/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

public class OrderDetail {
    private int orderId;
    private int userId;
    private String firstName;
    private String lastName;
    private Date createDate;
    private String productId;
    private String productName;
    private int quantity;
    private double totalPrice;
    private int orderStatus;
    private String shippingAddress;
    private String brandName;
    private String colorName;
    private String imageUrl;
    private int colorId;
    private int isFeedback;
    private String mobile;
    private int gender;
    private String orderDeliverCode;
    private String orderName;
    private String email;
    private String orderEmail;
    private String orderPhone;
    private String note;
    private int saleId;
    private int shipperId;
    private int orderDetailId;
    private double productPrice;
    private String orderStatusDescription;
    private int payMethod;


    public OrderDetail() {
    }

    public OrderDetail(int orderId, Date createDate, String productId, String productName, int quantity, String brandName, String colorName, String imageUrl, int colorId, int isFeedback, int orderDetailId, double productPrice, String orderStatusDescription) {
        this.orderId = orderId;
        this.createDate = createDate;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.brandName = brandName;
        this.colorName = colorName;
        this.imageUrl = imageUrl;
        this.colorId = colorId;
        this.isFeedback = isFeedback;
        this.orderDetailId = orderDetailId;
        this.productPrice = productPrice;
        this.orderStatusDescription = orderStatusDescription;
    }

    public OrderDetail(int orderId, int userId, String firstName, String lastName, Date createDate, String productId, String productName, int quantity, double totalPrice, int orderStatus, String shippingAddress, String brandName, String colorName, String imageUrl, int colorId, int isFeedback, String mobile, int gender, String orderDeliverCode, String orderName, String email, String orderEmail, String orderPhone, String note, int saleId, int shipperId, int orderDetailId, double productPrice, String orderStatusDescription, int payMethod) {
        this.orderId = orderId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createDate = createDate;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.shippingAddress = shippingAddress;
        this.brandName = brandName;
        this.colorName = colorName;
        this.imageUrl = imageUrl;
        this.colorId = colorId;
        this.isFeedback = isFeedback;
        this.mobile = mobile;
        this.gender = gender;
        this.orderDeliverCode = orderDeliverCode;
        this.orderName = orderName;
        this.email = email;
        this.orderEmail = orderEmail;
        this.orderPhone = orderPhone;
        this.note = note;
        this.saleId = saleId;
        this.shipperId = shipperId;
        this.orderDetailId = orderDetailId;
        this.productPrice = productPrice;
        this.orderStatusDescription = orderStatusDescription;
        this.payMethod = payMethod;
    }

    
   

    public String getMobile() {
        return mobile;
    }

    public int getGender() {
        return gender;
    }

    public String getOrderDeliverCode() {
        return orderDeliverCode;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getEmail() {
        return email;
    }

    public String getOrderEmail() {
        return orderEmail;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public String getNote() {
        return note;
    }

    public int getSaleId() {
        return saleId;
    }

    public int getShipperId() {
        return shipperId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public double getProductPrice() {
        return productPrice;
    }
    
    

    // Các phương thức getter
    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getColorName() {
        return colorName;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public int getColorId() {
        return colorId;
    }

    public int getIsFeedback() {
        return isFeedback;
    }

    // Các phương thức setter
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public void setIsFeedback(int isFeedback) {
        this.isFeedback = isFeedback;
    }

    // Bạn có thể thêm các phương thức khác nếu cần thiết

    public void setMobile(String mobile) {
    this.mobile = mobile;
}

public void setEmail(String email) {
    this.email = email;
}

public void setGender(int gender) {
    this.gender = gender;
}

public void setOrderDeliverCode(String orderDeliverCode) {
    this.orderDeliverCode = orderDeliverCode;
}

public void setOrderName(String orderName) {
    this.orderName = orderName;
}

public void setOrderEmail(String orderEmail) {
    this.orderEmail = orderEmail;
}

public void setOrderPhone(String orderPhone) {
    this.orderPhone = orderPhone;
}

public void setNote(String note) {
    this.note = note;
}

public void setSaleId(int saleId) {
    this.saleId = saleId;
}

public void setShipperId(int shipperId) {
    this.shipperId = shipperId;
}

public void setOrderDetailId(int orderDetailId) {
    this.orderDetailId = orderDetailId;
}

public void setProductPrice(double productPrice) {
    this.productPrice = productPrice;
}

    
// Getter cho orderStatusDescription
public String getOrderStatusDescription() {
    return orderStatusDescription;
}

// Setter cho orderStatusDescription
public void setOrderStatusDescription(String orderStatusDescription) {
    this.orderStatusDescription = orderStatusDescription;
}

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

}
