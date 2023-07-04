package com.malpha.acccrud.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.malpha.acccrud.Model.Usuario;

/* interface do repositorio */
@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

  @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = ?1")
  boolean existsByEmail(String email);

  @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.username = ?1")
  boolean existsByUsername(String username);

  Usuario findByUsername(String username);
}