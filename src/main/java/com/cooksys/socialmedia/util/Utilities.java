package com.cooksys.socialmedia.util;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.entities.Credentials;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.exceptions.BadRequestException;
import com.cooksys.socialmedia.exceptions.NotAuthorizedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Utilities {
	
	public boolean matchesCredentials(Credentials databaseCredentials, Credentials userRequestCredentials) {
		if (userRequestCredentials == null)
			return false;
		if (!databaseCredentials.getUsername().equalsIgnoreCase(userRequestCredentials.getUsername())
				|| !databaseCredentials.getPassword().equals(userRequestCredentials.getPassword())) {
			return false;
		}
		return true;
	}
	
	public void matchesCredentials(User databaseCredentials, CredentialsDto credentialsDto) {
		if (credentialsDto == null)
			throw new NotAuthorizedException("You must provide credentials.");
		if (!databaseCredentials.getCredentials().getUsername().equalsIgnoreCase(credentialsDto.getUsername())
				|| !databaseCredentials.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
			throw new NotAuthorizedException("Your credentials do not match.");
		}
	}

	public void matchesCredentials(User databaseCredentials, TweetRequestDto tweetRequestDto) {
		if (tweetRequestDto.getCredentials() == null)
			throw new NotAuthorizedException("You must provide credentials.");
		if (!databaseCredentials.getCredentials().getUsername()
				.equalsIgnoreCase(tweetRequestDto.getCredentials().getUsername())
				|| !databaseCredentials.getCredentials().getPassword()
						.equals(tweetRequestDto.getCredentials().getPassword())) {
			throw new NotAuthorizedException("Your credentials do not match.");
		}
	}
	
	public void credentialsHaveRequiredFields(Credentials credentials) {
		if (credentials == null) {
			throw new BadRequestException("You must provide a password and username!");
		}
		if (credentials.getPassword() == null) {
			throw new BadRequestException("You must provide a password!");
		}
		if (credentials.getUsername() == null) {
			throw new BadRequestException("You must provide a username!");
		}
	}
	
	public void credentialsHaveRequiredFields(CredentialsDto credentialsDto) {
		if (credentialsDto == null) {
			throw new BadRequestException("You must provide a password and username!");
		}
		if (credentialsDto.getPassword() == null) {
			throw new BadRequestException("You must provide a password!");
		}
		if (credentialsDto.getUsername() == null) {
			throw new BadRequestException("You must provide a username!");
		}
	}
	
	public void hasEmail(User user) {
		if (user.getProfile() == null) {
			throw new BadRequestException("You must provide an email");
		}
		if (user.getProfile().getEmail() == null) {
			throw new BadRequestException("You must provide an email");
		}
	}

	public void tweetHasContent(Tweet tweet) {
		if (tweet.getContent() == null || tweet.getContent().isEmpty()
				|| tweet.getContent().isBlank()) {
			throw new BadRequestException("Your tweet cannot be blank.");
		}
	}
	
	public void hasProfileInfo(User user) {
		if (user.getProfile() == null)
			throw new BadRequestException("You must provide profile information");
	}
	
}
