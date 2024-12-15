package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

//Clase por la cual comparamos el Login del Usuario
@Data
public class LoginRequest {
    @JsonProperty("nomUsuarioCorreo")
    private String nomUsuarioCorreo; //String que puede almacenar el nombre del usuario o correo
    @JsonProperty("contrasenia")
    private String password;
}
