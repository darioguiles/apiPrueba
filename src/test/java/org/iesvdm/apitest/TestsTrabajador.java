package org.iesvdm.apitest;

import jakarta.transaction.Transactional;
import org.iesvdm.apitest.domain.Trabajador;
import org.iesvdm.apitest.domain.Usuario;
import org.iesvdm.apitest.repository.TrabajadorRepository;
import org.iesvdm.apitest.repository.UsuarioRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestsTrabajador {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    TrabajadorRepository trabajadorRepository;

    @Test @Order(1)
    void crearTrabajador() {
        // CREAMOS USUARIOS
        Usuario u = new Usuario(0, "usuario1","u@a.com","password","",false,null,null);
        usuarioRepository.save(u);
        Usuario u2 = new Usuario(0,"usuario2","usuario2@a.com","password","",false,null,null);
        usuarioRepository.save(u2);

        // CREAMOS TRABAJADORES

        Trabajador t = new Trabajador(0, "Dario","Guiles","Primer Trabajador","610",null);
        trabajadorRepository.save(t);
        Trabajador t2 = new Trabajador(0, "Juan","Cuesta","Trabajador2","640",null);
        trabajadorRepository.save(t2);

        //RELACION 1-1

        u.setTrabajador(t);
        t.setUsuario(u);
        u2.setTrabajador(t2);
        t2.setUsuario(u2);

        usuarioRepository.save(u);
        usuarioRepository.save(u2);
        trabajadorRepository.save(t);
        trabajadorRepository.save(t2);
    }
    @Test @Order(2)
    void desvincularTrabajador()
    {

    }
}
