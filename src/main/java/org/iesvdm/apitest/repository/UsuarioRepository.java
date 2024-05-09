package org.iesvdm.apitest.repository;

import org.iesvdm.apitest.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Reforzamos el repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    //Si no usar native query con @Query
    Optional<Usuario> findByNomUsuario(String nombre);

    Optional<Usuario> findByCorreo(String email);

    Boolean existsByNomUsuario(String username);

    Boolean existsByCorreo(String email);


}

