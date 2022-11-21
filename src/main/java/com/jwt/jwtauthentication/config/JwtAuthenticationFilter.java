package com.jwt.jwtauthentication.config;

import com.jwt.jwtauthentication.helper.JwtUtil;
import com.jwt.jwtauthentication.services.CustomUserDetailsService;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //get JWT/ Header
        //Bearer
        //Validate
         String requestTokenHeader = request.getHeader("Authorization");
         String username=null;
         String jwtToken=null;
         //null and Format check
         if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer "))
         {
             jwtToken=requestTokenHeader.substring(7);

             try{
                 String JwtToken = null;
                 username = this.jwtUtil.getUsernameFromToken(JwtToken);

             }catch (Exception e)
             {
                 e.printStackTrace();
             }
         }
         UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
        //Security
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
             UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

             usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

             SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }else
        {
            System.out.println("Token is not Validated...");
        }
        filterChain.doFilter(request, response);

    }
}
