/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author tuana
 */
public class BlogTopic {
    private int blogTopicId;
    private String toppicName;
    private int blogTopicCount;
    private int status;
    
    public BlogTopic() {
    }

    public BlogTopic(int blogTopicId, String toppicName, int blogTopicCount, int status) {
        this.blogTopicId = blogTopicId;
        this.toppicName = toppicName;
        this.blogTopicCount = blogTopicCount;
        this.status = status;
    }
    
    public BlogTopic(int blogTopicId, String toppicName) {
        this.blogTopicId = blogTopicId;
        this.toppicName = toppicName;
    }

    public int getBlogTopicId() {
        return blogTopicId;
    }

    public void setBlogTopicId(int blogTopicId) {
        this.blogTopicId = blogTopicId;
    }

    public String getToppicName() {
        return toppicName;
    }

    public void setToppicName(String toppicName) {
        this.toppicName = toppicName;
    }

    public int getBlogTopicCount() {
        return blogTopicCount;
    }

    public void setBlogTopicCount(int blogTopicCount) {
        this.blogTopicCount = blogTopicCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
