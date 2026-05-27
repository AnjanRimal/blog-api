package com.anjan.blog_api.service;

import com.anjan.blog_api.dto.PostDTO;
import com.anjan.blog_api.entity.Post;
import com.anjan.blog_api.entity.User;
import com.anjan.blog_api.repository.PostRepository;
import com.anjan.blog_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public PostDTO createPost(Post post) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setAuthor(author);
        Post saved = postRepository.save(post);
        return convertToDTO(saved);
    }

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return convertToDTO(post);
    }

    public PostDTO updatePost(Long id, Post post) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!existing.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to update this post");
        }
        existing.setTitle(post.getTitle());
        existing.setContent(post.getContent());
        return convertToDTO(postRepository.save(existing));
    }

    public void deletePost(Long id) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!existing.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to delete this post");
        }
        postRepository.deleteById(id);
    }

    private PostDTO convertToDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getUsername(),
                post.getCreatedAt()
        );
    }
}