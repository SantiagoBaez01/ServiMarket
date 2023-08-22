package com.grupob.ServiMarket.entity;


import com.grupob.ServiMarket.enums.Rubro;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Table(name = "publicacion")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Publication {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private UserEntity provider;
        
        private String title;

        private Rubro rubro;

        private String description;
        private String description2;

        @OneToMany
        private List<Image> image;

        @CreationTimestamp
        private LocalDateTime createdOn;
        @OneToMany
        private List<UserEntity> usercliente;

        private boolean confirmacion;// atributo para ver si el trabajo es rechazado/aceptado
        private boolean estado;//// atributo para ver si esta en curso o finalizado el trabajo

        private boolean publicated;// atributo para ver si esta visible o no
}
