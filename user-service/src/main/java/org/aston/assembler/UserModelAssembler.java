package org.aston.assembler;

import org.aston.controller.UserController;
import org.aston.dto.UserDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDTO, UserDTO> {
    @Override
    public @NonNull UserDTO toModel(@NonNull UserDTO userDTO) {
        userDTO.add(linkTo(methodOn(UserController.class).getUser(userDTO.getId())).withSelfRel());
        userDTO.add(linkTo(methodOn(UserController.class).getAll()).withRel("users"));
        userDTO.add(linkTo(methodOn(UserController.class).updateUser(userDTO, userDTO.getId())).withRel("update"));
        userDTO.add(linkTo(methodOn(UserController.class).delete(userDTO.getId())).withRel("delete"));
        return userDTO;
    }
}
