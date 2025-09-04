package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS_TABLE")
public class User {
    
    private String username;
    private String password;
    private String role; 

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Comida> comidas = new ArrayList<>();

    public User(Long id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(){

    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() { 
        return role; 
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Comida> getComidas() {
        return comidas;
    }

    public void setComidas(List<Comida> comidas) {
        this.comidas = comidas;
    }

    public void setRole(String role) { 
        this.role = role; 
    }

    
}
