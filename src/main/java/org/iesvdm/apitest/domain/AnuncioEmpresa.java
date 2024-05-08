package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class AnuncioEmpresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id_anuncioempresa;

    private String descripcion;
    private int cantidad_puestos;

    //Nullable
    private int num_adscritos;
    private Date fecha_inicio;

    //Nullable
    private Date fecha_fin;

    @ManyToOne
    @JsonBackReference
    private Empresa empresa;

    @ManyToMany(mappedBy = "anunciosAplicados")
    @JsonIgnore
    private Set<Trabajador> trabajadoresInteresados = new HashSet<>();

}
