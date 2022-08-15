package com.cooksys.socialmedia.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.exceptions.BadRequestException;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

	private final HashtagRepository hashtagRepository;

	private final UserRepository userRepository;

	@Override
	public boolean tagExists(String label) {
		return hashtagRepository.findByLabel(label).isPresent();
	}

	@Override
	public boolean usernameExists(String username) {
		if (username == null) {
			throw new BadRequestException("You must provide a username to look up");
		}
		List<User> usersInDatabase = userRepository.findAllByDeletedFalse();
		if (usersInDatabase.isEmpty()) {
			return false;
		}
		for (User user : usersInDatabase) {
			if (user.getCredentials().getUsername().equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean usernameExists(UserRequestDto userRequestDto) {
		if (userRequestDto.getCredentials().getUsername() == null) {
			throw new BadRequestException("You must provide a username to look up");
		}
		List<User> usersInDatabase = userRepository.findAllByDeletedFalse();
		if (usersInDatabase.isEmpty()) {
			return false;
		}
		for (User user : usersInDatabase) {
			if (user.getCredentials().getUsername().equalsIgnoreCase(userRequestDto.getCredentials().getUsername())) {
				return true;
			}
		}
		return false;
	}
	
}