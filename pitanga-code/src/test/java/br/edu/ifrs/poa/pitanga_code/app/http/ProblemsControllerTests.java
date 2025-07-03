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
import br.edu.ifrs.poa.pitanga_code.app.dtos.CreateProblemCommand;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.entities.Problem;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.repository.CreateProblemsRepository;
import br.edu.ifrs.poa.pitanga_code.domain.pbl.vo.Difficulty;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
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
    void givenProblemsList_whenGetProblemBySlug_thenStatus200() {
        List<Problem> problems = List.of(
                new Problem("Traverse a tree in-order", "...", "1234", Difficulty.EASY, new HashSet<>()),
                new Problem("Revert a linked list", "...", "1234", Difficulty.HARD, new HashSet<>()));
        problemsRepository.saveAll(problems);

        for (Problem problem : problems) {
            given().contentType(ContentType.JSON)
                    .header("Authorization", "Bearer fake-token")
                    .when()
                    .get("/problems/" + problem.getSlug())
                    .then()
                    .statusCode(200)
                    .body("title", equalTo(problem.getTitle()));
        }
    }

    @Test
    void givenProblemsList_whenGetProblems_thenStatus200() {
        List<Problem> problems = List.of(
                new Problem("Traverse a tree in-order", "...", "1234", Difficulty.EASY, new HashSet<>()),
                new Problem("Revert a linked list", "...", "1234", Difficulty.HARD, new HashSet<>()));
        problemsRepository.saveAll(problems);

        ValidatableResponse validation = given()
                .header("Authorization", "Bearer fake-token")
                .contentType(ContentType.JSON)
                .when()
                .get("/problems")
                .then()
                .statusCode(200)
                .body("data", hasSize(2));

        int i = 0;
        for (Problem problem : problems) {
            validation.body("data[" + i + "].description", nullValue());
            validation.body("data[" + i + "].title", equalTo(problem.getTitle()));

            i++;
        }
    }

    @Test
    void givenProblemsListEmpty_whenCreateNewProblem_thenStatus201() {
        Long sum = problemsRepository.count();
        assertThat("there are no problems on the list", sum == 0);

        String bodyJson = """
                {
                    "title": "Traverse a tree in-order",
                    "description": "...",
                    "initialDifficultyLevel": "HARD",
                    "allowedLanguages": [],
                    "testingScenarios": [{ "input": "[0, 1, 2]", "isExample": false }]
                }
                """;

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer fake-token")
                .body(bodyJson)
                .when().post("/problems")
                .then().statusCode(201)
                .body("id", notNullValue())
                .body("slug", equalTo("traverse-a-tree-in-order"))
                .body("title", equalTo("Traverse a tree in-order"));
    }
}
