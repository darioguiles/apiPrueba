package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar estilo nuevo
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    // Error java: The old-style 'exclude/of' parameter cannot be used together with the new-style @Include / @Exclude annotations.
    //Solución: @EqualsAndHashCode.Include en atributos y @EqualsAndHashCode(onlyExplicitlyIncluded = true) en la clase
    @Column(name = "id_categoria")
    private long id;

    /* Esta Anotacion junto a la de @EqualsAndHashCode
    * Nos permiten que en el Set<> nombre sea considerado Id
    * evitandonos así la repetición de categoria, pero da problemas en la implementación...
    */
    private String nombre;

 /*   @ManyToMany(
            fetch = FetchType.EAGER
    ) //este Set se mapea a partir del otro
    @JsonIgnore //Si queremos mostrar la categoria
    @ToString.Exclude
    Set<Pelicula> peliculas = new HashSet<>();*/

    /*
    @Column(name = "ultima_actualizacion")
    @JsonFormat(pattern = "yyyy-MM-dd-HH:mm:ss",  shape = JsonFormat.Shape.STRING)
    private Date ultimaActualizacion;
    */

    // @Transient
    // private int conteo // Esto nos permite tener una atributo que no se inicializa, sacando el DTO de la ecuacion
}