package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.AnuncioTrabajador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioTrabajadorRepository extends JpaRepository<AnuncioTrabajador,Long> {
    //Estamos probando diferentes cosas de filtrado
    public List<AnuncioTrabajador> findByDescripcionContainingIgnoreCase (String titulo);
    public List<AnuncioTrabajador> findAllByDescripcionContainingIgnoreCaseOrderByDescripcionAsc (String desc);
    public List<AnuncioTrabajador> findAllByDescripcionContainingIgnoreCaseOrderByDescripcionDesc (String desc);


}
