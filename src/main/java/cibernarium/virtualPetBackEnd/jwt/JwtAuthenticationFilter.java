package cibernarium.virtualPetBackEnd.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;



@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    // LOGERS OK! TOKEN RECIBIDO, SEGUIMOS CON EL FLUJO DEL PROGRAMA EN: ??
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String token = getTokenFromRequest(request);
        if (token == null) {
            logger.warn("No JWT token provided in request");
            filterChain.doFilter(request, response);
            return;
        }

        final String username;

        // Verificar si el token es nulo, aunque ya se haya verificado antes
        if (token == null) {
            logger.warn("Token is null in the request");
            filterChain.doFilter(request, response);
            return;
        }

        username = jwtService.getUsernameFromToken(token);
        logger.info("Token recibido: " + token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Usuario extraído del token: " + username);

            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.info("Detalles del usuario cargados: " + userDetails.getUsername());

                if (jwtService.isTokenValid(token, userDetails)) {
                    logger.info("Token validado con éxito para el usuario: " + username);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
// ESTAMOS AQUI!!!!!! ///
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Autenticación configurada para el usuario: " + username);
                } else {
                    logger.error("Token inválido para el usuario: " + username);
                }
            } catch (Exception e) {
                logger.error("Error al cargar los detalles del usuario o validar el token", e);
            }
        } else {
            logger.warn("No se pudo extraer el nombre de usuario o ya está autenticado");
        }

        filterChain.doFilter(request, response);
    }


// METODO BASE (tiene algunos loggers)

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        final String token = getTokenFromRequest(request);
//        if (token == null) {
//            logger.warn("No JWT token provided in request");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        final String username;
//
//        if (token==null)
//        {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        username=jwtService.getUsernameFromToken(token);
//        logger.info("Token recibido: " + token);
//
//        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
//        {
//            UserDetails userDetails=userDetailsService.loadUserByUsername(username);
//
//            if (jwtService.isTokenValid(token, userDetails))
//            {
//                UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities());
//
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//
//            }
//
//        }
//
//        filterChain.doFilter(request, response);
//    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer "))
        {
            return authHeader.substring(7);
        }
        return null;
    }


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        final String token = getTokenFromRequest(request);
//
//        if (token == null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    // Este metodo obtiene el header del token (El header contiene tipo de token y el algoritmo usado)
//    private String getTokenFromRequest(HttpServletRequest request) {
//
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")) {
//            return authHeader.substring(7);
//        }
//     return null; // (?) mirar diferentes opciones para que la aplicación no continúe de modo silencioso tras un token inexistente
//
//    }







}
