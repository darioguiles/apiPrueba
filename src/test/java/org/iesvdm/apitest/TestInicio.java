package org.iesvdm.apitest;

import jakarta.annotation.PostConstruct;
import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;
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

import java.time.LocalDateTime;
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

    @PostConstruct
    public void init() {
        // Limpieza previa
        /*anuncioEmpresaRepository.deleteAll();
        anuncioTrabajadorRepository.deleteAll();
        trabajadorRepository.deleteAll();
        empresaRepository.deleteAll();
        usuarioRepository.deleteAll();*/

        // Usuario normal
        Usuario userNormal = new Usuario(0, "usuarioNormal", "normal@demo.com", "password", "", false, null, null);
        usuarioRepository.save(userNormal);

        // Usuario admin
        Usuario admin = new Usuario(0, "adminUser", "admin@demo.com", "password", "", true, null, null);
        usuarioRepository.save(admin);

        // Usuario trabajador
        Usuario userTrabajador = new Usuario(0, "trabajadorUser", "trabajador@demo.com", "password", "", false, null, null);
        usuarioRepository.save(userTrabajador);
        Trabajador trabajador = new Trabajador("Luis", "Fernández", "Desarrollador Java", "610101010", userTrabajador);
        trabajadorRepository.save(trabajador);
        userTrabajador.setTrabajador(trabajador);
        usuarioRepository.save(userTrabajador);

        // Usuario empresa
        Usuario userEmpresa = new Usuario(0, "empresaUser", "empresa@demo.com", "password", "", false, null, null);
        usuarioRepository.save(userEmpresa);
        Empresa empresa = new Empresa("TechCorp", "Empresa de IT", "+34123456789", userEmpresa);
        empresaRepository.save(empresa);
        userEmpresa.setEmpresa(empresa);
        usuarioRepository.save(userEmpresa);

        // Anuncios del trabajador
        for (int i = 1; i <= 2; i++) {
            AnuncioTrabajador anuncioT = new AnuncioTrabajador(0, LocalDateTime.now(), "Anuncio trabajador " + i, trabajador, null);
            anuncioTrabajadorRepository.save(anuncioT);
        }

        // Anuncios de la empresa
        for (int i = 1; i <= 2; i++) {
            AnuncioEmpresa anuncioE = new AnuncioEmpresa(0, "Oferta de empleo " + i, 8 + i, 0, LocalDateTime.now(), null, empresa, null);
            anuncioEmpresaRepository.save(anuncioE);
        }

    }

}

