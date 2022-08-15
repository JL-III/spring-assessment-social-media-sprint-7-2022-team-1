package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Tweet;

public interface UserService {

	List<UserResponseDto> getAllUsers();

	UserResponseDto getUserByUserName(String username);
	
	UserResponseDto updateUser(String username, UserRequestDto userRequestDto);

	UserResponseDto createUser(UserRequestDto userRequestDto);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	void followUser(String username, CredentialsDto credentialsDto);

	List<UserResponseDto> getListUsernameIsFollowing(String username);

	List<UserResponseDto> getListOfFollowers(String username);
	
	void unfollowUser(String username, CredentialsDto credentialsDto);

    List<TweetResponseDto> getAllTweetsMentionsUser(String username);

    List<TweetResponseDto> getUserFeed(String username);

	List<TweetResponseDto> getUsersTweets(String username);
}

