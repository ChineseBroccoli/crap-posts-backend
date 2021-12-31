package com.example.crappostsbackend.response;

import com.example.crappostsbackend.enums.VoteStatus;
import com.example.crappostsbackend.model.Post;

public class PostResponse {
    private Post post;
    private VoteStatus voteStatus;

    public PostResponse(Post post, VoteStatus voteStatus) {
        this.post = post;
        this.voteStatus = voteStatus;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public VoteStatus getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(VoteStatus voteStatus) {
        this.voteStatus = voteStatus;
    }
}
