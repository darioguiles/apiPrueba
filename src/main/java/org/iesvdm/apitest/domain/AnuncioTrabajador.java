package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar estilo nuevo
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnuncioTrabajador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id_anunciotrabajador;

    private Date fecha_publicacion;
    private String descripcion;

    @ManyToOne
    private Trabajador trabajador;

    @ManyToMany (mappedBy = "anunciosInteresado")
    @JsonBackReference
    private Set<Empresa> empresasInteresadas = new HashSet<>();

}
