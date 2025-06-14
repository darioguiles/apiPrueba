package org.iesvdm.apitest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.iesvdm.apitest.domain.Empresa;
import org.iesvdm.apitest.domain.Trabajador;
import org.iesvdm.apitest.domain.Usuario;

/**
 * Esta clase se crea por la necesidad de almacenar la información con un
 * mejor orden que con el Usuario base
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {

    private Long id;
    @JsonProperty("nomUsuario")
    private String nombreUsuario;
    private String correo;

    private String contrasenia;
    private String rutapfp;
    private boolean esAdmin; //<-- Este booleano es para definir si el usuario es administrador o no

    private Trabajador trabajador;
    private Empresa empresa;

    public UsuarioDto(Usuario usuario) {
        this.id = usuario.getIdUsuario();
        this.nombreUsuario = usuario.getNomUsuario();
        this.correo = usuario.getCorreo();
        this.contrasenia = usuario.getContrasenia();
        this.rutapfp = usuario.getRutapfp();
        this.esAdmin = usuario.isEsAdmin();
        this.trabajador = usuario.getTrabajador();
        this.empresa = usuario.getEmpresa();
    }
}
