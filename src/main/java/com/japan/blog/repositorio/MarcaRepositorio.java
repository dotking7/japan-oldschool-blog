package com.japan.blog.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.japan.blog.modelo.Marca;

@Repository
public interface MarcaRepositorio extends JpaRepository<Marca, Integer> {
    // sin metodos extra, con los de JpaRepository llega
}
