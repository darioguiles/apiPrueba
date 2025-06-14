package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.AnuncioEmpresa;
import org.iesvdm.apitest.domain.AnuncioTrabajador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioTrabajadorRepository extends JpaRepository<AnuncioTrabajador,Long> {
    //Estamos probando diferentes cosas de filtrado
    public List<AnuncioTrabajador> findByDescripcionContainingIgnoreCase (String titulo);
    public List<AnuncioTrabajador> findAllByDescripcionContainingIgnoreCaseOrderByDescripcionAsc (String desc);
    public List<AnuncioTrabajador> findAllByDescripcionContainingIgnoreCaseOrderByDescripcionDesc (String desc);

    @Query("SELECT a FROM AnuncioTrabajador a WHERE " +
            "(:descripcion IS NULL OR a.descripcion LIKE %:descripcion%) AND " +
            "(:nombre IS NULL OR a.trabajador.nombre LIKE %:nombre% OR a.trabajador.apellidos LIKE %:nombre% )")
    public List<AnuncioTrabajador> findAnuncioTod (@Param("descripcion") String descripcion,
                                                @Param("nombre") String nombre);
    @Query("SELECT a FROM AnuncioTrabajador a WHERE " +
            "(:descripcion IS NULL OR a.descripcion LIKE %:descripcion%) AND " +
            "(:nombre IS NULL OR a.trabajador.nombre LIKE %:nombre% OR a.trabajador.apellidos LIKE %:nombre% )")
    Page<AnuncioTrabajador> findBecauseFilter(@Param("descripcion") String descripcion,
                                           @Param("nombre") String nombre,
                                           Pageable pageable);
    @Query("SELECT a FROM AnuncioTrabajador a WHERE a.trabajador.id_trabajador = :id")
    List<AnuncioTrabajador> findAllByTrabajadorIdtrabajador(@Param("id") Long id);
    @Query("SELECT at FROM AnuncioTrabajador at JOIN FETCH at.trabajador")
    List<AnuncioTrabajador> findAllWithTrabajador();
    @Query("SELECT at FROM AnuncioTrabajador at JOIN FETCH at.trabajador")
    Page<AnuncioTrabajador> findAllWithTrabajador(Pageable pageable);

}
