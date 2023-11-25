package com.gamingzone.redeem.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtAuthExtractor extractor;
    //@Autowired
    //private JwtUtil jwtUtil;
   // private final List<String> PATHS_TO_IGNORE_SETTING_SAMESITE = Arrays.asList("resources", <add other paths you want to exclude>);
    private final String SESSION_COOKIE_NAME = "JSESSIONID";
    private final String SESSION_PATH_ATTRIBUTE = ";Path=";
    private final String ROOT_CONTEXT = "/";
    private final String SAME_SITE_ATTRIBUTE_VALUES = ";HttpOnly;Secure;SameSite=None";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

       // HttpServletRequest req = (HttpServletRequest) request;
       // HttpServletResponse resp = (HttpServletResponse) response;

        /*
        String userName = null;
        boolean pass = true;
        Map<String, Object> jwtValues = null;
        String jwt = request.getParameter("jwt");
        // System.out.println("check_token: " + jwt);
        // System.out.println(jwt);

        if (jwt != null) {
            try {
                jwtValues = jwtUtil.getValuesFromToken(jwt);
                userName = (String) jwtValues.get("user");

            } catch (Exception exception) {
                pass = false;

            }

        }
       if (!pass && userName != null &&  SecurityContextHolder.getContext().getAuthentication() == null){
           UserDetails userDetails =  new User(userName,"notUsed", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
           UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                   userDetails,null , userDetails.getAuthorities()
           );
           usernamePasswordAuthenticationToken.setDetails(  new WebAuthenticationDetailsSource().buildDetails(request));
           SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

       }
       */


        System.out.println("request.getRequestURI()");
        System.out.println(request.getRequestURI());
        System.out.println("request.getUserPrincipal()");
        System.out.println(request.getUserPrincipal());
        System.out.println("SecurityContextHolder.getContext().getAuthentication()");
        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        if(SecurityContextHolder.getContext().getAuthentication() == null){
       //if(!( SecurityContextHolder.getContext().getAuthentication() != null &&
       //            SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) ) {
           //when Anonymous Authentication is enabl
           System.out.println("request.getRequestURI() x ");
           System.out.println(request.getRequestURI());
           extractor.extract(request)
                   .ifPresent(SecurityContextHolder.getContext()::setAuthentication);
       }


        filterChain.doFilter(request, response);
    }
}