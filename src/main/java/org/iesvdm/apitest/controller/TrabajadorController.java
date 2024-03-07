package org.iesvdm.apitest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.Trabajador;
import org.iesvdm.apitest.service.TrabajadorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j //Anotacion para Log y ver los mensajes en terminal
@RestController //Anotacion que extiende las capacidades de Controller, proporcionando
//Compatibilidad con la Api Rest y la seriealizacion y deserializacion con Jackson
@CrossOrigin(origins = "http://localhost:8080") //<- Origen de la petición del frontend, hay que permitir la entrada
//desde el back para ir al front
@RequestMapping("/v1/data-api/trabajadores")
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    public TrabajadorController(TrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }

    @GetMapping(value = {"","/"}) //Usuario
    public List<Trabajador> all() {
        log.info("Accediendo a todas los trabajadores");
        return this.trabajadorService.all();
    }

    @PostMapping({"","/"})
    public Trabajador newTrabajador(@RequestBody Trabajador trabajador) {
        return this.trabajadorService.save(trabajador);
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
