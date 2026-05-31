package com.japan.blog.modelo;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "marcas")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idmarca;

    private String nombre;

    //  articulos de esta marca
    @OneToMany(mappedBy = "marca")
    private List<Articulo> articulos;
}
