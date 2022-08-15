package com.cooksys.socialmedia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//TODO: Check if this makes sense to have both all args and no args annotated 
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorDto {
	
	private String message;

}
