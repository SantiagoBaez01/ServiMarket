package com.grupob.ServiMarket.entity;

import com.grupob.ServiMarket.enums.EstadoServicio;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Table(name = "solicitud")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private UserEntity provider;

    @ManyToOne
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;

    @ManyToOne
    @JoinColumn(name = "user_client_id", nullable = false)
    private UserEntity userClient;

    @Enumerated(EnumType.STRING)
    private EstadoServicio estado;

    private boolean completo;

    private boolean calificado;

    private boolean dardebaja;


}
