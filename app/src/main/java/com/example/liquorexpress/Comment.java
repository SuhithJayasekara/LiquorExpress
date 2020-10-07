package com.example.feedback;

public class Comment {

    String username;
    double rating;
    String userImage;
    String commentTitle;
    String commentDescription;

    public Comment(String username, double rating, String userImage, String commentTitle, String commentDescription) {
        this.username = username;
        this.rating = rating;
        this.userImage = userImage;
        this.commentTitle = commentTitle;
        this.commentDescription = commentDescription;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCommentTitle() {
        return commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }
}
