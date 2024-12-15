package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa,Long> {

    public List<Empresa> findByNombreContainingIgnoreCase (String titulo);
    public List<Empresa> findByNombreContainingIgnoreCaseOrderByNombreAsc (String titulo);
    public List<Empresa> findByNombreContainingIgnoreCaseOrderByNombreDesc (String titulo);

    public Optional<Empresa> findByUsuarioCorreo(String correo);
}
