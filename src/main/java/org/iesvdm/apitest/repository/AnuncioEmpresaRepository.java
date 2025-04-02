package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.AnuncioEmpresa;
import org.iesvdm.apitest.domain.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioEmpresaRepository extends JpaRepository<AnuncioEmpresa,Long> {
   //Estos seran para Filtrado y busqueda

    public List<AnuncioEmpresa> findByDescripcionContainingIgnoreCase (String titulo);
    public List<AnuncioEmpresa> findAllByDescripcionContainingIgnoreCaseOrderByDescripcionAsc (String desc);
    public List<AnuncioEmpresa> findAllByDescripcionContainingIgnoreCaseOrderByDescripcionDesc (String desc);

    @Query("SELECT ae FROM AnuncioEmpresa ae JOIN FETCH ae.empresa")
    List<AnuncioEmpresa> findAllWithEmpresa();

    @Query("SELECT ae FROM AnuncioEmpresa ae JOIN FETCH ae.empresa")
    Page<AnuncioEmpresa> findAllWithEmpresa(Pageable pageable);

}
