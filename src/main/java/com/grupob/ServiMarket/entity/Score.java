package com.grupob.ServiMarket.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Table(name = "calificaciones")
@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer puntaje;
    private String comentario;

    @OneToOne
    private UserEntity cliente;
    @OneToOne
    private Solicitud solicitud;

    @ManyToOne
    private UserEntity provider;

    private boolean calificado;
    private boolean censured;

}
