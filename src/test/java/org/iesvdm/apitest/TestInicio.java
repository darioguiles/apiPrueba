package org.iesvdm.apitest;

import org.iesvdm.apitest.domain.Empresa;
import org.iesvdm.apitest.domain.Trabajador;
import org.iesvdm.apitest.domain.Usuario;
import org.iesvdm.apitest.repository.EmpresaRepository;
import org.iesvdm.apitest.repository.TrabajadorRepository;
import org.iesvdm.apitest.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestInicio {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    TrabajadorRepository trabajadorRepository;
    @Autowired
    EmpresaRepository empresaRepository;

    @Test
    void contextLoads() {


        Usuario u1 = new Usuario(0, "usuario1","u@a.com","password","",false,null,null);
        usuarioRepository.save(u1);
        Usuario u2 = new Usuario(0, "Mr.Oracle","u@a.com","password","",false,null,null);
        usuarioRepository.save(u2);
        Trabajador t = new Trabajador(0, "Dario","Guiles","Primer Trabajador","610",null);
        trabajadorRepository.save(t);
        Empresa e = new Empresa(0, "Oracle","Empresa de desarrollo de software","+408",null);
        empresaRepository.save(e);




    }
}

