package com.cooksys.socialmedia.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TweetRequestDto {
	
	private String content;
	
	private CredentialsDto credentials;
	
}
