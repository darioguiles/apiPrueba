package org.iesvdm.apitest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.AnuncioEmpresa;
import org.iesvdm.apitest.domain.AnuncioTrabajador;
import org.iesvdm.apitest.service.AnuncioEmpresaService;
import org.iesvdm.apitest.service.AnuncioTrabajadorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j //Anotacion para Log y ver los mensajes en terminal
@RestController //Anotacion que extiende las capacidades de Controller, proporcionando
//Compatibilidad con la Api Rest y la seriealizacion y deserializacion con Jackson
@CrossOrigin(origins = "http://localhost:4200") //<- Origen de la petición del frontend, hay que permitir la entrada
//desde el back para ir al front
@RequestMapping("/v1/data-api/anuncios")
public class AnuncioController {

    private final AnuncioTrabajadorService anuncioTrabajadorService;
    private final AnuncioEmpresaService anuncioEmpresaService;
    public AnuncioController(AnuncioTrabajadorService anuncioTrabajadorService, AnuncioEmpresaService anuncioEmpresaService ) {
        this.anuncioTrabajadorService = anuncioTrabajadorService;
        this.anuncioEmpresaService = anuncioEmpresaService;
    }


    @GetMapping("/todos")
    public ResponseEntity<Map<String, Object>> obtenerTodosLosAnuncios() {
        List<Map<String, Object>> anunciosEmpresas = anuncioEmpresaService.allF();
        List<Map<String, Object>> anunciosTrabajadores = anuncioTrabajadorService.allF();

        // Cargar relaciones trabajador y empresa para que se envíen en la respuesta


        Map<String, Object> response = new HashMap<>();
        response.put("anuncioEmpresa", anunciosEmpresas);
        response.put("anuncioTrabajador", anunciosTrabajadores);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/todos/filtrar")
    public ResponseEntity<Map<String, Object>> getAnunciosEmpresaFiltrados(
            @RequestParam(required = false, value = "descripcion") String descripcion,
            @RequestParam(required = false, value = "nombre") String nombre
    ) {

        List<Map<String, Object>> anunciosEmpresas = anuncioEmpresaService.allFiltered(descripcion,nombre);
        List<Map<String, Object>> anunciosTrabajadores = anuncioTrabajadorService.allFiltered(descripcion,nombre);

        // Cargar relaciones trabajador y empresa para que se envíen en la respuesta

        Map<String, Object> response = new HashMap<>();
        response.put("anuncioEmpresa", anunciosEmpresas);
        response.put("anuncioTrabajador", anunciosTrabajadores);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Map<String, Object>> obtenerTodosLosAnunciosId(@PathVariable("id") Long id) {
        List<Map<String, Object>> anunciosEmpresas = anuncioEmpresaService.allId(id);
        List<Map<String, Object>> anunciosTrabajadores = anuncioTrabajadorService.allId(id);

        // Cargar relaciones trabajador y empresa para que se envíen en la respuesta


        Map<String, Object> response = new HashMap<>();
        response.put("anuncioEmpresa", anunciosEmpresas);
        response.put("anuncioTrabajador", anunciosTrabajadores);

        return ResponseEntity.ok(response);
    }

}
