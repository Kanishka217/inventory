package com.ys.controller;

import lombok.Data;

// One shared shape for login, register, and update-settings request bodies —
// simpler than 3 separate classes since the fields overlap.
@Data
public class AuthRequest {
    private String username;
    private String password;
    private String firmName;
    private String propName;
    private String address;
    private String phone;
    private String gstin;
    private String email;
}
