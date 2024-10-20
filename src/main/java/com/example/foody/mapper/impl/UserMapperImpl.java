package com.example.foody.mapper.impl;

import com.example.foody.builder.UserBuilder;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.CreditCard;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.User;
import com.example.foody.utils.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapperImpl implements UserMapper {
    private final UserBuilder userBuilder;

    public UserMapperImpl(UserBuilder userBuilder) {
        this.userBuilder = userBuilder;
    }

    @Override
    public UserResponseDTO userToUserResponseDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setCreditCardId( userCreditCardId( user ) );
        userResponseDTO.setId( user.getId() );
        userResponseDTO.setEmail( user.getEmail() );
        userResponseDTO.setPassword( user.getPassword() );
        userResponseDTO.setName( user.getName() );
        userResponseDTO.setSurname( user.getSurname() );
        userResponseDTO.setBirthDate( user.getBirthDate() );
        userResponseDTO.setPhoneNumber( user.getPhoneNumber() );
        userResponseDTO.setAvatar( user.getAvatar() );
        if ( user.getRole() != null ) {
            userResponseDTO.setRole( user.getRole().name() );
        }
        userResponseDTO.setActive( user.isActive() );

        return userResponseDTO;
    }

    @Override
    public User userRequestDTOToUser(UserRequestDTO userRequestDTO) {
        if ( userRequestDTO == null ) {
            return null;
        }

        UserBuilder builder = userBuilder
                .email( userRequestDTO.getEmail() )
                .password( userRequestDTO.getPassword() )
                .name( userRequestDTO.getName() )
                .surname( userRequestDTO.getSurname() )
                .birthDate( userRequestDTO.getBirthDate() )
                .phoneNumber( userRequestDTO.getPhoneNumber() )
                .avatar( userRequestDTO.getAvatar() );

        if ( userRequestDTO.getRole() != null ) {
            builder.role( Enum.valueOf( Role.class, userRequestDTO.getRole() ) );
        }

        User user = builder.build();

        return user;
    }

    @Override
    public List<UserResponseDTO> usersToUserResponseDTOs(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDTO> list = new ArrayList<UserResponseDTO>( users.size() );
        for ( User user : users ) {
            list.add( userToUserResponseDTO( user ) );
        }

        return list;
    }

    private Long userCreditCardId(User user) {
        if ( user == null ) {
            return null;
        }
        CreditCard creditCard = user.getCreditCard();
        if ( creditCard == null ) {
            return null;
        }
        long id = creditCard.getId();
        return id;
    }
}
