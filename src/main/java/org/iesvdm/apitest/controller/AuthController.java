package org.iesvdm.apitest.controller;

import jakarta.validation.Valid;
import org.iesvdm.apitest.domain.LoginRequest;
import org.iesvdm.apitest.domain.MessageResponse;
import org.iesvdm.apitest.domain.RegisterRequest;
import org.iesvdm.apitest.domain.Usuario;
import org.iesvdm.apitest.repository.UsuarioRepository;
import org.iesvdm.apitest.security.TokenUtils;
import org.iesvdm.apitest.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TokenUtils tokenUtils;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();

        response.put("token", token);
        response.put("id", userDetails.getId());
        response.put("username", userDetails.getUsername());
        response.put("email", userDetails.getEmail());
        response.put("roles", roles);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByNomUsuario(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username ya en uso!"));
        }

        if (userRepository.existsByCorreo(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email ya en uso!"));
        }

        // Create new user's account
        Usuario user = new Usuario(registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRoles();
        boolean esAdmin = false;

        if (strRoles == null) {
            String userRole = userRepository.findByCorreo()
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            esAdmin=userRole;
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Rol adminRole = rolRepository.findByRol(ERol.ROL_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(adminRole);

                        break;

                    default:
                        Rol userRole = rolRepository.findByRol(ERol.ROL_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado correctamente!"));
    }


}
