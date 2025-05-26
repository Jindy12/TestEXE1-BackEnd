/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 *
 * @author tuana
 */
public class Blog {
    private int blogId;
    private int userId;
    private int blogTopicId;
    private String blogTitle;
    private String blogTime;
    private String openBlog;
    private String bodyMain1;
    private String bodySp1;
    private String bodyMain2;
    private String bodySp2;
    private String bodyMain3;
    private String bodySp3;
    private String endBlog;
    private int status;
    List<ImageBlog> images;

    public Blog() {
    }

    public Blog(int blogId, int userId, int blogTopicId, String blogTitle, String blogTime, String openBlog, String bodyMain1, String bodySp1, String bodyMain2, String bodySp2, String bodyMain3, String bodySp3, String endBlog, int status, List<ImageBlog> images) {
        this.blogId = blogId;
        this.userId = userId;
        this.blogTopicId = blogTopicId;
        this.blogTitle = blogTitle;
        this.blogTime = blogTime;
        this.openBlog = openBlog;
        this.bodyMain1 = bodyMain1;
        this.bodySp1 = bodySp1;
        this.bodyMain2 = bodyMain2;
        this.bodySp2 = bodySp2;
        this.bodyMain3 = bodyMain3;
        this.bodySp3 = bodySp3;
        this.endBlog = endBlog;
        this.status = status;
        this.images = images;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBlogTopicId() {
        return blogTopicId;
    }

    public void setBlogTopicId(int blogTopicId) {
        this.blogTopicId = blogTopicId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogTime() {
        return blogTime;
    }

    public void setBlogTime(String blogTime) {
        this.blogTime = blogTime;
    }

    public String getOpenBlog() {
        return openBlog;
    }

    public void setOpenBlog(String openBlog) {
        this.openBlog = openBlog;
    }

    public String getBodyMain1() {
        return bodyMain1;
    }

    public void setBodyMain1(String bodyMain1) {
        this.bodyMain1 = bodyMain1;
    }

    public String getBodySp1() {
        return bodySp1;
    }

    public void setBodySp1(String bodySp1) {
        this.bodySp1 = bodySp1;
    }

    public String getBodyMain2() {
        return bodyMain2;
    }

    public void setBodyMain2(String bodyMain2) {
        this.bodyMain2 = bodyMain2;
    }

    public String getBodySp2() {
        return bodySp2;
    }

    public void setBodySp2(String bodySp2) {
        this.bodySp2 = bodySp2;
    }

    public String getBodyMain3() {
        return bodyMain3;
    }

    public void setBodyMain3(String bodyMain3) {
        this.bodyMain3 = bodyMain3;
    }

    public String getBodySp3() {
        return bodySp3;
    }

    public void setBodySp3(String bodySp3) {
        this.bodySp3 = bodySp3;
    }

    public String getEndBlog() {
        return endBlog;
    }

    public void setEndBlog(String endBlog) {
        this.endBlog = endBlog;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ImageBlog> getImages() {
        return images;
    }

    public void setImages(List<ImageBlog> images) {
        this.images = images;
    }
    
}
