package org.iesvdm.apitest.service;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.Usuario;
import org.iesvdm.apitest.exception.UsuarioNotFoundException;
import org.iesvdm.apitest.repository.EmpresaRepository;
import org.iesvdm.apitest.repository.TrabajadorRepository;
import org.iesvdm.apitest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    private final TrabajadorRepository trabajadorRepository;

    private final EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject the BCryptPasswordEncoder

    public UsuarioService(UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository,
                          TrabajadorRepository trabajadorRepository)
    { this.usuarioRepository= usuarioRepository;
    this.empresaRepository=empresaRepository;
    this.trabajadorRepository=trabajadorRepository;
    }

    public List<Usuario> all() {
        return this.usuarioRepository.findAll();
    }

    // Save a new user with a hashed password
    public Usuario save(Usuario usuario) {
        // Hash the password before saving
        if (!usuario.getContrasenia().startsWith("$2a$10$")) { // BCrypt genera contraseñas que empiezan con $2a$10$
            String hashedPassword = passwordEncoder.encode(usuario.getContrasenia());
            usuario.setContrasenia(hashedPassword);
        }

        return this.usuarioRepository.save(usuario);
    }

    public Usuario one(Long id) {
        return this.usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    public Usuario replace(Long id, Usuario usuario) {
        log.info("Intentando actualizar el usuario con ID: " + id);
        //log.info("Usuario" + usuario);
        return this.usuarioRepository.findById(id).map(usuarioExistente -> {
            // Solo actualizamos los campos no nulos del nuevo usuario
            if (usuario.getNomUsuario() != null) {
                usuarioExistente.setNomUsuario(usuario.getNomUsuario());
            }
            if (usuario.getCorreo() != null) {
                usuarioExistente.setCorreo(usuario.getCorreo());
            }
            if (usuario.getRutapfp() != null) {
                usuarioExistente.setRutapfp(usuario.getRutapfp());
            }
            if (usuario.getTrabajador() != null) {
                usuarioExistente.setTrabajador(usuario.getTrabajador());
                this.trabajadorRepository.save(usuario.getTrabajador());
            }
            if (usuario.getEmpresa() != null) {
                usuarioExistente.setEmpresa(usuario.getEmpresa());
                this.empresaRepository.save(usuario.getEmpresa());
            }

            // Guardar y devolver usuario actualizado

            return this.usuarioRepository.save(usuarioExistente);

        }).orElseThrow(() -> new UsuarioNotFoundException(id));

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
