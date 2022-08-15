package com.cooksys.socialmedia.services;

import com.cooksys.socialmedia.dtos.UserRequestDto;

public interface ValidateService {
	
    boolean tagExists(String label);

	boolean usernameExists(String username);
	
	boolean usernameExists(UserRequestDto userRequestDto);
}
