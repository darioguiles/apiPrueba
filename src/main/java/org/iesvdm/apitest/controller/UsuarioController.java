package org.iesvdm.apitest.controller;


import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.LoginRequest;
import org.iesvdm.apitest.domain.Usuario;
import org.iesvdm.apitest.dto.UsuarioDto;
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
import java.util.stream.Collectors;

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
    public List<UsuarioDto> all() {
        return this.usuarioService.all().stream()
                .map(UsuarioDto::new)
                .collect(Collectors.toList());
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

        //Se añade esta linea porque sin ella no se podia hacer algo tan normal como borrar y controlar usuarios nuevos
        //Estos usuarios nuevos no tienen una empresa ni trabajador asociado han entrado a probar la aplicación.
        if (this.usuarioService.one(id).getEmpresa()!=null || this.usuarioService.one(id).getTrabajador()!=null) {
            if (this.usuarioService.one(id).getEmpresa().getId_empresa() == id) {
                this.empresaService.delete(id);
            } else if (this.usuarioService.one(id).getTrabajador().getId_trabajador() == id) {
                this.trabajadorService.delete(id);
            }
        }

        this.usuarioService.delete(id);
    }
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest loginRequest) {

        Usuario usuario = usuarioService.validarCredenciales(loginRequest.getNomUsuarioCorreo(), loginRequest.getPassword());

        if (usuario == null) {
            // Si no se encuentra por nombreUsuario, intenta con el correo
            usuario = usuarioService.validarCredencialesPorCorreo(loginRequest.getNomUsuarioCorreo(), loginRequest.getPassword());
        }

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(usuario); // Devuelve el objeto Usuario completo
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {

        if (usuario.getCorreo() == null || usuario.getContrasenia() == null) {
            return ResponseEntity.badRequest().build();
        }
        Usuario nuevoUsuario = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

}
