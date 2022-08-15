package com.cooksys.socialmedia.mappers;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.ProfileDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Credentials;
import com.cooksys.socialmedia.entities.Profile;
import com.cooksys.socialmedia.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-08-07T20:47:44-0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.15 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDto entityToDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setUsername( userCredentialsUsername( user ) );
        userResponseDto.setProfile( profileToProfileDto( user.getProfile() ) );
        userResponseDto.setJoined( user.getJoined() );

        return userResponseDto;
    }

    @Override
    public User userRequestDtoToEntity(UserRequestDto userRquestDto) {
        if ( userRquestDto == null ) {
            return null;
        }

        User user = new User();

        user.setCredentials( credentialRequestDtoToEntity( userRquestDto.getCredentials() ) );
        user.setProfile( profileDtoToEntity( userRquestDto.getProfile() ) );

        return user;
    }

    @Override
    public User userResponseDtoToEntity(UserResponseDto userResponseDto) {
        if ( userResponseDto == null ) {
            return null;
        }

        User user = new User();

        user.setJoined( userResponseDto.getJoined() );
        user.setProfile( profileDtoToEntity( userResponseDto.getProfile() ) );

        return user;
    }

    @Override
    public List<UserResponseDto> entitiesToDtos(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDto> list = new ArrayList<UserResponseDto>( users.size() );
        for ( User user : users ) {
            list.add( entityToDto( user ) );
        }

        return list;
    }

    @Override
    public Profile profileDtoToEntity(ProfileDto profile) {
        if ( profile == null ) {
            return null;
        }

        Profile profile1 = new Profile();

        profile1.setFirstName( profile.getFirstName() );
        profile1.setLastName( profile.getLastName() );
        profile1.setEmail( profile.getEmail() );
        profile1.setPhone( profile.getPhone() );

        return profile1;
    }

    @Override
    public Credentials credentialRequestDtoToEntity(CredentialsDto credentialsDto) {
        if ( credentialsDto == null ) {
            return null;
        }

        Credentials credentials = new Credentials();

        credentials.setUsername( credentialsDto.getUsername() );
        credentials.setPassword( credentialsDto.getPassword() );

        return credentials;
    }

    @Override
    public List<User> dtosToEntities(List<UserResponseDto> allUsers) {
        if ( allUsers == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( allUsers.size() );
        for ( UserResponseDto userResponseDto : allUsers ) {
            list.add( userResponseDtoToEntity( userResponseDto ) );
        }

        return list;
    }

    private String userCredentialsUsername(User user) {
        if ( user == null ) {
            return null;
        }
        Credentials credentials = user.getCredentials();
        if ( credentials == null ) {
            return null;
        }
        String username = credentials.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }

    protected ProfileDto profileToProfileDto(Profile profile) {
        if ( profile == null ) {
            return null;
        }

        ProfileDto profileDto = new ProfileDto();

        profileDto.setFirstName( profile.getFirstName() );
        profileDto.setLastName( profile.getLastName() );
        profileDto.setEmail( profile.getEmail() );
        profileDto.setPhone( profile.getPhone() );

        return profileDto;
    }
}
