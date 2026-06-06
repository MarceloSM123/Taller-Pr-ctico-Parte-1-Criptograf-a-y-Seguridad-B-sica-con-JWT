package com.krakedev.jwt.entidades.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krakedev.jwt.entidades.Usuario;


public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
Optional<Usuario> findByUsername(String username);
    
    // Método para verificar si existe un username
    boolean existsByUsername(String username);
}
