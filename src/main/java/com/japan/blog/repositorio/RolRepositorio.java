package com.japan.blog.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.japan.blog.modelo.Rol;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, Integer> {

    Rol findByRol(String rol); // busca el rol por nombre ej ROLE_USER
}
