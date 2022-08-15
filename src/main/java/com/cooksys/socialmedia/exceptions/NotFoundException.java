package com.cooksys.socialmedia.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException {/**
	 * 
	 */
	private static final long serialVersionUID = -6496940574625196392L;
	
	private String message;

}
