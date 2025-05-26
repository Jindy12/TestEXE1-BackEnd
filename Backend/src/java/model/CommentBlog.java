/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author tuana
 */
public class CommentBlog {
    private int commentId;
    private int userId;
    private int blogId;
    private String commentInfor;
    private String commentDate;
    private int commentRating;

    public CommentBlog() {
    }

    public CommentBlog(int commentId, int userId, int blogId, String commentInfor, String commentDate, int commentRating) {
        this.commentId = commentId;
        this.userId = userId;
        this.blogId = blogId;
        this.commentInfor = commentInfor;
        this.commentDate = commentDate;
        this.commentRating = commentRating;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getCommentInfor() {
        return commentInfor;
    }

    public void setCommentInfor(String commentInfor) {
        this.commentInfor = commentInfor;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public int getCommentRating() {
        return commentRating;
    }

    public void setCommentRating(int commentRating) {
        this.commentRating = commentRating;
    }

}