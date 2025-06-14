package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private long idAnuncioEmpresa;

    private String descripcion;
    private int cantidadPuestos;

    //Nullable
    private int numAdscritos;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Spain")
    private LocalDateTime fechaInicio;

    //Nullable
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Spain")
    private LocalDateTime fechaFin;

    @ManyToOne
    @JsonBackReference
    private Empresa empresa;

    @ManyToMany(mappedBy = "anunciosAplicados")
    @JsonIgnore
    private Set<Trabajador> trabajadoresInteresados = new HashSet<>();

    public AnuncioEmpresa(String descripcion, int cantidadPuestos, LocalDateTime fechaInicio,LocalDateTime fechaFin) {
        this.descripcion = descripcion;
        this.cantidadPuestos = cantidadPuestos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public AnuncioEmpresa(String descripcion, int cantidadPuestos, LocalDateTime fechaInicio) {
        this.descripcion = descripcion;
        this.cantidadPuestos = cantidadPuestos;
        this.fechaInicio = fechaInicio;
    }
}
