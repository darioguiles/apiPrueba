package org.iesvdm.apitest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
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
    @JsonManagedReference
    @MapsId
    private Usuario usuario;

    @OneToMany (mappedBy = "empresa", cascade = CascadeType.REMOVE, orphanRemoval = true)
   // @OnDelete(action = OnDeleteAction.CASCADE) //Requiere de @JoinColumn p
    @JsonManagedReference //Solo en One to Many en el lado que queremos quitar
    private Set<AnuncioEmpresa> anuncioEmpresas = new HashSet<>();

    /*
    * @JsonBackReference y @JsonManagedReference
    * solo sirve en oneToMany y JsonManagedReference se asigna a los oneToMany en colecciones
    *
    * */


    @ManyToMany
    @JsonIgnore
    /*@JoinTable(name = "anuncio_trabajador_has_empresa",
    joinColumns = @JoinColumn(name ="id_empresa" ,referencedColumnName ="id_empresa" ),
    inverseJoinColumns = @JoinColumn(name ="id_anunciotrabajador" ,referencedColumnName ="id_anunciotrabajador" ) )
    */
    private Set<AnuncioTrabajador> anunciosInteresado = new HashSet<>();



    public Empresa(String nombre, String descripcion, String telefono, Usuario usuario) {
        this.id_empresa=usuario.getIdUsuario();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.usuario = usuario;
    }
}
