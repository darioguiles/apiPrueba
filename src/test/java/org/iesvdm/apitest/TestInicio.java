package org.iesvdm.apitest;

import org.iesvdm.apitest.domain.*;
import org.iesvdm.apitest.repository.*;
import org.iesvdm.apitest.service.AnuncioTrabajadorService;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    /**
     * Configuración inicial para los tests.
     * Este método crea dos usuarios, un trabajador y una empresa asociados a ellos.
     */
    @BeforeEach
    void setup() {
        //Limpiamos
        anuncioEmpresaRepository.deleteAll();
        anuncioTrabajadorRepository.deleteAll();
        trabajadorRepository.deleteAll();
        empresaRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Crear usuarios
        Usuario u1 = new Usuario(0, "usuario1", "u@a.com", "password", "", false, null, null);
        Usuario u2 = new Usuario(0, "Mr.Oracle", "u@a1.com", "password", "", false, null, null);
        usuarioRepository.save(u1);
        usuarioRepository.save(u2);

        // Crear trabajador asociado a usuario u1
        Trabajador trabajador = new Trabajador("Dario", "Guiles", "Primer Trabajador", "610", u1);
        trabajadorRepository.save(trabajador);

        // Crear empresa asociada a usuario u2
        Empresa empresa = new Empresa("Oracle", "Empresa de desarrollo de software", "+408", u2);
        empresaRepository.save(empresa);

        // Actualizar relaciones
        u1.setTrabajador(trabajador);
        u2.setEmpresa(empresa);
        usuarioRepository.save(u1);
        usuarioRepository.save(u2);
    }

    /**
     * Test para verificar la relación entre Usuario y Trabajador.
     */
    @Test
    void testUsuarioTrabajador() {
        // Obtener el usuario creado en el setup
        Usuario u1 = usuarioRepository.findByNomUsuario("usuario1").orElse(null);
        assert u1 != null;

        // Verificar que el usuario tiene un trabajador asociado
        assert u1.getTrabajador() != null;
        assert u1.getTrabajador().getNombre().equals("Dario");
    }

    /**
     * Test para verificar la relación entre Usuario y Empresa.
     */
    @Test
    void testUsuarioEmpresa() {
        // Obtener el usuario creado en el setup
        Usuario u2 = usuarioRepository.findByNomUsuario("Mr.Oracle").orElse(null);
        assert u2 != null;

        // Verificar que el usuario tiene una empresa asociada
        assert u2.getEmpresa() != null;
        assert u2.getEmpresa().getNombre().equals("Oracle");
    }


    /**
     * Test para verificar la relación entre AnuncioTrabajador y Trabajador.



     */
    @Test
    void testAnuncioTrabajador() {
        // Obtener el trabajador del setup
        Trabajador trabajador = trabajadorRepository.findByUsuarioCorreo("u@a.com").orElse(null);
        assert trabajador != null;

        // Crear un anuncio para el trabajador
        AnuncioTrabajador anuncio = new AnuncioTrabajador(0, new Date(), "Esto es un anuncio de un Trabajador", trabajador, null);
        anuncioTrabajadorRepository.save(anuncio);

        // Verificar que el anuncio se creó y está asociado al trabajador
        trabajador = trabajadorRepository.findByUsuarioCorreo("u@a.com").orElse(null);
        assert trabajador != null;
        assert trabajador.getAnunciosTrabajador().contains(anuncio);
    }

    /**
     * Test para verificar la relación entre AnuncioEmpresa y Empresa.
     @Test
     void testAnuncioEmpresa() {
     // Obtener la empresa del setup
     Empresa empresa = empresaRepository.findByUsuarioCorreo("u@a1.com").orElse(null);
     assert empresa != null;

     // Crear un anuncio para la empresa
     AnuncioEmpresa anuncio = new AnuncioEmpresa(0, "Esto es un anuncio de una Empresa", 5, 0, new Date(), null, empresa, null);
     anuncioEmpresaRepository.save(anuncio);

     // Verificar que el anuncio se creó y está asociado a la empresa
     empresa = empresaRepository.findByUsuarioCorreo("u@a1.com").orElse(null);
     assert empresa != null;
     assert empresa.getAnuncioEmpresas().contains(anuncio);
     }
     */


    /**
     * Test para verificar la eliminación en cascada de Anuncios al eliminar Trabajadores o Empresas.
     @Test
     void testEliminarCascada() {
     // Obtener el trabajador y la empresa del setup
     Trabajador trabajador = trabajadorRepository.findById(1L).orElse(null);
     Empresa empresa = empresaRepository.findById(2L).orElse(null);
     assert trabajador != null;
     assert empresa != null;

     // Crear anuncios asociados
     AnuncioTrabajador anuncioT = new AnuncioTrabajador(0, new Date(), "Anuncio de prueba", trabajador, null);
     AnuncioEmpresa anuncioE = new AnuncioEmpresa(0, "Otro anuncio", 10, 0, new Date(), null, empresa, null);
     anuncioTrabajadorRepository.save(anuncioT);
     anuncioEmpresaRepository.save(anuncioE);

     // Eliminar trabajador y empresa
     trabajadorRepository.delete(trabajador);
     empresaRepository.delete(empresa);

     // Verificar que los anuncios asociados también fueron eliminados
     assert anuncioTrabajadorRepository.findById(anuncioT.getIdAnuncioTrabajador()).isEmpty();
     assert anuncioEmpresaRepository.findById(anuncioE.getIdAnuncioEmpresa()).isEmpty();
     }
     */


}

