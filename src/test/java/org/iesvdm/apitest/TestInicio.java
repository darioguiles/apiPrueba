package org.iesvdm.apitest;

import org.iesvdm.apitest.domain.Empresa;
import org.iesvdm.apitest.domain.Trabajador;
import org.iesvdm.apitest.domain.Usuario;
import org.iesvdm.apitest.repository.*;
import org.iesvdm.apitest.service.AnuncioTrabajadorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    AnuncioEmpresaRepository anuncioEmpresaRepository;
    @Autowired
    AnuncioTrabajadorRepository anuncioTrabajadorRepository;


    Usuario u1;
    Usuario u2;
    Trabajador t;
    Empresa e;
    /*
     * Ejemplo de implementacion, tenemos la clase usuario vacia,
     * y cuando alguien utiliza la aplicacion
     * y crea un usuario
     * */

    /*
    * Para los test tenemos lo siguiente:
    *



    * */

    @BeforeEach
    void inicioClases() {

        u1 = new Usuario(0, "usuario1","u@a.com","password","",false,null,null);
        usuarioRepository.save(u1);
         u2 = new Usuario(0, "Mr.Oracle","u@a.com","password","",false,null,null);
        usuarioRepository.save(u2);
         t = new Trabajador( "Dario","Guiles","Primer Trabajador","610",u1);
        trabajadorRepository.save(t);
         e = new Empresa( "Oracle","Empresa de desarrollo de software","+408",u2);
        empresaRepository.save(e);

        u1.setTrabajador(t);
        u2.setEmpresa(e);
        usuarioRepository.save(u1);
        trabajadorRepository.save(t);
        usuarioRepository.save(u2);
        empresaRepository.save(e);
    }
    @AfterEach
    void borradoClases() {
        usuarioRepository.delete(u1);
        usuarioRepository.delete(u2);
    }

    /**
     * @author Dario Guiles
     * Bateria de test que comprueba la relación usuario Trabajador
     * Usando una ya asociada y jugando con varias
     * */
    @Test
    void testeoUsuarioTrabajador() {

    }

    @Test
    void testeoUsuarioEmpresa() {


    }

    @Test
    void testeoAnuncioTConTrabajadorYEmpresa() {


    }

    @Test
    void testeoAnuncioEConTrabajadorYEmpresa() {


    }
}

