package org.iesvdm.apitest.service;

import org.iesvdm.apitest.domain.Usuario;
import org.iesvdm.apitest.exception.UsuarioNotFoundException;
import org.iesvdm.apitest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;


    @Autowired
    private PasswordEncoder passwordEncoder; // Inject the BCryptPasswordEncoder

    public UsuarioService(UsuarioRepository usuarioRepository){ this.usuarioRepository= usuarioRepository;}

    public List<Usuario> all() {
        return this.usuarioRepository.findAll();
    }

    // Save a new user with a hashed password
    public Usuario save(Usuario usuario) {
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(usuario.getContrasenia());
        usuario.setContrasenia(hashedPassword);
        return this.usuarioRepository.save(usuario);
    }

    public Usuario one(Long id) {
        return this.usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    public Usuario replace(Long id, Usuario usuario) {

        return this.usuarioRepository.findById(id).map( p -> (id.equals(usuario.getIdUsuario())  ?
                        this.usuarioRepository.save(usuario) : null))
                .orElseThrow(() -> new UsuarioNotFoundException(id));

    }

    public void delete(Long id) {
        this.usuarioRepository.findById(id).map(p -> {this.usuarioRepository.delete(p);
                    return p;})
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    public Usuario validarCredenciales(String nomUsuarioCorreo, String password) {
        Usuario usuario = usuarioRepository.findByNomUsuario(nomUsuarioCorreo)
                .orElseGet(() -> usuarioRepository.findByCorreo(nomUsuarioCorreo).orElse(null));

        if (usuario != null && passwordEncoder.matches(password, usuario.getContrasenia())) {
            return usuario; // Credenciales correctas
        }


        return null; // Si no se encuentran coincidencias
    }



}
