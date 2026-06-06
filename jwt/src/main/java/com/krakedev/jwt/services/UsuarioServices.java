package com.krakedev.jwt.services;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krakedev.jwt.entidades.Usuario;
import com.krakedev.jwt.entidades.repositories.UsuarioRepositorio;



@Service
public class UsuarioServices {
	private final UsuarioRepositorio usuarioRepository;

    // Inyección de dependencia por constructor
	@Autowired
    public UsuarioServices(UsuarioRepositorio usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}

    /**
     * Método para guardar un usuario
     */
    public Usuario guardar(Usuario usuario) {
    	
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new RuntimeException("El username es obligatorio");
        }
        
        if (usuario.getUsername().length() < 3) {
            throw new RuntimeException("El username debe tener al menos 3 caracteres");
        }
        
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            throw new RuntimeException("El rol es obligatorio");
        }
        
        // Verificar si el username ya existe
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El username '" + usuario.getUsername() + "' ya está en uso");
        }
        
        return usuarioRepository.save(usuario);
    }



	/**
     * Método para autenticar un usuario
     */
    public boolean autenticar(String username, String password) {
        // Validación manual
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        // Buscar al usuario por username
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        
        // Verificar si el usuario existe
        if (usuarioOpt.isEmpty()) {
            return false;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Validar la contraseña
        if (!password.equals(usuario.getPassword())) {
            return false;
        }
        
        return true;
    }

    /**
     * Método para buscar un usuario por username
     */
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Método para obtener todos los usuarios
     */
    public java.util.List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Método para buscar un usuario por ID
     */
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}
