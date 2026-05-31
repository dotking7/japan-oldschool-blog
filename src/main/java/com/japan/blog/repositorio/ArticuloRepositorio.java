package com.japan.blog.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.japan.blog.modelo.Articulo;

@Repository
public interface ArticuloRepositorio extends JpaRepository<Articulo, Integer> {

    //  todos ordenados del mas nuevo al mas viejo
    List<Articulo> findAllByOrderByFechaDesc();

    List<Articulo> findByMarcaIdmarcaOrderByFechaDesc(Integer idmarca);

    //  para el carrusel de portada, los 4 ultimos con imagen
    List<Articulo> findTop4ByImagenIsNotNullAndImagenNotOrderByFechaDesc(String imagen);

    //  los del autor, para que no vea los de otros
    List<Articulo> findByAutorIdusuarioOrderByFechaDesc(Integer idusuario);
}
