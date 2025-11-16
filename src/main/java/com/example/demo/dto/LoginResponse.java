package com.example.demo.dto; // o package com.example.demo.dto;

import java.util.Set;

public class LoginResponse {
  public Long id;
  public String username;
  public Set<String> roles;
  public String token;

  public LoginResponse() {
  }

  public LoginResponse(Long id, String username, Set<String> roles, String token) {
    this.id = id;
    this.username = username;
    this.roles = roles;
    this.token = token;
  }
}
