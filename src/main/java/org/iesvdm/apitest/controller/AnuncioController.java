package org.iesvdm.apitest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.AnuncioEmpresa;
import org.iesvdm.apitest.domain.AnuncioTrabajador;
import org.iesvdm.apitest.service.AnuncioEmpresaService;
import org.iesvdm.apitest.service.AnuncioTrabajadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/v1/data-api/anuncios")
public class AnuncioController {

    @Autowired
    private AnuncioTrabajadorService anuncioTrabajadorService;

    @Autowired
    private AnuncioEmpresaService anuncioEmpresaService;

    // Endpoint para obtener anuncios de empresas con filtros de búsqueda y ordenación
    @GetMapping("/empresas")
    public ResponseEntity<Map<String, Object>> getAnunciosEmpresas(
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanio", defaultValue = "5") int tamanio) {

        // Llamar al servicio con los filtros
        Map<String, Object> anuncios = anuncioEmpresaService.findAll(pagina, tamanio);

        return ResponseEntity.ok(anuncios);
    }

    // Endpoint para obtener anuncios de trabajadores con filtros de búsqueda y ordenación
    @GetMapping("/trabajadores")
    public ResponseEntity<Map<String, Object>> getAnunciosTrabajadores(
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanio", defaultValue = "5") int tamanio) {

        // Llamar al servicio con los filtros
        Map<String, Object> anuncios = anuncioTrabajadorService.findAll(pagina, tamanio);
        return ResponseEntity.ok(anuncios);
    }

}
