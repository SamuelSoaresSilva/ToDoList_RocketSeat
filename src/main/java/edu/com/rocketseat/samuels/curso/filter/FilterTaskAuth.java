package edu.com.rocketseat.samuels.curso.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import edu.com.rocketseat.samuels.curso.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

//sera usado para autorizar a criação de uma task relacionada a um usuário
@Component
//classe generica de gerenciamento do spring
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //faz a verificação para que a autentificação seja exigida só em tasks
        var serlvetPath = request.getServletPath();

        if (serlvetPath.startsWith("/tasks/")){

            var authorization = request.getHeader("Authorization");
            //armazena a autorização sem a palavra Basic pois o ".trim()" serve para apagar da string

            var authEncoded = authorization.substring("Basic".length()).trim();

            //decodifica a criptografia e guarda em um array de bytes
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

            //transforma o array de bytes em uma string
            var authString = new String(authDecoded);

            //[username, password]
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];


            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401,"Usuário não encontrado");
            } else {
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified == true) {


                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                }else {
                    response.sendError(401, "Senha incorreta de usuario");
                }
            }
        }else {
            filterChain.doFilter(request, response);
        }
    }
}
