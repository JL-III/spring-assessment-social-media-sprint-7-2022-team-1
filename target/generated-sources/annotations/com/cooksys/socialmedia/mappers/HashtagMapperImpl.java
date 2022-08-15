package com.cooksys.socialmedia.mappers;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.entities.Hashtag;
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
public class HashtagMapperImpl implements HashtagMapper {

    @Override
    public HashtagDto entityToDto(Hashtag hashtag) {
        if ( hashtag == null ) {
            return null;
        }

        HashtagDto hashtagDto = new HashtagDto();

        hashtagDto.setLabel( hashtag.getLabel() );
        hashtagDto.setFirstUsed( hashtag.getFirstUsed() );
        hashtagDto.setLastUsed( hashtag.getLastUsed() );

        return hashtagDto;
    }

    @Override
    public List<HashtagDto> entitiesToDtos(List<Hashtag> hashtags) {
        if ( hashtags == null ) {
            return null;
        }

        List<HashtagDto> list = new ArrayList<HashtagDto>( hashtags.size() );
        for ( Hashtag hashtag : hashtags ) {
            list.add( entityToDto( hashtag ) );
        }

        return list;
    }

    @Override
    public Hashtag stringToEntity(String string) {
        if ( string == null ) {
            return null;
        }

        Hashtag hashtag = new Hashtag();

        return hashtag;
    }
}
