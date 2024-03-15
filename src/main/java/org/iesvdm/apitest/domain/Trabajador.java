package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Trabajador {

    @Id
    @EqualsAndHashCode.Include
    private long id_trabajador;

    private String nombre; //Nombre y apellidos del trabajador

    private String apellidos;

    private String informacion;

    private String telefono;

    @OneToOne
    @JsonBackReference
    private Usuario usuario;
    // COINCIDEN LOS IDs


    public Trabajador(String nombre, String apellidos, String informacion, String telefono, Usuario usuario) {
        this.id_trabajador=usuario.getId_usuario();
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.informacion = informacion;
        this.telefono = telefono;
        this.usuario = usuario;
    }
}
