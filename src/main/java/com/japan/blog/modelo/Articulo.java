package com.japan.blog.modelo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "articulos")
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idarticulo;

    private String titulo;

    private String texto;

    private String imagen;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "idmarca")
    private Marca marca;

    //  quien escribio esto
    @ManyToOne
    @JoinColumn(name = "idusuario")
    private Usuario autor;

    //  los comentarios del articulo
    @OneToMany(mappedBy = "articulo", cascade = CascadeType.ALL)
    private List<Comentario> comentarios;
}
