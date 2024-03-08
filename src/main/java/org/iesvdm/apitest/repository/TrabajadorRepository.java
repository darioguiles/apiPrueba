package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.Trabajador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface TrabajadorRepository extends JpaRepository<Trabajador,Long> {

    public List<Trabajador> findByNombreContainingIgnoreCase (String titulo);
    public List<Trabajador> findByNombreContainingIgnoreCaseOrderByNombreAsc (String titulo);
    public List<Trabajador> findByNombreContainingIgnoreCaseOrderByNombreDesc (String titulo);


}
