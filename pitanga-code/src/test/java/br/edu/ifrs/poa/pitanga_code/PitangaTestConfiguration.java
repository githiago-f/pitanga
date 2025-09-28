package br.edu.ifrs.poa.pitanga_code;

import java.nio.file.Path;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import br.edu.ifrs.poa.pitanga_code.infra.sandbox.SandboxProvider;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.SandboxResult;
import br.edu.ifrs.poa.pitanga_code.infra.sandbox.dto.BuildDTO;

@TestConfiguration
public class PitangaTestConfiguration {
    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "test-user")
                .claim("scope", "read")
                .build();
    }

    @Bean
    @Profile("test")
    public SandboxProvider sandbox() {
        return new SandboxProvider() {
            @Override
            public Box setup(BuildDTO buildDTO) {
                return new Box(1, Path.of(""));
            }

            @Override
            public void cleanup(Box box) {
            }

            @Override
            public SandboxResult execute(Box box, BuildDTO buildDTO, String inputLine) {
                return new SandboxResult("Hello, World!");
            }
        };
    }
}
