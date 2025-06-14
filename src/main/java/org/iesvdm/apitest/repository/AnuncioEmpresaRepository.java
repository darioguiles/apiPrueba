package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.AnuncioEmpresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnuncioEmpresaRepository extends JpaRepository<AnuncioEmpresa,Long> {
   //Estos seran para Filtrado y busqueda

    public List<AnuncioEmpresa> findByDescripcionContainingIgnoreCase (String titulo);
    public List<AnuncioEmpresa> findAllByDescripcionContainingIgnoreCaseOrderByDescripcionAsc (String desc);
    public List<AnuncioEmpresa> findAllByDescripcionContainingIgnoreCaseOrderByDescripcionDesc (String desc);

    @Query("SELECT a FROM AnuncioEmpresa a WHERE " +
            "(:descripcion IS NULL OR a.descripcion LIKE %:descripcion%) AND " +
            "(:nombre IS NULL OR a.empresa.nombre LIKE %:nombre%)")
    List<AnuncioEmpresa> findAnuncioTod (@Param("descripcion") String descripcion,
                                         @Param("nombre") String nombre);

    @Query("SELECT a FROM AnuncioEmpresa a WHERE " +
            "(:descripcion IS NULL OR a.descripcion LIKE %:descripcion%) AND " +
            "(:nombre IS NULL OR a.empresa.nombre LIKE %:nombre%)")
    Page<AnuncioEmpresa> findBecauseFilter(@Param("descripcion") String descripcion,
                                           @Param("nombre") String nombre,
                                           Pageable pageable);

    @Query("SELECT a FROM AnuncioEmpresa a WHERE a.empresa.id_empresa = :id")
    public List<AnuncioEmpresa> findAllByEmpresaIdEmpresa(@Param("id") Long id);
    @Query("SELECT ae FROM AnuncioEmpresa ae JOIN FETCH ae.empresa")
    List<AnuncioEmpresa> findAllWithEmpresa();

    @Query("SELECT ae FROM AnuncioEmpresa ae JOIN FETCH ae.empresa")
    Page<AnuncioEmpresa> findAllWithEmpresa(Pageable pageable);

}
