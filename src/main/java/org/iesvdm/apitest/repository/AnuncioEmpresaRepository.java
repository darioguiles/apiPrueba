package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.AnuncioEmpresa;
import org.iesvdm.apitest.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioEmpresaRepository extends JpaRepository<AnuncioEmpresa,Long> {
    public List<Empresa> findByNombreContainingIgnoreCase (String titulo);
    public List<Empresa> findByNombreContainingIgnoreCaseOrderByNombreAsc (String titulo);
    public List<Empresa> findByNombreContainingIgnoreCaseOrderByNombreDesc (String titulo);

}
