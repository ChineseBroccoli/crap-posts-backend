package com.example.crappostsbackend.controller;

import com.example.crappostsbackend.exception.BadRequestException;
import com.example.crappostsbackend.exception.ResourceNotFoundException;
import com.example.crappostsbackend.formdata.PostFormData;
import com.example.crappostsbackend.model.AppUser;
import com.example.crappostsbackend.model.Post;
import com.example.crappostsbackend.service.AppUserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.crappostsbackend.service.PostsService;

import javax.validation.Valid;

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
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postsService.findAllActivePosts();
        return ResponseEntity.ok().body(posts);
    }
    //get post by id
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable(value = "id") long postId) throws ResourceNotFoundException {
        Post post = postsService.getActivePostById(postId);
        return ResponseEntity.ok().body(post);
    }

    //save post
    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@Valid @RequestBody PostFormData postFormData) {
        Post createdPost = postsService.createPost(postFormData);
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post addedPost = appUserService.addPost(appUser, createdPost);
        return ResponseEntity.ok().body(addedPost);
    }

    //update post
    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable(name = "id") long postId, @Valid @RequestBody PostFormData postFormData) throws ResourceNotFoundException
    {
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post ownedPost = appUserService.getAuthenticatedUserPosts(appUser, postId);
        Post newPost = postsService.updatePost(ownedPost, postFormData);
        return ResponseEntity.ok().body(newPost);
    }

    //delete post
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable(name = "id") long postId) throws ResourceNotFoundException
    {
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post ownedPost = appUserService.getAuthenticatedUserPosts(appUser, postId);
        Post disabledPost = postsService.disablePost(ownedPost);
        return ResponseEntity.ok().body(disabledPost);
    }

    //upvote post
    @PutMapping("/posts/{id}/upvote")
    public ResponseEntity<Post> upvotePost(@PathVariable(name="id") long postId) throws ResourceNotFoundException, BadRequestException {
        Post post = postsService.getActivePostById(postId);
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post upvotedPost = postsService.upvote(post, appUser);
        return ResponseEntity.ok().body(upvotedPost);
    }

    //downvote post
    @PutMapping("/posts/{id}/downvote")
    public ResponseEntity<Post> downvotePost(@PathVariable(name="id") long postId) throws ResourceNotFoundException, BadRequestException {
        Post post = postsService.getActivePostById(postId);
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post downvotedPost = postsService.downvote(post, appUser);
        return ResponseEntity.ok().body(downvotedPost);
    }

    //unvote post
    @PutMapping("/posts/{id}/unvote")
    public ResponseEntity<Post> unvotePost(@PathVariable(name = "id") long postId) throws ResourceNotFoundException {
        Post post = postsService.getActivePostById(postId);
        AppUser appUser = appUserService.getAuthenticatedAppUser();
        Post unvotedPost = postsService.unvote(post, appUser);
        return ResponseEntity.ok().body(unvotedPost);
    }

}
