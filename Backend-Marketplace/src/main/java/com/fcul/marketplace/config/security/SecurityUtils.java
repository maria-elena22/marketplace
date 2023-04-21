package com.fcul.marketplace.config.security;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.CreatedUser;
import com.auth0.json.auth.TokenHolder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.net.Response;
import com.auth0.net.SignUpRequest;
import com.auth0.net.TokenRequest;
import com.fcul.marketplace.exceptions.JWTTokenMissingException;
import com.fcul.marketplace.exceptions.SignUpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SecurityUtils {

    @Autowired
    AuthAPI authAPI;

    @Value("${auth0.client-secret}")
    public String secret;

    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(token);
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes());
    }


    public String getToken(String authorizationHeader) throws JWTTokenMissingException {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        } else {
            throw new JWTTokenMissingException("Authorization Token is missing");
        }

    }

    public String generateToken(String email, String password) throws Auth0Exception {
        TokenRequest tokenRequest = authAPI.login(email, password);
        Response<TokenHolder> tokenHolderResponse = tokenRequest.execute();
        return tokenHolderResponse.getBody().getIdToken();
    }

    public CreatedUser registerUser(String email, String password, String role) throws SignUpException, Auth0Exception {
        SignUpRequest signUpRequest =authAPI.signUp(email,password.toCharArray(),"Username-Password-Authentication");

        signUpRequest.setCustomFields(Map.of("role", role));


        Response<CreatedUser> createdUserResponse = null;
        try {
            createdUserResponse = signUpRequest.execute();
        } catch (APIException e) {
            if(e.getError().equals("invalid_password")){
                throw new SignUpException("Password too weak");
            }
            if(e.getError().equals("invalid_signup")){
                throw new SignUpException("Email already registered");
            }
        }
        return createdUserResponse.getBody();

    }


    public String getEmailFromAuthHeader(String authorizationHeader) throws JWTTokenMissingException{
        String token = getToken(authorizationHeader);
        return verifyToken(token).getClaim("email").asString();
    }

    public String getRoleFromAuthHeader(String authorizationHeader) throws JWTTokenMissingException{
        String token = getToken(authorizationHeader);
        return verifyToken(token).getClaim("role").asString().substring(5);
    }
}

