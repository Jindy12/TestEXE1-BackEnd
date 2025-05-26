/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author tuana
 */
public class Segment {
    private int segmentId;
    private String segmentName;
    private int status;
    private int segmentProductCount;
    
    public Segment() {
    }

    public Segment(int segmentId, String segmentName, int status, int segmentProductCount) {
        this.segmentId = segmentId;
        this.segmentName = segmentName;
        this.status = status;
        this.segmentProductCount = segmentProductCount;
    }

 
    
    public Segment(int segmentId, String segmentName, int status) {
        this.segmentId = segmentId;
        this.segmentName = segmentName;
        this.status = status;
    }
    
    public Segment(int segmentId, String segmentName) {
        this.segmentId = segmentId;
        this.segmentName = segmentName;
    }

    public int getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSegmentProductCount() {
        return segmentProductCount;
    }

    public void setSegmentProductCount(int segmentProductCount) {
        this.segmentProductCount = segmentProductCount;
    }

    
    
    
}
