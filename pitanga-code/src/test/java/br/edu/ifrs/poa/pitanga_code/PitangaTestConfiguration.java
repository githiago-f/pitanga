package br.edu.ifrs.poa.pitanga_code;

import java.util.List;

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
            public Integer setup(BuildDTO buildDTO) {
                return 1;
            }

            @Override
            public void cleanup(Integer identifier) {
            }

            @Override
            public SandboxResult execute(Integer identifier, BuildDTO buildDTO) {
                return new SandboxResult("Hello, World!", "");
            }
        };
    }
}
