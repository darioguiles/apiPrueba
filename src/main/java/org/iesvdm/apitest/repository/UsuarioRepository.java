package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    //Por algun motivo no va?
/*
    public List<Usuario> findByNom_usuario (String titulo);
    public List<Usuario> findByNom_usuarioContainingIgnoreCaseOrderByNom_usuarioAsc (String titulo);
    public List<Usuario> findByNom_usuarioContainingIgnoreCaseOrderByNom_usuarioDesc (String titulo);
*/
}
