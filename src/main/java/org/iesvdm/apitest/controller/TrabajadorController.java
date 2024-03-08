package org.iesvdm.apitest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.Trabajador;
import org.iesvdm.apitest.service.TrabajadorService;
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
@RequestMapping("/v1/data-api/trabajadores")
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    public TrabajadorController(TrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }

    @GetMapping(value = {"","/"}, params = {"!buscar", "!ordenar", "!pagina", "!tamanio"}) //Usuario
    public List<Trabajador> all() {
        log.info("Accediendo a todas los trabajadores");
        return this.trabajadorService.all();

        // TODO Crear DTO Para el numero de anuncios para trabajador y empresa
        /*List<TrabajadorDTO> trabajadorDTOList = listaT.stream()
                .map(TrabajadorDTO::new)
                .collect(Collectors.toList());
        * */
        //Para mejorar el consumo de memoria quitar DTO del path por defecto y que sea con el parametro count ¿?

    }

    @GetMapping(value = {"","/"}, params = {"!pagina", "!tamanio"}) // <- Hace falta bloquear la paginación por esta ruta
    public List<Trabajador> all(@RequestParam("buscar") Optional<String> buscarOpc
            , @RequestParam("ordenar") Optional<String> ordenarOpt) {
        log.info("Accediendo a todas las categorias con filtro buscar: %s y ordenar");
        buscarOpc.orElse("VOID");
        ordenarOpt.orElse("VOID");

        return this.trabajadorService.allByQueryFiltersStream(buscarOpc, ordenarOpt);
    }

    @GetMapping(value = {"","/"})
    public ResponseEntity<Map<String,Object>> all(@RequestParam( value = "pagina", defaultValue = "0") int pagina
            , @RequestParam(value = "tamanio" , defaultValue = "3") int tamanio) {
        log.info("Accediendo a todas las categorias con paginacion");

        Map<String, Object> responseAll = this.trabajadorService.findAll(pagina, tamanio);

        return ResponseEntity.ok(responseAll);
    }

    /*
    *
    *  @GetMapping(value = {"","/"}, params = {"!pagina", "!tamanio"})
    public List<Trabajador> all(@RequestParam( value = "order", defaultValue = "idPelicula,asc") String[] order) {


        log.info("Accediendo a todos los parametros con el orden " + order[0] + " y " + order[1] );

        //Problemas: titulo (error en el order[1] al estar vacio)
        // titulo, no se da ordenación y se queda vacio, el default no funciona?
        // TODO fixear estos errores o buscar alternativas

        return this.peliculaService.all(order);
    }
    * */

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
