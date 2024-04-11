package org.iesvdm.apitest.controller;


import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.AnuncioEmpresa;
import org.iesvdm.apitest.service.AnuncioEmpresaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j //Anotacion para Log y ver los mensajes en terminal
@RestController //Anotacion que extiende las capacidades de Controller, proporcionando
//Compatibilidad con la Api Rest y la seriealizacion y deserializacion con Jackson
@CrossOrigin(origins = "http://localhost:4200") //<- Origen de la petición del frontend, hay que permitir la entrada
//desde el back para ir al front
@RequestMapping("/v1/data-api/anuncioempresa")
public class AnuncioEmpresaController {

    private final AnuncioEmpresaService anuncioEmpresaService;

    public AnuncioEmpresaController(AnuncioEmpresaService anuncioEmpresaService) {
        this.anuncioEmpresaService = anuncioEmpresaService;
    }

    @GetMapping(value = {"","/"}, params = {"!buscar", "!ordenar", "!pagina", "!tamanio"})
    public List<AnuncioEmpresa> all() {
        log.info("Accediendo a todas los trabajadores");
        return this.anuncioEmpresaService.all();
        // TODO anuncios y los aplicados a este o quien lo hace ¿?

    }

    @GetMapping(value = {"","/"}, params = {"!pagina", "!tamanio"}) // <- Hace falta bloquear la paginación por esta ruta
    public List<AnuncioEmpresa> all(@RequestParam("buscar") Optional<String> buscarOpc
            , @RequestParam("ordenar") Optional<String> ordenarOpt) {
        log.info("Accediendo a todas los trabajadores con filtro buscar: %s y ordenar");
        buscarOpc.orElse("VOID");
        ordenarOpt.orElse("VOID");

        return this.anuncioEmpresaService.allByQueryFiltersStream(buscarOpc, ordenarOpt);
    }
    @GetMapping(value = {"","/"})
    public ResponseEntity<Map<String,Object>> all(@RequestParam( value = "pagina", defaultValue = "0") int pagina
            , @RequestParam(value = "tamanio" , defaultValue = "3") int tamanio) {
        log.info("Accediendo a todas las categorias con paginacion");

        Map<String, Object> responseAll = this.anuncioEmpresaService.findAll(pagina, tamanio);

        return ResponseEntity.ok(responseAll);
    }
    @PostMapping({"","/"})
    public AnuncioEmpresa newTrabajador(@RequestBody AnuncioEmpresa anuncioEmpresa) {
        return this.anuncioEmpresaService.save(anuncioEmpresa);
    }
    @GetMapping("/{id}")
    public AnuncioEmpresa one(@PathVariable("id") Long id) {
        return this.anuncioEmpresaService.one(id);
    }
    @PutMapping("/{id}")
    public AnuncioEmpresa replaceTrabajador(@PathVariable("id") Long id, @RequestBody AnuncioEmpresa anuncioEmpresa) {
        return this.anuncioEmpresaService.replace(id, anuncioEmpresa);
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTrabajador(@PathVariable("id") Long id) {
        this.anuncioEmpresaService.delete(id);
    }



}
