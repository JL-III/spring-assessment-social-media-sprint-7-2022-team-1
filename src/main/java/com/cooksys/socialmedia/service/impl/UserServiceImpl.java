package com.cooksys.socialmedia.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.exceptions.BadRequestException;
import com.cooksys.socialmedia.exceptions.NotFoundException;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.mappers.UserMapper;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.UserService;
import com.cooksys.socialmedia.services.ValidateService;
import com.cooksys.socialmedia.util.Utilities;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final TweetMapper tweetMapper;
	private final ValidateService validateService;
	private final Utilities utilities;

	public User getUserByCredentials(CredentialsDto credentialsDto) {
		return getUserByUsernameReturnUserEntity(credentialsDto.getUsername());
	}

	public User getUserByUsernameReturnUserEntity(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty() || optionalUser.get().isDeleted()) {
			throw new NotFoundException("User not found with username: " + username);
		}
		return optionalUser.get();
	}

	public User getUserById(Long id) {
		if (userRepository.findByIdAndDeletedFalse(id).isEmpty())
			throw new BadRequestException("User with id: " + id + " was not found");
		return userRepository.findByIdAndDeletedFalse(id).get();
	}
	
	private void setProfileInfo(User oldInfo, User newInfo) {
		if (newInfo.getProfile().getEmail() != null) {
			oldInfo.getProfile().setEmail(newInfo.getProfile().getEmail());
		}
		if (newInfo.getProfile().getFirstName() != null) {
			oldInfo.getProfile().setFirstName(newInfo.getProfile().getFirstName());
		}
		if (newInfo.getProfile().getLastName() != null) {
			oldInfo.getProfile().setLastName(newInfo.getProfile().getLastName());
		}
		if (newInfo.getProfile().getPhone() != null) {
			oldInfo.getProfile().setPhone(newInfo.getProfile().getPhone());
		}
	}

	@Override
	public UserResponseDto getUserByUserName(String username) {
		return userMapper.entityToDto(getUserByUsernameReturnUserEntity(username));
	}

	@Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
	}

	@Override
	public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {

		validateService.usernameExists(username);
		User userInDatabase = getUserByUsernameReturnUserEntity(username);
		User userNewInfo = userMapper.userRequestDtoToEntity(userRequestDto);

		utilities.hasProfileInfo(userNewInfo);
		utilities.credentialsHaveRequiredFields(userNewInfo.getCredentials());
		utilities.matchesCredentials(userInDatabase.getCredentials(), userNewInfo.getCredentials());
		setProfileInfo(userInDatabase, userNewInfo);
		
		return userMapper.entityToDto(userRepository.saveAndFlush(userInDatabase));
	}

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {

		utilities.credentialsHaveRequiredFields(userRequestDto.getCredentials());
		if (validateService.usernameExists(userRequestDto)) {
			throw new BadRequestException("That username already exists! Please try another one!");
		}
		User userToCreate = userMapper.userRequestDtoToEntity(userRequestDto);
		utilities.hasEmail(userToCreate);
		utilities.credentialsHaveRequiredFields(userToCreate.getCredentials());

		for (User user : userRepository.findAll()) {
			if (utilities.matchesCredentials(user.getCredentials(), userToCreate.getCredentials())
					&& user.isDeleted()) {
				user.setDeleted(false);
				return userMapper.entityToDto(userRepository.saveAndFlush(user));
			}
		}

		userToCreate.setCredentials(userMapper.credentialRequestDtoToEntity(userRequestDto.getCredentials()));
		userToCreate.setProfile(userMapper.profileDtoToEntity(userRequestDto.getProfile()));
		return userMapper.entityToDto(userRepository.saveAndFlush(userToCreate));
	}

	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		utilities.credentialsHaveRequiredFields(credentialsDto);
		User user = getUserByUsernameReturnUserEntity(username);
		utilities.matchesCredentials(user.getCredentials(), getUserByCredentials(credentialsDto).getCredentials());
		user.setDeleted(true);
		return userMapper.entityToDto(userRepository.saveAndFlush(user));
	}

	@Override
	public void followUser(String username, CredentialsDto credentialsDto) {
		User userToSubscribe = getUserByUsernameReturnUserEntity(credentialsDto.getUsername());
		User userBeingFollowed = getUserByUsernameReturnUserEntity(username);

		List<User> following = userToSubscribe.getFollowing();
		List<User> subscribed = userBeingFollowed.getFollowers();
		if (following.contains(userBeingFollowed)) {
			throw new BadRequestException("You are already following this person!");
		}
		if (userToSubscribe.equals(userBeingFollowed)) {
			throw new BadRequestException("Nice try, but you cannot follow yourself!");
		}
		utilities.matchesCredentials(userToSubscribe, credentialsDto);
		following.add(userBeingFollowed);
		subscribed.add(userToSubscribe);
		userBeingFollowed.setFollowers(subscribed);
		userToSubscribe.setFollowing(following);
		userRepository.saveAndFlush(userToSubscribe);
		userRepository.saveAndFlush(userBeingFollowed);
	}

	@Override
	public List<UserResponseDto> getListUsernameIsFollowing(String username) {
		List<User> usersFollowing = getUserByUsernameReturnUserEntity(username).getFollowing();
		usersFollowing.removeIf(User::isDeleted);
		return userMapper.entitiesToDtos(usersFollowing);
	}

	@Override
	public List<UserResponseDto> getListOfFollowers(String username) {
		List<User> usersFollowers = getUserByUsernameReturnUserEntity(username).getFollowers();
		usersFollowers.removeIf(User::isDeleted);
		return userMapper.entitiesToDtos(usersFollowers);
	}

	@Override
	public List<TweetResponseDto> getAllTweetsMentionsUser(String username) {
		if (!validateService.usernameExists(username)) {
			throw new BadRequestException("User not found");
		}

		List<Tweet> tweets = getUserByUsernameReturnUserEntity(username).getMentionedBy();
		tweets.removeIf(Tweet::isDeleted);
		Collections.reverse(tweets);
		return tweetMapper.entitiesToDtos(tweets);
	}

	@Override
	public List<TweetResponseDto> getUserFeed(String username) {
		List<Tweet> feedOfTweets = getUserByUsernameReturnUserEntity(username).getTweets();
		for (User userFollowing : getUserByUsernameReturnUserEntity(username).getFollowing()) {
			feedOfTweets.addAll(userFollowing.getTweets());
		}

		Collections.sort(feedOfTweets);
		feedOfTweets.removeIf(Tweet::isDeleted);

		return tweetMapper.entitiesToDtos(feedOfTweets);
	}

	@Override
	public void unfollowUser(String username, CredentialsDto credentialsDto) {
		User userToUnsubscribe = getUserByUsernameReturnUserEntity(credentialsDto.getUsername());
		User userBeingFollowed = getUserByUsernameReturnUserEntity(username);

		List<User> following = userToUnsubscribe.getFollowing();
		List<User> subscribed = userBeingFollowed.getFollowers();
		if (!following.contains(userBeingFollowed)) {
			throw new BadRequestException("You are not following this person!");
		}
		if (userToUnsubscribe.equals(userBeingFollowed)) {
			throw new BadRequestException("Nice try, but you cannot follow yourself!");
		}
		utilities.matchesCredentials(userToUnsubscribe, credentialsDto);
		following.remove(userBeingFollowed);
		subscribed.remove(userToUnsubscribe);
		userBeingFollowed.setFollowers(subscribed);
		userToUnsubscribe.setFollowing(following);
		userRepository.saveAndFlush(userToUnsubscribe);
		userRepository.saveAndFlush(userBeingFollowed);
	}

	@Override
	public List<TweetResponseDto> getUsersTweets(String username) {
		List<Tweet> tweets = getUserByUsernameReturnUserEntity(username).getTweets();
		Collections.sort(tweets);
		tweets.removeIf(Tweet::isDeleted);
		return tweetMapper.entitiesToDtos(tweets);
	}

}
