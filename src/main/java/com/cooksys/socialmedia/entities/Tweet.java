package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;


@Entity
@NoArgsConstructor
@Data
public class Tweet implements Comparable<Tweet> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User author;

    @CreatedDate
    private Timestamp timePosted = Timestamp.valueOf(LocalDateTime.now());
    
    private boolean deleted;

    private String content;

    @ManyToOne
    private Tweet inReplyTo;
    
    @OneToMany(mappedBy = "inReplyTo")
    private List<Tweet> replies = new ArrayList<>();

    @ManyToOne
    private Tweet repostOf;
    
    @OneToMany(mappedBy = "repostOf")
    private List<Tweet> reposts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tweet_hashtags",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<Hashtag> hashtags = new ArrayList<>();

    @ManyToMany(mappedBy = "likedTweets")
    private List<User> likes = new ArrayList<>();

    @ManyToMany(mappedBy = "mentionedBy")
    private List<User> mentions = new ArrayList<>();

    @Override
    public int compareTo(Tweet o) {
        return (int) (o.getId() - this.id);
    }
}
