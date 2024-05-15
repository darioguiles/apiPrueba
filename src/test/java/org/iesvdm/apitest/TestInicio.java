package org.iesvdm.apitest;

import org.iesvdm.apitest.domain.*;
import org.iesvdm.apitest.repository.*;
import org.iesvdm.apitest.service.AnuncioTrabajadorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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


    /*
     * Ejemplo de implementacion, tenemos la clase usuario vacia,
     * y cuando alguien utiliza la aplicacion
     * y crea un usuario
     * */

    /*
    * Para los test tenemos lo siguiente:
    *
     @BeforeEach
    void inicioClases() {

        Usuario u1 = new Usuario(0, "usuario1","u@a.com","password","",false,null,null);
        usuarioRepository.save(u1);
        Usuario  u2 = new Usuario(0, "Mr.Oracle","u@a1.com","password","",false,null,null);
        usuarioRepository.save(u2);
        Trabajador  t = new Trabajador( "Dario","Guiles","Primer Trabajador","610",u1);
        trabajadorRepository.save(t);
        Empresa  e = new Empresa( "Oracle","Empresa de desarrollo de software","+408",u2);
        empresaRepository.save(e);

        u1.setTrabajador(t);
        u2.setEmpresa(e);
        usuarioRepository.save(u1);
        trabajadorRepository.save(t);
        usuarioRepository.save(u2);
        empresaRepository.save(e);
    }
    * */

    //Todo Hacer que funcione Delete de usuario como se tiene entendido

    //Todo Que al borrar una empresa o trabajador se eliminen también los anuncios creados

    /**
     * @author Dario Guiles
     * Bateria de test que comprueba la relación usuario Trabajador
     * Usando una ya asociada y jugando con varias
     * */
    @Test
    void testeoUsuarioTrabajador() {
        /*
        Usuario u1 = new Usuario(0, "usuario1","u@a.com","password","",false,null,null);
        usuarioRepository.save(u1);
        Usuario  u2 = new Usuario(0, "Mr.Oracle","u@a1.com","password","",false,null,null);
        usuarioRepository.save(u2);
        Trabajador  t = new Trabajador( "Dario","Guiles","Primer Trabajador","610",u1);
        trabajadorRepository.save(t);
        Empresa  e = new Empresa( "Oracle","Empresa de desarrollo de software","+408",u2);
        empresaRepository.save(e);

        u1.setTrabajador(t);
        u2.setEmpresa(e);
        usuarioRepository.save(u1);
        trabajadorRepository.save(t);
        usuarioRepository.save(u2);
        empresaRepository.save(e);
        */
    }

    @Test
    void testeoUsuarioEmpresa() {
/*
        Usuario u1 = new Usuario(0, "usuario1","u@a.com","password","", false,null,null);
        usuarioRepository.save(u1);
        Usuario  u2 = new Usuario(0, "Mr.Oracle","u@a1.com","password","",false,null,null);
        usuarioRepository.save(u2);
        Trabajador  t = new Trabajador( "Dario","Guiles","Primer Trabajador","610",u1);
        trabajadorRepository.save(t);
        Empresa  e = new Empresa( "Oracle","Empresa de desarrollo de software","+408",u2);
        empresaRepository.save(e);

        u1.setTrabajador(t);
        u2.setEmpresa(e);
        usuarioRepository.save(u1);
        trabajadorRepository.save(t);
        usuarioRepository.save(u2);
        empresaRepository.save(e);

 */
    }

    @Test
    @Transactional
    void testeoAnuncioTConTrabajadorYEmpresa() {
        /*
        //Sacar el trabajador con el id correspondiente en este caso es el 1 pero podria ser otro
        Trabajador t = trabajadorRepository.getReferenceById(1L);
        Empresa e = empresaRepository.getReferenceById(2L);
        AnuncioTrabajador anuncioT = new AnuncioTrabajador("Esto es un anuncio de un Trabajador");
        anuncioTrabajadorRepository.save(anuncioT);
        AnuncioEmpresa anuncioE = new AnuncioEmpresa("Esto es un anuncio de una Empresa con 5 puestos y " +
                "que no tiene fin",5);
        anuncioEmpresaRepository.save(anuncioE);

        //Logica de los anunciosT
        anuncioT.setTrabajador(t);
        anuncioTrabajadorRepository.save(anuncioT);
        t.getAnunciosTrabajador().add(anuncioT);
        trabajadorRepository.save(t);

        //Logica de los anunciosE
        anuncioE.setEmpresa(e);
        anuncioEmpresaRepository.save(anuncioE);
        e.getAnuncioEmpresas().add(anuncioE);
        empresaRepository.save(e);

         */
        /*
        * TODO revisar el funcionamiento de Empresa-Anuncio puesto que no se pone el Id al igual que en Trabajador-Anuncio
        * */


    }

    @Test
    void testeoAnuncioEConTrabajadorYEmpresa() {
        //usuarioRepository.deleteAll();
        //trabajadorRepository.deleteAll();
        //empresaRepository.deleteAll();


    }
}

