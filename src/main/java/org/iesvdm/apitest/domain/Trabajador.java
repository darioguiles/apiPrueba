package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

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

    //orphanRemoval = true
    //Esta propiedad nos permite eliminar la parte del muchos automaticamente
    //Tras haberlo quitado de la colección
    @OneToMany (mappedBy = "trabajador")
    @OnDelete(action = OnDeleteAction.CASCADE) //Requiere de @JoinColumn
    @JsonBackReference
    private Set<AnuncioTrabajador> anunciosTrabajador;


    public Trabajador(String nombre, String apellidos, String informacion, String telefono, Usuario usuario) {
        this.id_trabajador=usuario.getId_usuario();
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.informacion = informacion;
        this.telefono = telefono;
        this.usuario = usuario;
    }
}
