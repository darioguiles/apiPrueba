package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.AnuncioTrabajador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioTrabajadorRepository extends JpaRepository<AnuncioTrabajador,Long> {
    //Estamos probando diferentes cosas de filtrado
    public List<AnuncioTrabajador> findByDescripcionContainingIgnoreCase (String titulo);
    public List<AnuncioTrabajador> findAllByDescripcionContainingIgnoreCaseOrderByDescripcionAsc (String desc);
    public List<AnuncioTrabajador> findAllByDescripcionContainingIgnoreCaseOrderByDescripcionDesc (String desc);

    @Query("SELECT at FROM AnuncioTrabajador at JOIN FETCH at.trabajador")
    List<AnuncioTrabajador> findAllWithTrabajador();
    @Query("SELECT at FROM AnuncioTrabajador at JOIN FETCH at.trabajador")
    Page<AnuncioTrabajador> findAllWithTrabajador(Pageable pageable);

}
