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
public class Empresa {

    @Id
    @EqualsAndHashCode.Include
    private long id_empresa;

    private String nombre;

    private String descripcion;

    private String telefono;
    
    @OneToOne
    @JsonBackReference
    private Usuario usuario;

    @OneToMany (mappedBy = "empresa")
    @OnDelete(action = OnDeleteAction.CASCADE) //Requiere de @JoinColumn
    @JsonBackReference
    private Set<AnuncioEmpresa> anuncioEmpresas;
    public Empresa(String nombre, String descripcion, String telefono, Usuario usuario) {
        this.id_empresa=usuario.getId_usuario();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.usuario = usuario;
    }
}
