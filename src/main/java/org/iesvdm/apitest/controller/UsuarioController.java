package org.iesvdm.apitest.controller;


import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.Usuario;
import org.iesvdm.apitest.service.EmpresaService;
import org.iesvdm.apitest.service.TrabajadorService;
import org.iesvdm.apitest.service.UsuarioService;
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
//desde el back para ir al front
@RequestMapping("/v1/data-api/usuarios") 
public class UsuarioController {

    @Autowired
    TrabajadorService trabajadorService;
    @Autowired
    EmpresaService empresaService;

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping(value = {"","/"})
    public List<Usuario> all() {
        log.info("Accediendo a todas los usuarios");
        return this.usuarioService.all();
    }

    @PostMapping({"","/"})
    public Usuario newUsuario(@RequestBody Usuario usuario) {
        return this.usuarioService.save(usuario);
    }

    @GetMapping("/{id}")
    public Usuario one(@PathVariable("id") Long id) {
        return this.usuarioService.one(id);
    }

    @PutMapping("/{id}")
    public Usuario replaceUsuario(@PathVariable("id") Long id, @RequestBody Usuario usuario) {
        return this.usuarioService.replace(id, usuario);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable("id") Long id) {

        if(this.usuarioService.one(id).getEmpresa().getId_empresa()==id)
        {
            this.empresaService.delete(id);
        } else if (this.usuarioService.one(id).getTrabajador().getId_trabajador()==id) {
            this.trabajadorService.delete(id);
        }
        //Esta implementación conlleva Que el ID, del usuario es el mismo que el del trabajador/empresa
        //lo cual puede ser que no ocurra


        this.usuarioService.delete(id);
    }

}
