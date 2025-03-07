package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
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
    @JsonIgnore
    @MapsId
    @ToString.Exclude
    private Usuario usuario;
    // COINCIDEN LOS IDs

    //orphanRemoval = true
    //Esta propiedad nos permite eliminar la parte del muchos automaticamente
    //Tras haberlo quitado de la colección
    @OneToMany (mappedBy = "trabajador", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Set<AnuncioTrabajador> anunciosTrabajador = new HashSet<>();

    @ManyToMany
    @JsonIgnore
    private Set<AnuncioEmpresa> anunciosAplicados = new HashSet<>();


    public Trabajador(String nombre, String apellidos, String informacion, String telefono, Usuario usuario) {
        this.id_trabajador=usuario.getIdUsuario();
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.informacion = informacion;
        this.telefono = telefono;
        this.usuario = usuario;
    }
}
