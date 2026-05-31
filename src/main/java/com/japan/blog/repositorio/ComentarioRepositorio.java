package com.japan.blog.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.japan.blog.modelo.Comentario;

@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario, Integer> {

    //  saca todos los comentarios de un articulo
    List<Comentario> findByArticuloIdarticulo(Integer idarticulo);
}
