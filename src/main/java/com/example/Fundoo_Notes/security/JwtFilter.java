package com.example.Fundoo_Notes.security;

import com.example.Fundoo_Notes.entity.User;
import com.example.Fundoo_Notes.repository.UserRepository;
import com.example.Fundoo_Notes.service.RedisTokenService;
import com.example.Fundoo_Notes.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTokenService redisTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("✅ JWT FILTER HIT");

        String header = request.getHeader("Authorization");

        // ✅ Step 1: Check header
        if (header != null) {
            String token = header.startsWith("Bearer ") ? header.substring(7) : header;

            try {
                // ✅ Step 2: Extract email
                String email = jwtUtil.extractEmail(token);

                // ✅ Step 3: Avoid duplicate auth
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // ✅ Step 4: Validate token
                    if (jwtUtil.validateToken(token) && redisTokenService.isJwtCached(token)) {

                        // ✅ Step 5: Fetch user from DB
                        User user = userRepository.findByEmail(email)
                                .orElse(null);

                        if (user != null) {

                            // ✅ Step 6: Set ROLE properly
                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(
                                            user.getEmail(),
                                            null,
                                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                                    );

                            auth.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );

                            // ✅ Step 7: Set security context
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("❌ JWT ERROR: " + e.getMessage());
            }
        }

        // ✅ Step 8: Continue filter chain
        filterChain.doFilter(request, response);
    }
}