package com.cooksys.socialmedia.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.ContextDto;
import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.exceptions.NotFoundException;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.mappers.UserMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.TweetService;
import com.cooksys.socialmedia.util.Utilities;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetMapper tweetMapper;
	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final HashtagMapper hashtagMapper;
	private final HashtagRepository hashtagRepository;
	private final UserServiceImpl userService;
	private final Utilities utilities;


	private Hashtag createHashtag(String label) {
		Hashtag hashtagToCreate = new Hashtag();
		hashtagToCreate.setFirstUsed(Timestamp.valueOf(LocalDateTime.now()));
		hashtagToCreate.setLastUsed(Timestamp.valueOf(LocalDateTime.now()));
		hashtagToCreate.setLabel(label);
		return hashtagToCreate;
	}

	private List<String> filterData(String content, String contentToFind) {
		List<String> stringList = new ArrayList<>();
		for (String string : Arrays.asList(content.split(" "))) {
			if (string.contains(contentToFind)) {
				if (string.charAt(0) == contentToFind.charAt(0))
					stringList.add(string.substring(1));
			}
		}
		return stringList;
	}

	public Tweet getTweet(Long id) {
		if (tweetRepository.findByIdAndDeletedFalse(id).isEmpty()) {
			throw new NotFoundException("Tweet with ID " + id + " does not exist.");
		}
		return tweetRepository.findByIdAndDeletedFalse(id).get();
	}
	
	public void registerTags(Tweet tweet) {
		for (String label : filterData(tweet.getContent(), "#")) {
			if (hashtagRepository.findByLabel(label).isEmpty()) {
				Hashtag createdHashtag = createHashtag(label);
				hashtagRepository.saveAndFlush(createdHashtag);
				tweet.getHashtags().add(createdHashtag);
			} else {
				Hashtag hashtagToUpdate = hashtagRepository.findByLabel(label).get();
				hashtagToUpdate.setTweetList(hashtagRepository.findByLabel(label).get().getTweetList());
				hashtagToUpdate.setLastUsed(Timestamp.valueOf(LocalDateTime.now()));
				hashtagRepository.saveAndFlush(hashtagToUpdate);
				tweet.getHashtags().add(hashtagToUpdate);
			}
		}
	}
	
	public void registerMentions(Tweet tweet) {
		for (String string : filterData(tweet.getContent(), "@")) {
			Optional<User> optionalUser = userRepository.findByCredentialsUsername(string);
			if (!optionalUser.isEmpty()) {
				optionalUser.get().getMentionedBy().add(tweet);
				tweet.getMentions().add(optionalUser.get());
			}
		}
	}

	@Override
	public List<TweetResponseDto> getAllTweetsNotDeleted() {
		List<Tweet> tweets = tweetRepository.findAllByDeletedFalse();
		Collections.sort(tweets);
		return tweetMapper.entitiesToDtos(tweets);
	}

	@Override
	public TweetResponseDto getTweetById(Long id) {
		return tweetMapper.entityToDto(getTweet(id));
	}

	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		
		utilities.tweetHasContent(tweetMapper.requestDtoToEntity(tweetRequestDto));
		utilities.credentialsHaveRequiredFields(tweetRequestDto.getCredentials());
		utilities.matchesCredentials(userService.getUserByCredentials(tweetRequestDto.getCredentials()), tweetRequestDto);
		
		Tweet tweetToCreate = tweetMapper.requestDtoToEntity(tweetRequestDto);
		tweetToCreate.setAuthor(userService.getUserByCredentials(tweetRequestDto.getCredentials()));
		tweetRepository.saveAndFlush(tweetToCreate);
		
		registerTags(tweetToCreate);
		tweetRepository.saveAndFlush(tweetToCreate);
		registerMentions(tweetToCreate);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToCreate));
	}

	@Override
	public TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto) {
		utilities.matchesCredentials(getTweet(id).getAuthor(), credentialsDto);
		getTweet(id).setDeleted(true);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(getTweet(id)));
	}

	@Override
	public List<UserResponseDto> getUsersMentionedInTweet(Long id) {
		return userMapper.entitiesToDtos(getTweet(id).getMentions());
	}

	@Override
	public List<TweetResponseDto> getTweetReplies(Long id) {
		List<Tweet> replies = getTweet(id).getReplies();
		Collections.sort(replies);
		replies.removeIf(Tweet::isDeleted);
		return tweetMapper.entitiesToDtos(replies);
	}

	@Override
	public List<UserResponseDto> getTweetLikes(Long id) {
		List<User> likes = getTweet(id).getLikes();
		likes.removeIf(User::isDeleted);
		return userMapper.entitiesToDtos(likes);
	}

	@Override
	public List<HashtagDto> getTweetHashtags(Long id) {
		return hashtagMapper.entitiesToDtos(getTweet(id).getHashtags());
	}

	@Override
	public List<TweetResponseDto> getTweetReposts(Long id) {
		List<Tweet> reposts = getTweet(id).getReposts();
		Collections.sort(reposts);
		reposts.removeIf(Tweet::isDeleted);
		return tweetMapper.entitiesToDtos(reposts);
	}

	@Override
	public String getTweetContent(Long id) {
		return getTweet(id).getContent();
	}

	@Override
	public void likeTweet(Long id, CredentialsDto credentialsDto) {
		Tweet tweetToBeLiked = getTweet(id);
		User user = userService.getUserByCredentials(credentialsDto);
		if (tweetToBeLiked.getLikes().contains(user))
			return;
		utilities.matchesCredentials(user, credentialsDto);

		tweetToBeLiked.getLikes().add(user);
		user.getLikedTweets().add(tweetToBeLiked);
		tweetRepository.saveAndFlush(tweetToBeLiked);
		userRepository.saveAndFlush(user);
	}

	@Override
	public TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto) {

		Tweet tweetResponseToExistingTweet = getTweet(createTweet(tweetRequestDto).getId());
		Tweet tweetBeingRepliedTo = getTweet(id);

		tweetResponseToExistingTweet.setInReplyTo(tweetBeingRepliedTo);
		tweetBeingRepliedTo.getReplies().add(tweetResponseToExistingTweet);

		tweetRepository.saveAndFlush(tweetBeingRepliedTo);
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetResponseToExistingTweet));
	}

	@Override
	public TweetResponseDto retweet(Long id, CredentialsDto credentialsDto) {
		Tweet tweetToRetweet = getTweet(id);
		Tweet tweetToSave = new Tweet();
		utilities.matchesCredentials(userService.getUserByUsernameReturnUserEntity(credentialsDto.getUsername()),credentialsDto);
		
		tweetToSave.setAuthor(userService.getUserByUsernameReturnUserEntity(credentialsDto.getUsername()));
		tweetToSave.setContent(tweetToRetweet.getContent());
		tweetToSave.setRepostOf(tweetToRetweet);
		tweetToSave.setTimePosted(Timestamp.valueOf(LocalDateTime.now()));
		tweetToRetweet.getReposts().add(tweetRepository.saveAndFlush(tweetToSave));
		tweetRepository.saveAndFlush(tweetToRetweet);
		
		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToRetweet));
	}

	@Override
	public ContextDto getTweetContext(Long id) {

		ContextDto context = new ContextDto();
		Tweet inReplyTo = getTweet(id).getInReplyTo();
		List<Tweet> replies = getTweet(id).getReplies();
		List<Tweet> listOfInReplyTo = new ArrayList<>();

		while (inReplyTo != null) {
			listOfInReplyTo.add(inReplyTo);
			inReplyTo = inReplyTo.getInReplyTo();
		}

		listOfInReplyTo.removeIf(Tweet::isDeleted);
		replies.removeIf(Tweet::isDeleted);

		context.setTarget(tweetMapper.entityToDto(getTweet(id)));
		context.setBefore(tweetMapper.entitiesToDtos(listOfInReplyTo));
		context.setAfter(tweetMapper.entitiesToDtos(replies));

		return context;
	}

}
