package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String label;

    @Column(nullable = false)
    @CreatedDate
    private Timestamp firstUsed = Timestamp.valueOf(LocalDateTime.now());

    @Column(nullable = false)
    @LastModifiedDate
    private Timestamp lastUsed = Timestamp.valueOf(LocalDateTime.now());

    @ManyToMany(mappedBy = "hashtags")
    private List<Tweet> tweetList = new ArrayList<>();

}
