package br.edu.ifrs.poa.pitanga_code.app.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import br.edu.ifrs.poa.pitanga_code.PitangaTestConfiguration;
import br.edu.ifrs.poa.pitanga_code.PostgresTestConfiguration;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.CreateProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.Difficulty;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.HashSet;
import java.util.List;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = PitangaTestConfiguration.class)
public class ProblemsControllerTests extends PostgresTestConfiguration {
    @LocalServerPort
    public Integer port;

    @Autowired
    private CreateProblemsRepository problemsRepository;

    @BeforeEach
    void up() {
        RestAssured.baseURI = "https://localhost:" + port;
        problemsRepository.deleteAll();
    }

    @Test
    public void givenProblemsList_whenGetProblemBySlug_thenStatus200() {
        List<Problem> problems = List.of(
                new Problem("Problema 1", "Problema numero 1", "criador", Difficulty.EASY, new HashSet<>()),
                new Problem("Problema 2", "Problema numero 2", "criador", Difficulty.HARD, new HashSet<>()));
        problemsRepository.saveAll(problems);

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer fake-token")
                .when()
                .get("/problems/problema-1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Problema 1"));

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer fake-token")
                .when()
                .get("/problems/problema-2")
                .then()
                .statusCode(200)
                .body("title", equalTo("Problema 2"));
    }

    @Test
    public void givenProblemsList_whenGetProblems_thenStatus200() {
        List<Problem> problems = List.of(
                new Problem("Problema 1", "Problema numero 1", "criador", Difficulty.EASY, new HashSet<>()),
                new Problem("Problema 2", "Problema numero 2", "criador", Difficulty.HARD, new HashSet<>()));
        problemsRepository.saveAll(problems);

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer fake-token")
                .when()
                .get("/problems")
                .then()
                .statusCode(200)
                .body("data", hasSize(2))
                .body("data[0].title", equalTo("Problema 1"));
    }
}
