package com.curso01.curso01.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import org.slf4j.Logger;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {
        // EL @Value CARGA LAS PROPIEDADES DESCRITAS ENTRE () QUE TENGAMOS EN EL
        // ApplicationProperties EN LAS VARIABLES KEY - ISSUER - TTLMILLIS.
        @Value("${security.jwt.secret}")
        private String key;
        @Value("${security.jwt.issuer}")
        private String issuer;
        @Value("${security.jwt.ttlMillis}")
        private long ttlMillis;

        private final Logger log = LoggerFactory
                        .getLogger(JWTUtil.class);

        // ESTA FUNCIÓN CREA EL JWT, EL TEXTO QUE SE LE PASA AL CLIENTE DESDE EL
        // SERVIDOR.
        /**
         * Create a new token.
         *
         * @param id
         * @param subject
         * @return
         */
        public String create(String id, String subject) {
                byte[] apiKeySecretBytes = Base64.getDecoder().decode(key);

                Key signingKey = new SecretKeySpec(apiKeySecretBytes,
                                SignatureAlgorithm.HS256.getJcaName());

                String jwt = Jwts.builder()
                                .setSubject(subject)
                                .setIssuer(issuer)
                                .setId(id)
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                                .signWith(signingKey, SignatureAlgorithm.HS256)
                                .compact();

                return jwt;
        }

        // getValue Y getkey, DEVUELVEN LA INFORMACIÓN QUE LE HAYAMOS CARGADO AL
        // TOKEN(NOMBRE USUARIO, TELÉFONO, ID, ROLES DE USUARIO,PERMISOS, ETC).

        /**
         * Method to validate and read the JWT
         *
         * @param jwt
         * @return
         */
        public String getValue(String jwt) {
                // This line will throw an exception if it is not a signed JWS (as
                // expected)
                Claims claims = Jwts.parser().setSigningKey(Base64.getDecoder().decode(key))
                                .parseClaimsJws(jwt).getBody();

                return claims.getSubject();
        }

        /**
         * Method to validate and read the JWT
         *
         * @param jwt
         * @return
         */
        public String getKey(String jwt) {
                byte[] decodedKey = Base64.getDecoder().decode(key);
                Key signingKey = Keys.hmacShaKeyFor(decodedKey);

                JwtParser parser = Jwts.parserBuilder()
                                .setSigningKey(signingKey)
                                .build();

                Claims claims = parser.parseClaimsJws(jwt).getBody();
                return claims.getId();
        }
}
