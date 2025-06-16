package org.iesvdm.apitest;

import jakarta.annotation.PostConstruct;
import org.iesvdm.apitest.domain.*;
import org.iesvdm.apitest.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TrabajadorRepository trabajadorRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private AnuncioTrabajadorRepository anuncioTrabajadorRepository;
    @Autowired
    private AnuncioEmpresaRepository anuncioEmpresaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Evitar duplicados si ya existen
        if (usuarioRepository.findByCorreo("admin@demo.com").isPresent()) return;

        // Crear usuarios
        Usuario userNormal = new Usuario(0, "usuarioNormal", "normal@demo.com", passwordEncoder.encode("password"), "", false, null, null);
        Usuario adminUser = new Usuario(0, "adminUser", "admin@demo.com", passwordEncoder.encode("password"), "", true, null, null);
        Usuario userTrabajador = new Usuario(0, "trabajadorUser", "trabajador@demo.com", passwordEncoder.encode("password"), "", false, null, null);
        Usuario userEmpresa = new Usuario(0, "empresaUser", "empresa@demo.com", passwordEncoder.encode("password"), "", false, null, null);

        usuarioRepository.save(userNormal);
        usuarioRepository.save(adminUser);
        usuarioRepository.save(userTrabajador);
        usuarioRepository.save(userEmpresa);

        // Crear trabajador
        Trabajador trabajador = new Trabajador("Luis", "Fernández", "Desarrollador Java", "610101010", userTrabajador);
        trabajadorRepository.save(trabajador);
        userTrabajador.setTrabajador(trabajador);
        usuarioRepository.save(userTrabajador);

        // Crear empresa
        Empresa empresa = new Empresa("TechCorp", "Empresa de IT", "+34123456789", userEmpresa);
        empresaRepository.save(empresa);
        userEmpresa.setEmpresa(empresa);
        usuarioRepository.save(userEmpresa);

        // Anuncios trabajador
        for (int i = 1; i <= 2; i++) {
            AnuncioTrabajador anuncio = new AnuncioTrabajador(0, LocalDateTime.now().withNano(0), "Anuncio trabajador " + i, trabajador, null);
            anuncioTrabajadorRepository.save(anuncio);
        }

        // Anuncios empresa
        for (int i = 1; i <= 2; i++) {
            AnuncioEmpresa anuncio = new AnuncioEmpresa(0, "Oferta de empleo " + i, 8 + i, 0, LocalDateTime.now().withNano(0), null, empresa, null);
            anuncioEmpresaRepository.save(anuncio);
        }
    }
}
