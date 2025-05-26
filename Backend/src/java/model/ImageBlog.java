/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author tuana
 */
public class ImageBlog {
     private int blogId;
    private int imageBlogId;
    private String imageUrl;
    private String imageText;

    public ImageBlog() {
    }

    public ImageBlog(int blogId, int imageBlogId, String imageUrl, String imageText) {
        this.blogId = blogId;
        this.imageBlogId = imageBlogId;
        this.imageUrl = imageUrl;
        this.imageText = imageText;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public int getImageBlogId() {
        return imageBlogId;
    }

    public void setImageBlogId(int imageBlogId) {
        this.imageBlogId = imageBlogId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }
}
