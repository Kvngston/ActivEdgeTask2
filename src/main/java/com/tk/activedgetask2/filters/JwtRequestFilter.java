package com.tk.activedgetask2.filters;

import com.tk.activedgetask2.entity.TokenBlackList;
import com.tk.activedgetask2.exception.SecurityException;
import com.tk.activedgetask2.repository.TokenBlackListRepository;
import com.tk.activedgetask2.security.CustomUserDetailsService;
import com.tk.activedgetask2.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.SneakyThrows;
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
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlackListRepository tokenBlackListRepository;


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String username;
        String jwt;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);


                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){

                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                    TokenBlackList tokenBlackList = tokenBlackListRepository.findByToken(jwt);

                    if (tokenBlackList != null){
                        throw new SecurityException("Session has expired, Please Login");
                    }

                    if (jwtUtil.validateToken(jwt,userDetails)){
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }


            }catch (ExpiredJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException ex){
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return;
            } catch (MalformedJwtException ex){
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Token");
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
