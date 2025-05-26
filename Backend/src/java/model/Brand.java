/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author tuana
 */
public class Brand {

    private int brandId;
    private String name;
    private String image;
    private int status;
    private int productCount;
    private double totalRevenue;

        public Brand(String name, double totalRevenue) {
            this.name = name;
            this.totalRevenue = totalRevenue;
        }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

       
    public Brand() {
    }

    public Brand(int brandId, String name, int status, int productCount) {
        this.brandId = brandId;
        this.name = name;
        this.status = status;
        this.productCount = productCount;
    }

    public Brand(int brandId, String name, String image, int status, int productCount) {
        this.brandId = brandId;
        this.name = name;
        this.image = image;
        this.status = status;
        this.productCount = productCount;
    }

    public Brand(int brandId, String name, String image, int status) {
        this.brandId = brandId;
        this.name = name;
        this.image = image;
        this.status = status;
    }

    public Brand(int brandId, String name, String image) {
        this.brandId = brandId;
        this.name = name;
        this.image = image;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return name;
    }

    public void setBrandName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
