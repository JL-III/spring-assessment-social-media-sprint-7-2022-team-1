package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Table(name = "user_table")
@Entity
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Credentials credentials;

    @Column(nullable = false)
    @CreatedDate
    private Timestamp joined = Timestamp.valueOf(LocalDateTime.now());

    private boolean deleted;

    @Embedded
    private Profile profile;

    @OneToMany(mappedBy = "author")
    private List<Tweet> tweets = new ArrayList<>();

    @ManyToMany
    @JoinTable (
            name = "followers_following",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private List<User> followers = new ArrayList<>();

    @ManyToMany(mappedBy = "followers")
    private List<User> following = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> likedTweets = new ArrayList<>();

    @ManyToMany
    @JoinTable (
            name = "user_mentions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )

    private List<Tweet> mentionedBy = new ArrayList<>();
}
