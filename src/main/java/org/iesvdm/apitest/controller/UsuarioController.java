package org.iesvdm.apitest.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.iesvdm.apitest.domain.*;
import org.iesvdm.apitest.dto.UsuarioDto;
import org.iesvdm.apitest.repository.EmpresaRepository;
import org.iesvdm.apitest.repository.TrabajadorRepository;
import org.iesvdm.apitest.repository.UsuarioRepository;
import org.iesvdm.apitest.service.EmpresaService;
import org.iesvdm.apitest.service.TrabajadorService;
import org.iesvdm.apitest.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
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
@Tag(name = "Usuarios", description = "API para la gestión de usuarios") //Nomenclatura Swagger
public class UsuarioController {


    @Autowired
    TrabajadorService trabajadorService;
    @Autowired
    EmpresaService empresaService;

    @Autowired
    TrabajadorRepository trabajadorRepository;
    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;


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


    @Operation(summary = "Obtiene un usuario por ID", description = "Devuelve los datos del usuario correspondiente al ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
        // Fetch the Usuario entity
        Usuario usuario = this.usuarioService.one(id);

        // Check if the Usuario exists
        if (usuario == null) {
            throw new ResourceNotFoundException("Usuario not found with id: " + id);
        }

        // Clean up Many-to-Many relationships
        if (usuario.getTrabajador() != null) {
            Trabajador trabajador = usuario.getTrabajador();
            trabajador.getAnunciosAplicados().clear(); // Clear the Many-to-Many relationship
            trabajadorRepository.save(trabajador); // Save the changes
        }

        if (usuario.getEmpresa() != null) {
            Empresa empresa = usuario.getEmpresa();
            empresa.getAnunciosInteresado().clear(); // Clear the Many-to-Many relationship
            empresaRepository.save(empresa); // Save the changes
        }

        // Delete the Usuario (this will cascade and delete Trabajador/Empresa due to CascadeType.REMOVE)
        this.usuarioService.delete(id);
    }
    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> login(@RequestBody LoginRequest loginRequest) {

        //Valida que el usuario/correo utilizado existe y que la contraseña sea la misma que en BD
        Usuario usuario = usuarioService.validarCredenciales(loginRequest.getNomUsuarioCorreo(), loginRequest.getPassword());

        //Se hacia un doble hasheo
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }


        Optional<Empresa> empresaOptional = empresaRepository.findById(usuario.getIdUsuario());
        Optional<Trabajador> trabajadorOptional = trabajadorRepository.findById(usuario.getIdUsuario());

        Empresa empresa = null;
        Trabajador trabajador = null;

        if (empresaOptional.isPresent())
        {
            empresa = empresaOptional.get();
        }
        if(trabajadorOptional.isPresent()) {
            trabajador = trabajadorOptional.get();
        }

        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getIdUsuario());
        dto.setNombreUsuario(usuario.getNomUsuario());
        dto.setCorreo(usuario.getCorreo());
        dto.setEsAdmin(usuario.isEsAdmin());
        dto.setEmpresa(empresa);
        dto.setTrabajador(trabajador);

        return ResponseEntity.ok(dto); // Devuelve el objeto Usuario completo

    }

    @Operation(summary = "Registra un nuevo usuario", description = "Crea un usuario en el sistema y devuelve el usuario creado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {

        // Validar campos esenciales
        if (usuario.getCorreo() == null || usuario.getContrasenia() == null || usuario.getNomUsuario() == null) {
            return ResponseEntity.badRequest().body(null);
        }


        // Crear el usuario sin referencias a Trabajador o Empresa
        Usuario nuevoUsuario = new Usuario(
                0,
                usuario.getNomUsuario(),
                usuario.getCorreo(),
                usuario.getContrasenia(),
                usuario.getRutapfp(),
                usuario.isEsAdmin(),
                null,
                null
        );

        // If Trabajador data is provided, create and link it to the saved Usuario
        if (usuario.getTrabajador() != null) {
            Trabajador trabajador = usuario.getTrabajador();
            trabajador.setUsuario(nuevoUsuario); // Link to the saved Usuario
            trabajadorRepository.save(trabajador);
        }

        // If Empresa data is provided, create and link it to the saved Usuario
        if (usuario.getEmpresa() != null) {
            Empresa empresa = usuario.getEmpresa();
            empresa.setUsuario(nuevoUsuario); // Link to the saved Usuario
            empresaRepository.save(empresa);
        }

        usuarioService.save(nuevoUsuario);//Guardamos el nuevo! Nueva password!!

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @GetMapping("/verificar-nombre")
    public ResponseEntity<Boolean> verificarNombreUsuario(@RequestParam String nomUsuario) {
        boolean existe = usuarioRepository.existsByNomUsuario(nomUsuario);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/verificar-correo")
    public ResponseEntity<Boolean> verificarCorreo(@RequestParam String correo) {
        boolean existe = usuarioRepository.existsByCorreo(correo);
        return ResponseEntity.ok(existe);
    }

}
