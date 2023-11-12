package com.gamingzone.redeem.security;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

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
        String jwt = request.getParameter("jwt");
        System.out.println("check_token: " + jwt);
        System.out.println(jwt);

        if (jwt == null ) {

            return Optional.empty();
        }
        Map<String ,Object> map = JwtUtil.decode(jwt);
        System.out.println("map: "+ map);
        System.out.println("jwt.length()");
        System.out.println(jwt.length());
        System.out.println("jwt.trim.length()");
        System.out.println(jwt.trim().length());

        jwtUtil.getValuesFromToken(jwt);




        return Optional.of(new JwtAuth("nouser", AuthorityUtils.NO_AUTHORITIES));
    }

}