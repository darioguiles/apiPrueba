package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //Reforzamos el repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    //Si no usar native query con @Query
    //Por algun motivo no va?
/*
    public List<Usuario> findByNom_usuarioContainingIgnoreCase (String titulo);
    public List<Usuario> findByNom_usuarioContainingIgnoreCaseOrderByNom_usuarioAsc (String titulo);
    public List<Usuario> findByNom_usuarioContainingIgnoreCaseOrderByNom_usuarioDesc (String titulo);
*/
}

