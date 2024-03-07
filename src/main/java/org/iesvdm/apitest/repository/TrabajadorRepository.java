package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.Trabajador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrabajadorRepository extends JpaRepository<Trabajador,Long> {
}
