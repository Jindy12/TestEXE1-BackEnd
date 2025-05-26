/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Sonvu
 */
public class Style {
    private String styleId;
    private String styleName;
    private int status;
    private int styleProductCount;
    public Style(String styleId, String styleName) {
        this.styleId = styleId;
        this.styleName = styleName;
    }

    public Style(String styleId, String styleName, int status, int styleProductCount) {
        this.styleId = styleId;
        this.styleName = styleName;
        this.status = status;
        this.styleProductCount = styleProductCount;
    }

    public Style(String styleId, String styleName, int status) {
        this.styleId = styleId;
        this.styleName = styleName;
        this.status = status;
    }

    
    
    public Style() {
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStyleProductCount() {
        return styleProductCount;
    }

    public void setStyleProductCount(int styleProductCount) {
        this.styleProductCount = styleProductCount;
    }
    
    
    
}
