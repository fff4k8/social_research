package com.f4k8.socialresearch.model;

import java.io.Serializable;

/**Dto user projection that stored in cache*/

public class UserDto implements Serializable {
    private String email;
    private String password;

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
