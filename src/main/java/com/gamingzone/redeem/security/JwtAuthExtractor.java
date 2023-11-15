package com.gamingzone.redeem.security;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtAuthExtractor {

    //@Value("${application.security.api-key}")
    //private String apiKey;
    @Autowired
    private JwtUtil jwtUtil;

    public Optional<Authentication> extract(HttpServletRequest request) {
       // String providedKey = request.getHeader("ApiKey");


        String jwtToken = request.getParameter("jwt");
       // System.out.println("check_token: " + jwt);
       // System.out.println(jwt);

        if (jwtToken == null ) {
             return Optional.empty();
        }
        Map<String ,Object> jwtValues =  jwtUtil.getValuesFromToken(jwtToken);
        if (jwtUtil.validateToken(jwtValues)) {
            if (jwtValues.get("KEY") !=null){
                request.getSession().setAttribute("KEY" ,jwtValues.get("KEY"));
            }
            UserDetails userDetails =  new User("userName","notUsed", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,null , userDetails.getAuthorities()
            );
            usernamePasswordAuthenticationToken.setDetails(  new WebAuthenticationDetailsSource().buildDetails(request));
            return Optional.of(usernamePasswordAuthenticationToken);
        }

        return Optional.empty();
    }

}