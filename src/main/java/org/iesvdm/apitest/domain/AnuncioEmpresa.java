package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private long idAnuncioEmpresa;

    private String descripcion;
    private int cantidadPuestos;

    //Nullable
    private int numAdscritos;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Spain")
    private Date fechaInicio;

    //Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Spain")
    private Date fechaFin;

    @ManyToOne
    @JsonBackReference
    private Empresa empresa;

    @ManyToMany(mappedBy = "anunciosAplicados")
    @JsonIgnore
    private Set<Trabajador> trabajadoresInteresados = new HashSet<>();

    public AnuncioEmpresa(String descripcion, int cantidadPuestos, Date fechaFin) {
        this.descripcion = descripcion;
        this.cantidadPuestos = cantidadPuestos;
        this.fechaInicio = new Date();
        this.fechaFin = fechaFin;
    }

    public AnuncioEmpresa(String descripcion, int cantidadPuestos) {
        this.descripcion = descripcion;
        this.cantidadPuestos = cantidadPuestos;
        this.fechaInicio = new Date();
    }
}
