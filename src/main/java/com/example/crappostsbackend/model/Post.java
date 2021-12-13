package com.example.crappostsbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<AppUser> upvotedUsers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<AppUser> downvotedUsers;

    @Column(name = "createdAt")
    private Date createdAt = new Date();

    @Column(name = "updatedAt")
    private Date updatedAt = new Date();

    @Column(name = "isDisabled")
    private boolean isDisabled = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getVotes() {
        return upvotedUsers.size() - downvotedUsers.size();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createAt) {
        this.createdAt = createAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean disabled) {
        this.isDisabled = disabled;
    }

    public List<AppUser> getUpvotedUsers() {
        return upvotedUsers;
    }

    public void addToUpvotedUsers(AppUser appUser) {
        upvotedUsers.add(appUser);
    }

    public void removeFromUpvotedUsers(AppUser appUser) {
        upvotedUsers.remove(appUser);
    }

    public List<AppUser> getDownvotedUsers() {
        return downvotedUsers;
    }

    public void addToDownvotedUsers(AppUser appUser) {
        downvotedUsers.add(appUser);
    }

    public void removeFromDownvotedUsers(AppUser appUser) {
        downvotedUsers.remove(appUser);
    }

}
