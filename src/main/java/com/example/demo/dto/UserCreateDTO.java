package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    private String username;
    private String email;
    private String password;
    private String role; // nombres de los roles, ej: ["CLIENTE", "OPERADOR"]

}
