package org.iesvdm.apitest.domain;

import jakarta.annotation.Nullable;
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
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id_usuario;

    private String nom_usuario; // <--

    private String correo; // <-- Ambos de estos campos serán equivalentes al hacer un login
    //Podemos tener un hashcode de nom_usuario y correo puesto que estos son los campos que definen el login
    //DEBEN ser únicos e irrepetible. HASH con opción a modificar

    private String contrasenia;

    private String rutapfp; //<-- Este string almacena la ruta donde se guarda la foto de perfil, tendremos por defecto
    //una foto si el usuario no pone una, esta parte está sujeta a cambios por si existe alguna manera más eficiente.

    private boolean esAdmin; //<-- Este booleano es para definir si el usuario es administrador o no

    @OneToOne
    private Empresa empresa;

    @OneToOne
    private Trabajador trabajador;

    //Constructor Usuario SIN ruta img (Img por defecto)
    public Usuario(long id_usuario, String nom_usuario, String correo, String contrasenia) {
        this.id_usuario = id_usuario;
        this.nom_usuario = nom_usuario;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.rutapfp = "ruta_defecto";
        // esAdmin se inicializará a false automáticamente
        // empresa y trabajador se inicializarán a null automáticamente
    }


    //Duda: Según Mi implementación un usuario puede ser o Empresa o Trabajador
    // Para bloquearlo dejo el otro atributo a null? o habria Solucionado: dejandolo a null está bien.

}
