package com.cooksys.socialmedia.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	@GetMapping
	public List<UserResponseDto> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@GetMapping("/@{username}")
	public UserResponseDto getUserByUserName(@PathVariable String username) {
		return userService.getUserByUserName(username);
	}

	@GetMapping("/@{username}/mentions")
	public List<TweetResponseDto> getAllTweetsMentionsUser(@PathVariable String username) {
		return userService.getAllTweetsMentionsUser(username);
	}

	@GetMapping("/@{username}/feed")
	public List<TweetResponseDto> getUserFeed(@PathVariable String username) {
		return userService.getUserFeed(username);
	}
	
	@GetMapping("/@{username}/tweets")
	public List<TweetResponseDto> getUsersTweets(@PathVariable String username) {
		return userService.getUsersTweets(username);
	}
	
	@PatchMapping("/@{username}")
	public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
		return userService.updateUser(username, userRequestDto);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
		return userService.createUser(userRequestDto);
	}
	
	@DeleteMapping("/@{username}")
	public UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		return userService.deleteUser(username, credentialsDto);
	}
	
	
	@PostMapping("/@{username}/follow")
	public void followUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		userService.followUser(username, credentialsDto);
	}
	
	@GetMapping("/@{username}/following")
	public List<UserResponseDto> getListUsernameIsFollowing(@PathVariable String username) {
		return userService.getListUsernameIsFollowing(username);
	}
	
	@GetMapping("/@{username}/followers")
	public List<UserResponseDto> getListOfFollowers(@PathVariable String username) {
		return userService.getListOfFollowers(username);
	}
	
	@PostMapping("/@{username}/unfollow")
	public void unfollowUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		userService.unfollowUser(username, credentialsDto);
	}
	
}
