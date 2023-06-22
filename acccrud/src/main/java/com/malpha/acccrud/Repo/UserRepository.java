package com.malpha.acccrud.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.malpha.acccrud.Model.Usuario;

/* interface do repositorio */
@Repository
public interface UserRepository extends JpaRepository<Usuario, Long>{ 
}