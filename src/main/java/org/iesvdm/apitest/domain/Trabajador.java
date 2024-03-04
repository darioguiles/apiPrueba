package org.iesvdm.apitest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar estilo nuevo
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id_trabajador;

    private String nombre; //Nombre y apellidos del trabajador

    private String apellidos;

    private String informacion;

    private String telefono;

    @OneToOne
    private Usuario usuario;

}
