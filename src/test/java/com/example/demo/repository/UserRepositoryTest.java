package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.model.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user1 = new User("juan", "1234", "USER");
        user1.setDireccion("Bogotá");
        user1.setTelefono("3001234567");

        user2 = new User("admin", "abcd", "ADMIN");
        user2.setDireccion("Medellín");
        user2.setTelefono("3109876543");

        userRepository.save(user1);
        userRepository.save(user2);
    }

    // PRUEBAS DEL CRUD DE USUARIOS

    @Test
    void testCreateUser() {
        // Arrange
        User nuevo = new User("luis", "pass", "USER");

        // Act
        User guardado = userRepository.save(nuevo);

        // Assert
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getUsername()).isEqualTo("luis");
    }

    @Test
    void testReadUserById() {
        // Arrange

        // Act
        Optional<User> encontrado = userRepository.findById(user1.getId());

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getUsername()).isEqualTo("juan");
    }

    @Test
    void testUpdateUser() {
        // Arrange
        user1.setPassword("9999");

        // Act
        User actualizado = userRepository.save(user1);

        // Assert
        assertThat(actualizado.getPassword()).isEqualTo("9999");
    }

    @Test
    void testDeleteUser() {
        // Arrange
        Long id = user2.getId();

        // Act
        userRepository.delete(user2);
        Optional<User> eliminado = userRepository.findById(id);

        // Assert
        assertThat(eliminado).isEmpty();
    }

    // Pruebas de cinco consultas

    @Test
    void testBuscarPorNombreDeUsuario() {
        // Arrange

        // Act
        User encontrado = userRepository.findByUsername("juan");

        // Assert
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getRole()).isEqualTo("USER");
    }

    @Test
    void testBuscarNombreYContrasenaValida() {
        // Arrange

        // Act
        Optional<User> encontrado = userRepository.findByUsernameAndPassword("admin", "abcd");

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getRole()).isEqualTo("ADMIN");
    }

    @Test
    void testBuscarNombreYContrasenaInvalida() {
        // Arrange
        String username = "juan";
        String password = "wrong";

        // Act
        Optional<User> encontrado = userRepository.findByUsernameAndPassword(username, password);

        // Assert
        assertThat(encontrado).isEmpty();
    }

    @Test
    void testObtenerTodosLosUsuarios() {
        // Arrange

        // Act
        List<User> usuarios = userRepository.findAll();

        // Assert
        assertThat(usuarios).hasSize(2);
    }

    @Test
    void testGuardarVariosUsuarios() {
        // Arrange
        User u1 = new User("carlos", "x1", "USER");
        User u2 = new User("ana", "x2", "USER");

        // Act
        userRepository.save(u1);
        userRepository.save(u2);
        List<User> todos = userRepository.findAll();

        // Assert
        assertThat(todos).hasSize(4);
    }
}
