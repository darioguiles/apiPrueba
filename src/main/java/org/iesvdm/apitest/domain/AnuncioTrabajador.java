package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private long idAnuncioTrabajador;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Spain")
    private Date fechaPublicacion;
    private String descripcion;

    @ManyToOne
    @JsonBackReference
    private Trabajador trabajador;

    @ManyToMany (mappedBy = "anunciosInteresado")
    @JsonIgnore
    private Set<Empresa> empresasInteresadas = new HashSet<>();

    public AnuncioTrabajador(String descripcion) {
        this.descripcion = descripcion;
        this.fechaPublicacion = new Date();
    }
}
