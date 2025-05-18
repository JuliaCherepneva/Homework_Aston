package org.aston.assembler;

import org.aston.controller.UserController;
import org.aston.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

class UserModelAssemblerTest {
    private UserModelAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = new UserModelAssembler();
    }

    @Test
    void toModel_shouldAddCorrectLinks() {
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setName("Test User");

        UserDTO result = assembler.toModel(user);

        assertThat(result.getLinks()).hasSize(4);

        assertThat(result.getLink("self")).isPresent()
                .get().extracting(Link::getHref)
                .isEqualTo(linkTo(methodOn(UserController.class).getUser(user.getId())).toUri().toString());

        assertThat(result.getLink("users")).isPresent()
                .get().extracting(Link::getHref)
                .isEqualTo(linkTo(methodOn(UserController.class).getAll()).toUri().toString());

        assertThat(result.getLink("update")).isPresent()
                .get().extracting(Link::getHref)
                .isEqualTo(linkTo(methodOn(UserController.class).updateUser(user, user.getId())).toUri().toString());

        assertThat(result.getLink("delete")).isPresent()
                .get().extracting(Link::getHref)
                .isEqualTo(linkTo(methodOn(UserController.class).delete(user.getId())).toUri().toString());
    }
}