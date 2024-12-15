package org.iesvdm.apitest.service;

import org.iesvdm.apitest.domain.Usuario;
import org.iesvdm.apitest.exception.UsuarioNotFoundException;
import org.iesvdm.apitest.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){ this.usuarioRepository= usuarioRepository;}

    public List<Usuario> all() {
        return this.usuarioRepository.findAll();
    }


    public Usuario save(Usuario usuario) {
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
        // Verificar si el dato ingresado es un nombre de usuario
        Usuario usuario = usuarioRepository.findByNomUsuario(nomUsuarioCorreo).orElse(null);

        if (usuario != null && usuario.getContrasenia().equals(password)) {
            return usuario; // Si las credenciales coinciden
        }

        return null; // Si no se encuentran coincidencias
    }

    public Usuario validarCredencialesPorCorreo(String nomUsuarioCorreo, String password) {
        // Verificar si el dato ingresado es un nombre de usuario
        Usuario usuario = usuarioRepository.findByCorreo(nomUsuarioCorreo).orElse(null);

        if (usuario != null && usuario.getContrasenia().equals(password)) {
            return usuario; // Si las credenciales coinciden
        }

        return null; // Si no se encuentran coincidencias
    }



}
