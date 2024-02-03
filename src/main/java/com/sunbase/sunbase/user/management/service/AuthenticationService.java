package com.sunbase.sunbase.user.management.service;

import com.sunbase.sunbase.user.management.dto.AuthRequest;
import com.sunbase.sunbase.user.management.dto.AuthResponse;
import com.sunbase.sunbase.user.management.dto.RegistrationRequest;

public interface AuthenticationService {

    AuthResponse register(RegistrationRequest request);

    AuthResponse authenticate(AuthRequest request);
}
