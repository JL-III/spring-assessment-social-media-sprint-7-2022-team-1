package com.cooksys.socialmedia.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.ProfileDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Credentials;
import com.cooksys.socialmedia.entities.Profile;
import com.cooksys.socialmedia.entities.User;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {

	@Mapping(target = "username", source = "credentials.username")
	UserResponseDto entityToDto(User user);

	User userRequestDtoToEntity(UserRequestDto userRquestDto);
	
	User userResponseDtoToEntity(UserResponseDto userResponseDto);

	@Mapping(target = "phone", source = "profile.phone")
	List<UserResponseDto> entitiesToDtos(List<User> users);

	Profile profileDtoToEntity(ProfileDto profile);
	
	Credentials credentialRequestDtoToEntity(CredentialsDto credentialsDto);

	List<User> dtosToEntities(List<UserResponseDto> allUsers);
	
}
