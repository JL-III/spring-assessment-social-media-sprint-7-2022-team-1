package com.cooksys.socialmedia.services;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;

import java.util.List;

public interface HashtagService {
    List<HashtagDto> getAllHashtags();

    List<TweetResponseDto> getTweetsWithHashtag(String label);
}
