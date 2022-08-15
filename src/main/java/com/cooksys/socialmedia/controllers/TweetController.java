package com.cooksys.socialmedia.controllers;

import com.cooksys.socialmedia.dtos.ContextDto;
import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.dtos.HashtagDto;
import org.springframework.web.bind.annotation.*;

import com.cooksys.socialmedia.services.TweetService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
	
	private final TweetService tweetService;

	@GetMapping
	public List<TweetResponseDto> getAllTweetsNotDeleted() {
		return tweetService.getAllTweetsNotDeleted();
	}

	@GetMapping("/{id}")
	public TweetResponseDto getTweetById(@PathVariable Long id) {
		return tweetService.getTweetById(id);
	}

	@GetMapping("/{id}/context")
	public ContextDto getTweetContext(@PathVariable Long id) {
		return tweetService.getTweetContext(id);
	}
	
	@PostMapping("/{id}/reply")
	public TweetResponseDto replyToTweet(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.replyToTweet(id, tweetRequestDto);
	}

	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getTweetReplies(@PathVariable Long id) {
		return tweetService.getTweetReplies(id);
	}

	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getTweetLikes(@PathVariable Long id) {
		return tweetService.getTweetLikes(id);
	}

	@GetMapping("/{id}/tags")
	public List<HashtagDto> getTweetHashtags(@PathVariable Long id) {
		return tweetService.getTweetHashtags(id);
	}

	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getTweetReposts(@PathVariable Long id) {
		return tweetService.getTweetReposts(id);
	}

	@GetMapping("/{id}/mentions")
	public List<UserResponseDto> getUsersMentionedInTweet(@PathVariable Long id) {
		return tweetService.getUsersMentionedInTweet(id);
	}
	
	@PostMapping
	public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweet(tweetRequestDto);
	}

	@PostMapping("/{id}/like")
	public void likeTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		tweetService.likeTweet(id, credentialsDto);
	}

	@PostMapping("/{id}/repost")
	public TweetResponseDto retweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.retweet(id, credentialsDto);
	}
	
	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.deleteTweet(id, credentialsDto);
	}

}
