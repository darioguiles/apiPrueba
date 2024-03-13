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

        u1.setTrabajador(t);
        t.setUsuario(u1);
        u2.setEmpresa(e);
        e.setUsuario(u2);
        usuarioRepository.save(u1);
        trabajadorRepository.save(t);
        usuarioRepository.save(u2);
        empresaRepository.save(e);
                                                                                                                        


        /*

        Usuario u1 = new Usuario(0, "usuario1","u@a.com","password","",false,null,null);
        usuarioRepository.save(u1);
        Usuario u2 = new Usuario(0, "Mr.Oracle","u@a.com","password","",false,null,null);
        usuarioRepository.save(u2);

        Trabajador t = new Trabajador(0, "Dario","Guiles","Primer Trabajador","610",null);
        trabajadorRepository.save(t);

        Trabajador t1 = new Trabajador(0, "Dario2","Guiles","Primer Trabajador","610",null);
        trabajadorRepository.save(t1);

        u1.setTrabajador(t1);
        usuarioRepository.save(u1);

        t1.setUsuario(u1);
        trabajadorRepository.save(t1);

        u2.setTrabajador(t);
        usuarioRepository.save(u2);

        t.setUsuario(u2);
        trabajadorRepository.save(t);
        */

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
    }
}

