package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar estilo nuevo
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empresa {

    @Id
    @EqualsAndHashCode.Include
    private long id_empresa;

    private String nombre;

    private String descripcion;

    private String telefono;
    
    @OneToOne
    @JsonIgnore
    private Usuario usuario;

    public Empresa(String nombre, String descripcion, String telefono, Usuario usuario) {
        this.id_empresa=usuario.getId_usuario();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.usuario = usuario;
    }
}
