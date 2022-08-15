package com.cooksys.socialmedia.mappers;

import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

    TweetResponseDto entityToDto(Tweet tweet);

//    @Mapping(target = "username", source = "credentials.username")
    List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets);

	Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);

	Tweet responseDtoToEntity(TweetResponseDto createTweet);
	
}
