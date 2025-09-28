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
import static io.restassured.http.ContentType.JSON;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
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
    void givenProblemsList_whenGetInvalidProblem_thenStatus404() {
        given().contentType(JSON)
                .header("Authorization", "Bearer fake-token")
                .when().get("/problems/any")
                .then().log().ifValidationFails()
                .statusCode(404).body(emptyString());
    }

    @Test
    void givenProblemsList_whenGetProblemsList_thenStatus200() {
        var builder = Problem.builder()
                .description("...")
                .creator("1234")
                .difficultyLevel(Difficulty.EASY);

        List<Problem> problems = List.of(
                builder.title("Traverse a tree in-order")
                        .slug("traverse-a-tree-in-order")
                        .build(),
                builder.title("Revert a linked list")
                        .slug("revert-a-linked-list")
                        .build());
        problemsRepository.saveAll(problems);

        ValidatableResponse validation = given()
                .header("Authorization", "Bearer fake-token")
                .contentType(JSON)
                .when()
                .get("/problems")
                .then().log().ifValidationFails()
                .statusCode(200).body("data", hasSize(2));

        for (int i = 0; i < problems.size(); i++) {
            Problem problem = problems.get(i);
            validation = validation.body("data[" + i + "].description", nullValue())
                    .body("data[" + i + "].title", equalTo(problem.getTitle()));
        }
    }

    @Test
    void givenNoProblemWithSlugExists_whenCreatingANewProblem_thenResourceIsAvailableAndStatus201() {
        Long sum = problemsRepository.count();
        assertThat("there are no problems on the list", sum == 0);

        String slug = "traverse-a-tree-in-order-i";
        String title = "Traverse a tree in-order";
        String bodyJson = String.format("""
                    {
                        "title": "%s",
                        "slug": "%s",
                        "description": "...",
                        "initialDifficultyLevel": "HARD",
                        "testingScenarios": [
                            { "input": "[0, 1, 2]", "isExample": false },
                            { "input": "[4, 3, 2]", "isExample": true }
                        ]
                    }
                """, title, slug);

        given().contentType(JSON)
                .header("Authorization", "Bearer fake-token")
                .body(bodyJson)
                .when().post("/problems")
                .then().log().ifValidationFails()
                .statusCode(201)
                .header("Location", equalTo("/problems/" + slug))
                .body("id", notNullValue())
                .body("slug", equalTo(slug))
                .body("title", equalTo(title));

        given().contentType(JSON)
                .header("Authorization", "Bearer fake-token")
                .when().get("/problems/" + slug)
                .then().log().ifValidationFails()
                .body("title", equalTo(title))
                .body("scenarios", hasSize(1))
                .body("scenarios[0].isExample", equalTo(true));
    }

    @Test
    void givenProblemWithSLugExists_whenCreatingProblem_thenStatus409() {
        Long sum = problemsRepository.count();
        assertThat("there are no problems on the list", sum == 0);

        String slug = "traverse-a-tree-in-order-i";
        String title = "Traverse a tree in-order";
        String bodyJson = String.format("""
                    {
                        "title": "%s",
                        "slug": "%s",
                        "description": "...",
                        "initialDifficultyLevel": "HARD",
                        "testingScenarios": [
                            { "input": "[0, 1, 2]", "isExample": false },
                            { "input": "[4, 3, 2]", "isExample": true }
                        ]
                    }
                """, title, slug);

        given().contentType(JSON)
                .body(bodyJson)
                .header("Authorization", "Bearer fake-token")
                .when().post("/problems")
                .then().statusCode(201);

        given().contentType(JSON)
                .body(bodyJson)
                .header("Authorization", "Bearer fake-token")
                .when().post("/problems")
                .then().statusCode(409)
                .header("Location", "/problems/" + slug)
                .body(emptyString());
    }
}
