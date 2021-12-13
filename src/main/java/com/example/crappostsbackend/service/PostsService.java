package com.example.crappostsbackend.service;

import com.example.crappostsbackend.exception.BadRequestException;
import com.example.crappostsbackend.exception.ResourceNotFoundException;
import com.example.crappostsbackend.formdata.PostFormData;
import com.example.crappostsbackend.model.AppUser;
import com.example.crappostsbackend.model.Post;
import com.example.crappostsbackend.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Post> findAllActivePosts(){
        return this.postsRepository
                .findAll()
                .stream()
                .filter(post -> !post.getIsDisabled())
                .collect(Collectors.toList());
    }

    public Post updatePost(Post post, PostFormData postFormData)
    {
        post.setText(postFormData.getText());
        post.setUpdatedAt(new Date());
        return this.postsRepository.save(post);
    }

    public Post createPost(PostFormData postFormData) {
        Post createdPost = new Post();
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
