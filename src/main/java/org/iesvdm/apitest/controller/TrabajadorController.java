package org.iesvdm.apitest.controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.Trabajador;
import org.iesvdm.apitest.service.TrabajadorService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/v1/data-api/trabajadores")
@Tag(name = "Trabajadores", description = "API para la gestión de trabajadores") //Nomenclatura Swagger
public class TrabajadorController {
    private final TrabajadorService trabajadorService;
    public TrabajadorController(TrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }
    @GetMapping(value = {"","/"}, params = {"!buscar", "!ordenar", "!pagina", "!tamanio"})
    public List<Trabajador> all() {
        log.info("Accediendo a todas los trabajadores");
        return this.trabajadorService.all();
    }
    @GetMapping(value = {"","/"}, params = {"!pagina", "!tamanio"}) // <- Hace falta bloquear la paginación por esta ruta
    public List<Trabajador> all(@RequestParam("buscar") Optional<String> buscarOpc
            , @RequestParam("ordenar") Optional<String> ordenarOpt) {
        log.info("Accediendo a todas los trabajadores con filtro buscar: %s y ordenar");
        buscarOpc.orElse("VOID");
        ordenarOpt.orElse("VOID");

        return this.trabajadorService.allByQueryFiltersStream(buscarOpc, ordenarOpt);
    }
    @GetMapping(value = {"","/"})
    public ResponseEntity<Map<String,Object>> all(@RequestParam( value = "pagina", defaultValue = "0") int pagina
            , @RequestParam(value = "tamanio" , defaultValue = "5") int tamanio) {
        log.info("Accediendo a todas los anuncioEmpresa con paginacion");

        Map<String, Object> responseAll = this.trabajadorService.findAll(pagina, tamanio);

        return ResponseEntity.ok(responseAll);
    }
    @PostMapping({"","/"})
    public Trabajador newTrabajador(@RequestBody Trabajador trabajador) {
        return this.trabajadorService.save(trabajador);
    }

    @PostMapping("/createTrabajador")
    public ResponseEntity<Trabajador> createTrabajador(@RequestBody Trabajador trabajador) {
        if (trabajador.getUsuario() == null || trabajador.getUsuario().getIdUsuario() == 0) {
            return ResponseEntity.badRequest().body(null);
        }
        Trabajador savedTrabajador = this.trabajadorService.save(trabajador);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrabajador);
    }

    @GetMapping("/{id}")
    public Trabajador one(@PathVariable("id") Long id) {
        return this.trabajadorService.one(id);
    }
    @PutMapping("/{id}")
    public Trabajador replaceTrabajador(@PathVariable("id") Long id, @RequestBody Trabajador trabajador) {
        return this.trabajadorService.replace(id, trabajador);
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTrabajador(@PathVariable("id") Long id) {
        this.trabajadorService.delete(id);
    }


}
