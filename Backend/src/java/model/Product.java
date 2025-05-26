/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author tuana
 */
import java.text.DecimalFormat;
import java.util.List;

public class Product {

    private String productId;
    private String name;
    private int seatNumber;
    private double price;
    private String fuel;
    private int stock;
    private String description;
    private double VAT;
    private int supplyId;
    private int brandId;
    private int segmentId;
    private int styleId;
    private int status;
    private List<ProductImage> images;
    private List<Color> colorList;
    private int totalQuantitySold;
    private double totalRevenue;

    public Product(String name, double totalRevenue) {
        this.name = name;
        this.totalRevenue = totalRevenue;
    }

    public Product(String productId, String name, int seatNumber, double price, String fuel, int stock, String description, double VAT, int supplyId, int brandId, int segmentId, int styleId, int status, List<ProductImage> images, List<Color> colorList, int totalQuantitySold, double totalRevenue) {
        this.productId = productId;
        this.name = name;
        this.seatNumber = seatNumber;
        this.price = price;
        this.fuel = fuel;
        this.stock = stock;
        this.description = description;
        this.VAT = VAT;
        this.supplyId = supplyId;
        this.brandId = brandId;
        this.segmentId = segmentId;
        this.styleId = styleId;
        this.status = status;
        this.images = images;
        this.colorList = colorList;
        this.totalQuantitySold = totalQuantitySold;
        this.totalRevenue = totalRevenue;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    
     
    public List<Color> getColorList() {
        return colorList;
    }

    public void setColorList(List<Color> colorList) {
        this.colorList = colorList;
    }

    public Product() {
    }

    public Product(String productId, String name, int seatNumber, double price, String fuel, int stock, String description, double VAT, int supplyId, int brandId, int segmentId, int styleId, int status) {
        this.productId = productId;
        this.name = name;
        this.seatNumber = seatNumber;
        this.price = price;
        this.fuel = fuel;
        this.stock = stock;
        this.description = description;
        this.VAT = VAT;
        this.supplyId = supplyId;
        this.brandId = brandId;
        this.segmentId = segmentId;
        this.styleId = styleId;
        this.status = status;
    }

    public Product(String productId, String name, int seatNumber, double price, String fuel, int stock, String description, double VAT, int supplyId, int brandId, int segmentId, int styleId) {
        this.productId = productId;
        this.name = name;
        this.seatNumber = seatNumber;
        this.price = price;
        this.fuel = fuel;
        this.stock = stock;
        this.description = description;
        this.VAT = VAT;
        this.supplyId = supplyId;
        this.brandId = brandId;
        this.segmentId = segmentId;
        this.styleId = styleId;
    }

    public Product(String productId, String name, int seatNumber, double price, String fuel, int stock, String description, double VAT, int supplyId, int brandId, int segmentId, int styleId, List<ProductImage> images) {
        this.productId = productId;
        this.name = name;
        this.seatNumber = seatNumber;
        this.price = price;
        this.fuel = fuel;
        this.stock = stock;
        this.description = description;
        this.VAT = VAT;
        this.supplyId = supplyId;
        this.brandId = brandId;
        this.segmentId = segmentId;
        this.styleId = styleId;
        this.images = images;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceFormat() {
        DecimalFormat df = new DecimalFormat("#,###"); // Định dạng với 2 số thập phân
        return df.format(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getVAT() {
        return VAT;
    }

    public void setVAT(double VAT) {
        this.VAT = VAT;
    }

    public int getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }

    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public void setTotalQuantitySold(int totalQuantitySold) {
        this.totalQuantitySold = totalQuantitySold;
    }
    
    
}
