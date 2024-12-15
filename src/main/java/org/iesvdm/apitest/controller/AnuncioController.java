package org.iesvdm.apitest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.service.AnuncioEmpresaService;
import org.iesvdm.apitest.service.AnuncioTrabajadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Endpoint para obtener anuncios de trabajadores y empresas con paginación y filtro.
     *
     * @param soloTrabajadores Filtro para mostrar solo anuncios de trabajadores
     * @param soloEmpresas Filtro para mostrar solo anuncios de empresas
     * @param pagina Número de página para la paginación
     * @param tamanio Tamaño de página para la paginación
     * @return Anuncios filtrados y paginados
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAnuncios(
            @RequestParam(required = false, defaultValue = "false") boolean soloTrabajadores,
            @RequestParam(required = false, defaultValue = "false") boolean soloEmpresas,
            @RequestParam(required = false, defaultValue = "0") int pagina,
            @RequestParam(required = false, defaultValue = "5") int tamanio) {

        Map<String, Object> response = new HashMap<>();

        // Lógica para obtener anuncios de Trabajadores
        if (soloTrabajadores) {
            response = anuncioTrabajadorService.findAll(pagina, tamanio);
        }
        // Lógica para obtener anuncios de Empresas
        else if (soloEmpresas) {
            response = anuncioEmpresaService.findAll(pagina, tamanio);
        }
        // Si no se selecciona ningún filtro, se combinan los anuncios de ambos tipos
        else {
            List<Object> todosAnuncios = new ArrayList<>();

            // Obtener anuncios de trabajadores y empresas
            Map<String, Object> trabajadores = anuncioTrabajadorService.findAll(pagina, tamanio);
            Map<String, Object> empresas = anuncioEmpresaService.findAll(pagina, tamanio);

            // Agregar anuncios de trabajadores y empresas
            todosAnuncios.addAll((List<?>) trabajadores.get("anuncioTrabajador"));
            todosAnuncios.addAll((List<?>) empresas.get("anuncioEmpresa"));

            // Respuesta con ambos tipos de anuncios combinados
            response.put("anuncios", todosAnuncios);
            response.put("currentPage", trabajadores.get("currentPage"));
            response.put("totalItems", (int) trabajadores.get("totalItems") + (int) empresas.get("totalItems"));
            response.put("totalPages", (int) trabajadores.get("totalPages"));
        }

        return ResponseEntity.ok(response);
    }
}
