package com.example.crappostsbackend.service;

import com.example.crappostsbackend.enums.VoteStatus;
import com.example.crappostsbackend.exception.BadRequestException;
import com.example.crappostsbackend.exception.ResourceNotFoundException;
import com.example.crappostsbackend.formdata.PostFormData;
import com.example.crappostsbackend.model.AppUser;
import com.example.crappostsbackend.model.Post;
import com.example.crappostsbackend.repository.PostsRepository;
import com.example.crappostsbackend.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Autowired
    public PostsService(PostsRepository postsRepository)
    {
        this.postsRepository = postsRepository;
    }

    public Post getActivePostById(Long postId) throws ResourceNotFoundException {
        Post post = this.postsRepository.findById(postId)
                .orElseThrow(() -> ResourceNotFoundException.postNotFoundById(postId));
        if (post.getIsDisabled()) {
            throw ResourceNotFoundException.postNotFoundById(postId);
        }
        return post;
    }

    public void sortPostByDate(List<Post> posts)
    {
        Collections.sort(posts, new Comparator<Post>() {
            public int compare(Post p1, Post p2) {
                return p2.getUpdatedAt().compareTo(p1.getUpdatedAt());
            }
        });
    }

    public List<PostResponse> getPostResponsesNotLoggedIn(List<Post> posts)
    {
        List<PostResponse> postsResponse = posts.stream().map(post -> {
            return new PostResponse(post, VoteStatus.UNVOTED);
        }).collect(Collectors.toList());
        return postsResponse;
    }

    public PostResponse getPostResponse(Post post, AppUser authenticatedAppUser)
    {
        VoteStatus voteStatus = VoteStatus.UNVOTED;
        if (post.getUpvotedUsers().contains(authenticatedAppUser))
        {
            voteStatus = VoteStatus.UPVOTED;
        } else if (post.getDownvotedUsers().contains(authenticatedAppUser)){
            voteStatus = VoteStatus.DOWNVOTED;
        }
        return new PostResponse(post, voteStatus);
    }

    public List<PostResponse> getPostResponseLoggedIn(List<Post> posts, AppUser authenticatedAppUser)
    {
        List<PostResponse> postsResponse = posts.stream().map(post ->
                getPostResponse(post, authenticatedAppUser))
                .collect(Collectors.toList());
        return postsResponse;
    }

    public List<Post> findAllActivePosts(){
        return filterAbled(this.postsRepository
                .findAll());
    }

    public List<Post> filterAbled(List<Post> posts){
        return posts.stream().filter(post -> !post.getIsDisabled()).collect(Collectors.toList());
    }

    public Post updatePost(Post post, PostFormData postFormData)
    {
        post.setTitle(postFormData.getTitle());
        post.setText(postFormData.getText());
        post.setUpdatedAt(new Date());
        return this.postsRepository.save(post);
    }

    public Post createPost(PostFormData postFormData, AppUser appUser) {
        Post createdPost = new Post();
        createdPost.setCreator(appUser);
        createdPost.setTitle(postFormData.getTitle());
        createdPost.setText(postFormData.getText());
        return this.postsRepository.save(createdPost);
    }

    public Post disablePost(Post post){
        post.setIsDisabled(true);
        return this.postsRepository.save(post);
    }

    public Post upvote(Post post, AppUser appUser) throws BadRequestException {
        if (post.getDownvotedUsers().contains(appUser))
        {
            post.removeFromDownvotedUsers(appUser);
        }
        if (post.getUpvotedUsers().contains(appUser))
        {
            throw BadRequestException.alreadyUpvoted(post.getId());
        }
        else {
            post.addToUpvotedUsers(appUser);
        }
        return this.postsRepository.save(post);
    }

    public Post downvote(Post post, AppUser appUser) throws BadRequestException {
        if (post.getUpvotedUsers().contains(appUser)) {
            post.removeFromUpvotedUsers(appUser);
        }
        if (post.getDownvotedUsers().contains(appUser))
        {
            throw BadRequestException.alreadyDownvoted(post.getId());
        }
        else {
            post.addToDownvotedUsers(appUser);
        }
        return this.postsRepository.save(post);
    }

    public Post unvote(Post post, AppUser appUser) {
        if (post.getUpvotedUsers().contains(appUser)) {
            post.removeFromUpvotedUsers(appUser);
        }
        if (post.getDownvotedUsers().contains(appUser))
        {
            post.removeFromDownvotedUsers(appUser);
        }
        return this.postsRepository.save(post);
    }
}
