package edu.com.rocketseat.samuels.curso.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
//data foi usado para atribuir getters e setters a todos os atributos
@Entity(name = "tb_users")
public class UserModel {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    //@Column so seria usado para atribuir outro nome para a coluna
    @Column(unique = true)
    private String username;
    private String  name, password;

    //Serve para armazenar a hora e a data puxando as informações locais do usuario administrador
    @CreationTimestamp
    private LocalDateTime createdAt;

}
