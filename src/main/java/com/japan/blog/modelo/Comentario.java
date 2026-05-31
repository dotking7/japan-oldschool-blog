package com.japan.blog.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idcomentario;

    //  el texto que escribe el usuario
    @Column(name = "texto")
    private String texto;

    @ManyToOne
    @JoinColumn(name = "idarticulo")
    private Articulo articulo; // articulo al que pertenece

    @ManyToOne
    @JoinColumn(name = "idusuario")
    private Usuario usuario;  // quien lo escribio

}
