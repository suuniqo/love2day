package es.upm.fi.love2day.integration;

import es.upm.fi.love2day.model.Verification;
import es.upm.fi.love2day.model.VerificationStatus;
import es.upm.fi.love2day.repository.VerificationsRepository;
import es.upm.fi.love2day.repository.AccountsRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(RestAssuredTest.SyncAsyncTestConfig.class)
class RestAssuredTest {

    // Se desactiva la ejecución asíncrona
    // de los tests para evitar problemas.
    @TestConfiguration
    @EnableAsync
    static class SyncAsyncTestConfig {

        @Bean
        public Executor taskExecutor() {
            return Runnable::run;
        }
    }

    @LocalServerPort
    private int port;

    @Autowired
    private VerificationsRepository verificationsRepository;

    @Autowired
    private AccountsRepository accountRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        verificationsRepository.deleteAll();
        accountRepository.deleteAll();
    }

    // Crea una nueva cuenta y retorna su `userId`
    // a partir de una semilla dada, de forma que si dos
    // cuentas tienen distintas semillas se garantiza
    // que las cuentas no tengan ningún conflicto.
    // Retorna el `userId` de la nueva cuenta.
    private Long createAccountFrom(String seed) {
        String body = """
            {
              "username": "%s",
              "email": "%s",
              "password": "%s"
            }
            """.formatted(seed, seed + "@hotmail.com", "password");

        return given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/account")
        .then()
            .statusCode(201)
            .extract()
            .jsonPath().getLong("id");
    }

    // Crea una nueva cuenta y retorna su `userId`.
    private Long createAccount() {
        return createAccountFrom("a");
    }


    // =========================================================
    // POST /verification?userId={userId}
    // =========================================================

    // Fuerza un cambio en el `VerificationStatus` de la verificación del usuario.
    private void modifyStatus(Long userId, VerificationStatus status) {
        Verification existing = verificationsRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalStateException("User not found: " + userId));

        existing.setStatus(status);

        verificationsRepository.save(existing);
    }

    // V1: Partición Válida: La cuenta existe y no ha sido verificada.
    @Test
    void shouldReturn201WithInquiry_whenNoPriorVerificationExists() {
        Long userId = createAccount();

        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", userId)
        .when()
            .post("/verification")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("sessionToken", notNullValue());
    }

    // V2: Partición Válida: La cuenta existe y su última verificación fue rechazada.
    @Test
    void shouldReturn201WithInquiry_whenLastVerificationWasRejected() {
        Long userId = createAccount();

        modifyStatus(userId, VerificationStatus.REJECTED);

        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", userId)
        .when()
            .post("/verification")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("sessionToken", notNullValue());
    }

    // I1: Partición Inválida: La cuenta existe pero ya ha sido verificada (`VERIFIED`).
    @Test
    void shouldReturn409_whenUserIsAlreadyVerified() {
        Long userId = createAccount();

        modifyStatus(userId, VerificationStatus.VERIFIED);

        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", userId)
        .when()
            .post("/verification")
        .then()
            .statusCode(409);
    }

    // I2: Partición Inválida: La cuenta existe pero ya hay una verificación en proceso (`PENDING`).
    @Test
    void shouldReturn409_whenVerificationAlreadyInProgress() {
        Long userId = createAccount();

        modifyStatus(userId, VerificationStatus.PENDING);

        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", userId)
        .when()
            .post("/verification")
        .then()
            .statusCode(409);
    }

    // I3: Partición Inválida: El `userId` no corresponde a ninguna cuenta.
    @Test
    void shouldReturn404_whenUserDoesNotExist() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("userId", 99999L)  // no hay ninguna cuenta con este `userId`
        .when()
            .post("/verification")
        .then()
            .statusCode(404);
    }

    // I4: Partición Inválida: Falta el parámetro `userId` en el query.
    @Test
    void shouldReturn400_whenUserIdIsMissing() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/verification")
        .then()
            .statusCode(400);
    }

    // =========================================================
    // POST /swipe
    // =========================================================

    // V1: Partición Válida: El usuario hace swipe a otro usuario, pero no hay match (el otro usuario no ha hecho swipe).
    @Test
    void shouldReturn200WithSwipe_whenSwipeIsLikeAndNoMatchExists() {
        Long sourceId = createAccountFrom("1");
        Long targetId = createAccountFrom("2");

        String body = """
            {
                "targetId": "%d",
                "type": "LIKE"
            }
            """.formatted(targetId);

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .queryParam("userId", sourceId)
        .when()
            .post("/swipes")
        .then()
            .statusCode(200)
            .body("swipe.id", notNullValue())
            .body("swipe.type", equalTo("LIKE"))
            .body("match", nullValue());
    }

    // V2: Partición Válida: El usuario hace swipe a otro usuario y se produce un match (el otro usuario también lo ha hecho).
    @Test
    void shouldReturn200WithSwipe_whenSwipeIsLikeAndMatchExists() {
        Long sourceId = createAccountFrom("1");
        Long targetId = createAccountFrom("2");

        String swipe1 = """
            {
              "targetId": "%d",
              "type": "LIKE"
            }
            """.formatted(targetId);

        given()
            .contentType(ContentType.JSON)
            .body(swipe1)
            .queryParam("userId", sourceId)
        .when()
            .post("/swipes")
        .then()
            .statusCode(200)
            .body("swipe.id", notNullValue())
            .body("swipe.type", equalTo("LIKE"))
            .body("match", nullValue());

        String swipe2 = """
        {
            "targetId": "%d",
            "type": "LIKE"
        }
        """.formatted(sourceId);

        given()
            .contentType(ContentType.JSON)
            .body(swipe2)
            .queryParam("userId", targetId)
        .when()
            .post("/swipes")
        .then()
            .statusCode(200)
            .body("swipe.id", notNullValue())
            .body("swipe.type", equalTo("LIKE"))
            .body("match", notNullValue());
    }

    // V3: Partición Válida: El usuario hace swipe a otro usuario, pero no hay match (el otro usuario no ha hecho swipe).
    @Test
    void shouldReturn200WithSwipe_whenSwipeIsPassAndNoMatchExists() {
        Long sourceId = createAccountFrom("1");
        Long targetId = createAccountFrom("2");

        String body = """
         {
                "targetId": "%d",
                "type": "PASS"
         }
            """.formatted(targetId);
        given()
            .contentType(ContentType.JSON)
            .body(body)
            .queryParam("userId", sourceId)
        .when()
            .post("/swipes")
        .then()
            .statusCode(200)
            .body("swipe.id", notNullValue())
            .body("swipe.type", equalTo("PASS"))
            .body("match", nullValue());
    }

    // I1: Partición Inválida: El userId que hace el swipe no corresponde a ninguna cuenta.
    @Test
    void shouldReturn404_whenUserWhoSwipesDoesNotExist() {
        Long targetId = createAccount();

        String swipe = """
        {
          "targetId": "%d",
          "type": "LIKE"
        }
        """.formatted(targetId);

        given()
            .contentType(ContentType.JSON)
            .body(swipe)
            .queryParam("userId", 99999L)  
        .when()
            .post("/swipes")
        .then()
            .statusCode(404);
    }

    // I2: Partición Inválida: El userId del swipe no corresponde a ninguna cuenta.
    @Test
    void shouldReturn404_whenUserForSwipesDoesNotExist() {
        Long userId = createAccount();

        String swipe = """
        {
          "targetId": "99999",
          "type": "LIKE"
        }
        """; 

        given()
            .contentType(ContentType.JSON)
            .body(swipe)
            .queryParam("userId", userId)
        .when()
            .post("/swipes")
        .then()
            .statusCode(404);
    }

    //I3:Partición Inválida: No hay type del swipe.
    @Test
    void shouldReturn400_whenTypeIsMissing() {
        Long sourceId = createAccountFrom("1");
        Long targetId = createAccountFrom("2");

        String swipe1 = """
            {
                "targetId": "%d"
            }
            """.formatted(targetId);
        given()
            .contentType(ContentType.JSON)
            .body(swipe1)
            .queryParam("userId", sourceId)
        .when()
            .post("/swipes")
        .then()
            .statusCode(400);
    }

    //I4:Partición Inválida: El type del swipe no es válido.
    @Test
    void shouldReturn400_whenTypeIsInvalid() {
        Long userId = createAccountFrom("swipeua");
        Long targetId = createAccountFrom("swipeub");

        String swipe1 = """
            {
              "targetId": "%d",
              "type": "YES"
            }
            """.formatted(targetId);
        given()
            .contentType(ContentType.JSON)
            .body(swipe1)
            .queryParam("userId", userId)
        .when()
            .post("/swipes")
        .then()
            .statusCode(400);
    }

    //I5: Partición Inválida: El usuario hace swipe a sí mismo.
    @Test
    void shouldReturn400_whenUserSwipesToThemselves() {
        Long sourceId = createAccount();

        String swipe1 = """
            {
              "targetId": "%d",
              "type": "LIKE"
            }
            """.formatted(sourceId);

        given()
            .contentType(ContentType.JSON)
            .body(swipe1)
            .queryParam("userId", sourceId)
        .when()
            .post("/swipes")
        .then()
            .statusCode(400);
    }

    //I6: Partición Inválida: El usuario hace swipe a un usuario al que ya le hizo swipe.
    @Test
    void shouldReturn400_whenUserSwipesToUserTheyAlreadySwiped() {
        Long sourceId = createAccountFrom("1");
        Long targetId = createAccountFrom("2");

        String swipe1 = """
            {
              "targetId": "%d",
              "type": "LIKE"
            }
            """.formatted(targetId);

        given()
            .contentType(ContentType.JSON)
            .body(swipe1)
            .queryParam("userId", sourceId)
        .when()
            .post("/swipes")
        .then()
            .statusCode(200)
            .body("swipe.id", notNullValue())
            .body("swipe.type", equalTo("LIKE"))
            .body("match", nullValue());

        String swipe2 = """
        {
            "targetId": "%d",
            "type": "LIKE"
        }
        """.formatted(targetId);

        given()
            .contentType(ContentType.JSON)
            .body(swipe2)
            .queryParam("userId", sourceId)
        .when()
            .post("/swipes")
        .then()
            .statusCode(400);
    }
}
