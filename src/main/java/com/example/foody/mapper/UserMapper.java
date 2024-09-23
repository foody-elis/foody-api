package com.example.foody.mapper;

import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "creditCard.id", target = "creditCardId")
    UserResponseDTO userToUserResponseDTO(User user);

    @InheritInverseConfiguration
    User userRequestDTOToUser(UserRequestDTO userRequestDTO);

    List<UserResponseDTO> usersToUserResponseDTOs(List<User> users);
}
