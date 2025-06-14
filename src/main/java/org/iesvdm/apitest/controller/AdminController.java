package org.iesvdm.apitest.controller;

import org.iesvdm.apitest.domain.Usuario;
import org.iesvdm.apitest.service.AnuncioEmpresaService;
import org.iesvdm.apitest.service.AnuncioTrabajadorService;
import org.iesvdm.apitest.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/data-api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final UsuarioService usuarioService;
    private final AnuncioEmpresaService anuncioEmpresaService;
    private final AnuncioTrabajadorService anuncioTrabajadorService;

    public AdminController(UsuarioService usuarioService, AnuncioEmpresaService anuncioEmpresaService,
                           AnuncioTrabajadorService anuncioTrabajadorService) {
        this.usuarioService = usuarioService;
        this.anuncioEmpresaService = anuncioEmpresaService;
        this.anuncioTrabajadorService = anuncioTrabajadorService;
    }

    @GetMapping("/dashboard/usuarios")
    public ResponseEntity<Map<String, Object>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.all();
        Map<String, Object> response = new HashMap<>();
        response.put("usuarios", usuarios);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/empresa")
    public ResponseEntity<Map<String, Object>> getAllAnunciosEmpresas() {
        List<Map<String, Object>> anunciosEmpresas = anuncioEmpresaService.allF();

        Map<String, Object> response = new HashMap<>();
        response.put("anuncioEmpresa", anunciosEmpresas);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/trabajador")
    public ResponseEntity<Map<String, Object>> getAllAnunciosTrabajadores() {
        List<Map<String, Object>> anunciosTrabajadores = anuncioTrabajadorService.allF();

        Map<String, Object> response = new HashMap<>();
        response.put("anuncioTrabajador", anunciosTrabajadores);

        return ResponseEntity.ok(response);
    }

}
