package com.cooksys.socialmedia.services;

import com.cooksys.socialmedia.dtos.*;

import java.util.List;

public interface TweetService {
    List<TweetResponseDto> getAllTweetsNotDeleted();

    TweetResponseDto getTweetById(Long id);

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto);

    List<UserResponseDto> getUsersMentionedInTweet(Long id);

    List<TweetResponseDto> getTweetReplies(Long id);

    List<UserResponseDto> getTweetLikes(Long id);

    List<HashtagDto> getTweetHashtags(Long id);

    List<TweetResponseDto> getTweetReposts(Long id);

    String getTweetContent(Long id);

    void likeTweet(Long id, CredentialsDto credentialsDto);

	TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto);

    TweetResponseDto retweet(Long id, CredentialsDto credentialsDto);

	ContextDto getTweetContext(Long id);

}
