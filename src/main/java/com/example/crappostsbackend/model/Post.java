package com.example.crappostsbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "posts")
public class Post{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<AppUser> upvotedUsers = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<AppUser> downvotedUsers = new HashSet<>();

    @Column(name = "createdAt")
    private Date createdAt = new Date();

    @Column(name = "updatedAt")
    private Date updatedAt = new Date();

    @JsonIgnoreProperties({"createdAt"})
    @ManyToOne
    private AppUser creator;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "isDisabled")
    private boolean isDisabled = false;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Transient
    private long votes;

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

    public void setVotes(long votes) {
        this.votes = votes;
    }

    public long getVotes() {
        return this.upvotedUsers.size() - this.downvotedUsers.size();
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

    public Set<AppUser> getUpvotedUsers() {
        return upvotedUsers;
    }

    public void addToUpvotedUsers(AppUser appUser) {
        upvotedUsers.add(appUser);
    }

    public void removeFromUpvotedUsers(AppUser appUser) {
        upvotedUsers.remove(appUser);
    }

    public Set<AppUser> getDownvotedUsers() {
        return downvotedUsers;
    }

    public void addToDownvotedUsers(AppUser appUser) {
        downvotedUsers.add(appUser);
    }

    public void removeFromDownvotedUsers(AppUser appUser) {
        downvotedUsers.remove(appUser);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AppUser getCreator() {
        return creator;
    }

    public void setCreator(AppUser creator) {
        this.creator = creator;
    }
}
