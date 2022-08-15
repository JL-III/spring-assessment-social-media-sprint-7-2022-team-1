package com.cooksys.socialmedia.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException {/**
	 * 
	 */
	private static final long serialVersionUID = -5552076667106114422L;
	
	private String message;

}
