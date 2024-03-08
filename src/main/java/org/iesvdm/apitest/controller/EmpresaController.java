package org.iesvdm.apitest.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.Empresa;
import org.iesvdm.apitest.service.EmpresaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j //Anotacion para Log y ver los mensajes en terminal
@RestController //Anotacion que extiende las capacidades de Controller, proporcionando
//Compatibilidad con la Api Rest y la seriealizacion y deserializacion con Jackson
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/v1/data-api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;
    public EmpresaController(EmpresaService empresaService) { this.empresaService = empresaService;}

    @GetMapping(value = {"","/"},  params = {"!buscar", "!ordenar", "!pagina", "!tamanio"})
    public List<Empresa> all() {
        log.info("Accediendo a todas las empresas");
        return this.empresaService.all();

        // TODO Crear DTO Para el numero de anuncios para trabajador y empresa
        /*List<TrabajadorDTO> trabajadorDTOList = listaT.stream()
                .map(TrabajadorDTO::new)
                .collect(Collectors.toList());
        * */
        //Para mejorar el consumo de memoria quitar DTO del path por defecto y que sea con el parametro count ¿?
    }


    @GetMapping(value = {"","/"}, params = {"!pagina", "!tamanio"}) // <- Hace falta bloquear la paginación por esta ruta
    public List<Empresa> all(@RequestParam("buscar") Optional<String> buscarOpc
            , @RequestParam("ordenar") Optional<String> ordenarOpt) {
        log.info("Accediendo a todas las categorias con filtro buscar: %s y ordenar");
        buscarOpc.orElse("VOID");
        ordenarOpt.orElse("VOID");

        return this.empresaService.allByQueryFiltersStream(buscarOpc, ordenarOpt);
    }

    @GetMapping(value = {"","/"})
    public ResponseEntity<Map<String,Object>> all(@RequestParam( value = "pagina", defaultValue = "0") int pagina
            , @RequestParam(value = "tamanio" , defaultValue = "3") int tamanio) {
        log.info("Accediendo a todas las categorias con paginacion");

        Map<String, Object> responseAll = this.empresaService.findAll(pagina, tamanio);

        return ResponseEntity.ok(responseAll);
    }

    @PostMapping({"","/"})
    public Empresa newEmpresa(@RequestBody Empresa empresa) {
        return this.empresaService.save(empresa);
    }

    @GetMapping("/{id}")
    public Empresa one(@PathVariable("id") Long id) {
        return this.empresaService.one(id);
    }

    @PutMapping("/{id}")
    public Empresa replaceEmpresa(@PathVariable("id") Long id, @RequestBody Empresa empresa) {
        return this.empresaService.replace(id, empresa);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEmpresa(@PathVariable("id") Long id) {
        this.empresaService.delete(id);
    }

}
