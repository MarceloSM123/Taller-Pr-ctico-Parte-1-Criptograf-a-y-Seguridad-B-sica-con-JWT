package com.krakedev.jwt.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import com.krakedev.jwt.entidades.Usuario;
import com.krakedev.jwt.entidades.repositories.UsuarioRepositorio;
import com.krakedev.jwt.services.UsuarioServices;
import com.krakedev.jwt.utils.jwtUtils;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final UsuarioServices usuarioService;
	private final UsuarioRepositorio usuarioRepository;
	
	public AuthController(UsuarioServices usuarioService, UsuarioRepositorio usuarioRepository) {
		super();
		this.usuarioService = usuarioService;
		this.usuarioRepository = usuarioRepository;
	}



	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
	    try {
	        Usuario usuarioNuevo = usuarioService.guardar(usuario);
	        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error al registrar usuario: " + e.getMessage());
	    }
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
	    String username = credenciales.get("username");
	    String password = credenciales.get("password");
	    
	    boolean autenticado = usuarioService.autenticar(username, password);
	    
	    if (autenticado) {
	        Usuario usuario = usuarioRepository.findByUsername(username).get();
	        String token = jwtUtils.generarToken(usuario.getUsername(),usuario.getRol());
	        return ResponseEntity.ok(Map.of("Token: ",token));
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrecta");
	    }
	}
	
	@GetMapping("/perfil")
	public ResponseEntity<?> verPerfil(@RequestHeader(value = "Authorization", required = false) String authHeader) {

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {

	    	
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("Acceso Denegado: Debes proveer un token Bearer valido en la cabecera Authorization");

	    }

	    String token = authHeader.substring(7);

	    DecodedJWT datosToken = jwtUtils.validarToken(token);

	    if (datosToken == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso Denegado: Token Inválido o Expirado");
	    }

	    String usuario = datosToken.getSubject();

	    String rol = datosToken.getClaim("rol").asString();

	    return ResponseEntity.ok(Map.of(
	    		"Mensaje", "Bienvenido al sistema protegido por jwt", "Usuario", usuario, "Rol", rol, 
	    		"Estatus", "Autenticado Exitosamente"
	    ));
	}
}
