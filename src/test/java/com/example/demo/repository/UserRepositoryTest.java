package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.model.User;
import com.example.demo.model.Role;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        Role rolUser = Role.builder().name("USER").build();
        Role rolAdmin = Role.builder().name("ADMIN").build();

        user1 = User.builder()
            .username("juan")
            .password("1234")
            .roles(Set.of(rolUser))
            .build();

        user2 = User.builder()
            .username("admin")
            .password("abcd")
            .roles(Set.of(rolAdmin))
            .build();

        userRepository.save(user1);
        userRepository.save(user2);
    }

    // PRUEBA CREATE
    @Test
    void testCreateUser() {
        // Arrange
        User nuevo = User.builder()
            .username("luis")
            .password("pass")
            .roles(Set.of(Role.builder().name("USER").build()))
            .build();

        // Act
        User guardado = userRepository.save(nuevo);

        // Assert
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getUsername()).isEqualTo("luis");
    }

    @Test
    void testReadUserById() {
        Optional<User> encontrado = userRepository.findById(user1.getId());
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getUsername()).isEqualTo("juan");
    }

    @Test
    void testUpdateUser() {
        user1.setPassword("9999");
        User actualizado = userRepository.save(user1);
        assertThat(actualizado.getPassword()).isEqualTo("9999");
    }

    @Test
    void testDeleteUser() {
        Long id = user2.getId();
        userRepository.delete(user2);
        Optional<User> eliminado = userRepository.findById(id);
        assertThat(eliminado).isEmpty();
    }

    @Test
    void testBuscarPorNombreDeUsuario() {
        Optional<User> encontrado = userRepository.findByUsername("juan");
        assertThat(encontrado).isPresent();

        User usuario = encontrado.get();
        boolean tieneRolUser = usuario.getRoles().stream()
                .anyMatch(r -> r.getName().equals("USER"));

        assertThat(tieneRolUser).isTrue();
    }

    @Test
    void testBuscarNombreYContrasenaValida() {
        Optional<User> encontrado = userRepository.findByUsername("admin");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getPassword()).isEqualTo("abcd");
    }

    @Test
    void testBuscarNombreYContrasenaInvalida() {
        Optional<User> encontrado = userRepository.findByUsername("juan");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getPassword()).isNotEqualTo("wrong");
    }

    @Test
    void testObtenerTodosLosUsuarios() {
        List<User> usuarios = userRepository.findAll();
        assertThat(usuarios).hasSize(2);
    }

    @Test
    void testGuardarVariosUsuarios() {
        User u1 = User.builder()
            .username("carlos")
            .password("x1")
            .roles(Set.of(Role.builder().name("USER").build()))
            .build();

        User u2 = User.builder()
            .username("ana")
            .password("x2")
            .roles(Set.of(Role.builder().name("USER").build()))
            .build();

        userRepository.save(u1);
        userRepository.save(u2);

        List<User> todos = userRepository.findAll();
        assertThat(todos).hasSize(4);
    }
}
