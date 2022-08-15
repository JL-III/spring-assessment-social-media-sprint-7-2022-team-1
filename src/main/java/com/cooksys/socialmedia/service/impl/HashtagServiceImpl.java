package com.cooksys.socialmedia.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.exceptions.NotFoundException;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.services.HashtagService;
import com.cooksys.socialmedia.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;
    private final TweetServiceImpl tweetServiceImpl;
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final ValidateService validateService;

    public Hashtag validateHashtag(String label) {
        if (!validateService.tagExists(label)) {
            throw new NotFoundException("The hashtag #" + label + " does not exist.");
        }
        return hashtagRepository.findByLabel(label).get();
    }

    @Override
    public List<HashtagDto> getAllHashtags() {
        return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
    }

    @Override
    public List<TweetResponseDto> getTweetsWithHashtag(String label) {
        validateHashtag(label);
        List<Tweet> tweetsToReturn = new ArrayList<>();
        
        for (Tweet tweet : tweetRepository.findAllByDeletedFalse()) {
        	for (HashtagDto hashtagdto : tweetServiceImpl.getTweetHashtags(tweet.getId())) {
        		if (hashtagdto.getLabel().equals(label)) {
        			tweetsToReturn.add(tweet);
        		}
        	}
        }
        return tweetMapper.entitiesToDtos(tweetsToReturn);
    }
}
