package com.example.crappostsbackend.controller;

import com.example.crappostsbackend.enums.VoteStatus;
import com.example.crappostsbackend.exception.BadRequestException;
import com.example.crappostsbackend.exception.ResourceNotFoundException;
import com.example.crappostsbackend.formdata.PostFormData;
import com.example.crappostsbackend.model.AppUser;
import com.example.crappostsbackend.model.Post;
import com.example.crappostsbackend.response.PostResponse;
import com.example.crappostsbackend.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.crappostsbackend.service.PostsService;

import javax.validation.Valid;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(path="/api/v1")
public class PostsController {

    private final PostsService postsService;
    private final AppUserService appUserService;

    @Autowired
    public PostsController(PostsService postsService, AppUserService appUserService) {
        this.postsService = postsService;
        this.appUserService = appUserService;
    }

    //get all posts
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postsService.findAllActivePosts();
        for (Post post: posts) {
            log.info(post.toString());
        }
        postsService.sortPostByDate(posts);
        try
        {
            AppUser authenticatedAppUser = appUserService.getAuthenticatedAppUser();
            List<PostResponse> postsResponse = postsService.getPostResponseLoggedIn(posts, authenticatedAppUser);
            return ResponseEntity.ok().body(postsResponse);
        }
        catch(Exception exception){
            List<PostResponse> postsResponse = postsService.getPostResponsesNotLoggedIn(posts);
            return ResponseEntity.ok().body(postsResponse);
        }
    }
    //get post by id
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable(value = "id") long postId) throws ResourceNotFoundException {
        Post post = postsService.getActivePostById(postId);
        return ResponseEntity.ok().body(post);
    }

    //get all users posts
    @GetMapping("/posts/users/{id}")
    public ResponseEntity<List<PostResponse>> getUsersPost(@PathVariable(value = "id") long userId) throws ResourceNotFoundException{
        AppUser appUser = appUserService.getAbledAppUser(userId);
        List<Post> userPosts = postsService.filterAbled(appUser.getPosts());
        postsService.sortPostByDate(userPosts);
        List<PostResponse> postsResponse = postsService.getPostResponseLoggedIn(userPosts, appUser);
        return ResponseEntity.ok().body(postsResponse);
    }

    //save post
    @PostMapping("/posts")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostFormData postFormData) {
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post createdPost = postsService.createPost(postFormData, appUser);
        Post addedPost = appUserService.addPost(appUser, createdPost);
        PostResponse postResponse = new PostResponse(addedPost, VoteStatus.UNVOTED);
        return ResponseEntity.ok().body(postResponse);
    }

    //update post
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable(name = "id") long postId, @Valid @RequestBody PostFormData postFormData) throws ResourceNotFoundException
    {
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post activePost = postsService.getActivePostById(postId);
        Post ownedPost = appUserService.checkOwnedPost(appUser, activePost);
        Post newPost = postsService.updatePost(ownedPost, postFormData);
        PostResponse postResponse = postsService.getPostResponse(newPost, appUser);
        return ResponseEntity.ok().body(postResponse);
    }

    //delete post
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable(name = "id") long postId) throws ResourceNotFoundException
    {
        Post activePost = postsService.getActivePostById(postId);
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post ownedPost = appUserService.checkOwnedPost(appUser, activePost);
        Post disabledPost = postsService.disablePost(ownedPost);
        return ResponseEntity.ok().body(disabledPost);
    }

    //upvote post
    @PutMapping("/posts/{id}/upvote")
    public ResponseEntity<PostResponse> upvotePost(@PathVariable(name="id") long postId) throws ResourceNotFoundException, BadRequestException {
        Post post = postsService.getActivePostById(postId);
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post upvotedPost = postsService.upvote(post, appUser);
        PostResponse postResponse = new PostResponse(upvotedPost, VoteStatus.UPVOTED);
        return ResponseEntity.ok().body(postResponse);
    }

    //downvote post
    @PutMapping("/posts/{id}/downvote")
    public ResponseEntity<PostResponse> downvotePost(@PathVariable(name="id") long postId) throws ResourceNotFoundException, BadRequestException {
        Post post = postsService.getActivePostById(postId);
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post downvotedPost = postsService.downvote(post, appUser);
        PostResponse postResponse = new PostResponse(downvotedPost, VoteStatus.DOWNVOTED);
        return ResponseEntity.ok().body(postResponse);
    }

    //unvote post
    @PutMapping("/posts/{id}/unvote")
    public ResponseEntity<PostResponse> unvotePost(@PathVariable(name = "id") long postId) throws ResourceNotFoundException {
        Post post = postsService.getActivePostById(postId);
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post unvotedPost = postsService.unvote(post, appUser);
        PostResponse postResponse = new PostResponse(unvotedPost, VoteStatus.UNVOTED);
        return ResponseEntity.ok().body(postResponse);
    }

}
