package org.iesvdm.apitest.controller;


import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.Usuario;
import org.iesvdm.apitest.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j //Anotacion para Log y ver los mensajes en terminal
@RestController //Anotacion que extiende las capacidades de Controller, proporcionando
//Compatibilidad con la Api Rest y la seriealizacion y deserializacion con Jackson
@CrossOrigin(origins = "http://localhost:8080") //<- Origen de la petición del frontend, hay que permitir la entrada
//desde el back para ir al front
@RequestMapping("/v1/data-api/usuarios") 
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping(value = {"","/"}) //Usuario
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
        this.usuarioService.delete(id);
    }

}
