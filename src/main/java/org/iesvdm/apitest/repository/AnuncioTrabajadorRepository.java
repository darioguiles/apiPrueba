package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.AnuncioTrabajador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioTrabajadorRepository extends JpaRepository<AnuncioTrabajador,Long> {
    //Estamos probando diferentes cosas de filtrado
    public List<AnuncioTrabajador> findAllByDescripcionMatches (String desc);
    public List<AnuncioTrabajador> findByNombreContainingIgnoreCaseOrderByNombreAsc (String titulo);
    public List<AnuncioTrabajador> findByNombreContainingIgnoreCaseOrderByNombreDesc (String titulo);


}
