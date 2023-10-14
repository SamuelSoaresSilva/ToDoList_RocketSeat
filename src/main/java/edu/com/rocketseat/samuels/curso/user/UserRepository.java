package edu.com.rocketseat.samuels.curso.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
//no JpaRepository e preciso declarar: classe referenciada e o tipo do ID entre "<classe, id>"
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    //metodo para buscar um objeto pelo atributo username
    UserModel findByUsername(String username);

}
