package com.krakedev.jwt.utils;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class jwtUtils {
	private static final String CLAVE_SECRETA = "EstaEsUnaClaveSuperSecretaYMuyLarga1234567890!";
	private static final String EMISOR = "KrakeDevBackend";
	private static final long TIEMPO_EXPIRACION = 1800000;

	public static String generarToken(String username, String rol) {
		Algorithm algoritmo = Algorithm.HMAC256(CLAVE_SECRETA);
		long tiempoActual = System.currentTimeMillis();
		Date fechaExpiracion = new Date(tiempoActual + TIEMPO_EXPIRACION);

		String tokenGenerado = JWT.create().withIssuer(EMISOR).withSubject(username)
				.withIssuedAt(new Date(tiempoActual)).withExpiresAt(fechaExpiracion).withClaim("rol", rol)
				.sign(algoritmo);

		return tokenGenerado;
	}

	public static DecodedJWT validarToken(String token) {
		try {
		    Algorithm algoritmo = Algorithm.HMAC256(CLAVE_SECRETA);
		    
		    JWTVerifier verificador = JWT.require(algoritmo)
		        .withIssuer(EMISOR)
		        .build();
		    
		    DecodedJWT tokenDecodificado = verificador.verify(token);
		    
		    return tokenDecodificado;
		} catch (Exception e) {
			System.err.println("Error de validación del Token: " + e.getMessage());
		    
		    return null;
		}
	}
}
