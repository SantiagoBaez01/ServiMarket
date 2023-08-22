package com.grupob.ServiMarket.entity;

import com.grupob.ServiMarket.enums.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Table (name = "usuarios")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    private String lastName;


    @Email
    private String email;



    private String password;



    private String contact;


    private String address;

    @OneToOne
    private Image image;


    private boolean status;//anotacion para dar de baja




    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany
    private List<Publication> servicioContratado;//un usuario contrata servicios y se le asignan

    

}
