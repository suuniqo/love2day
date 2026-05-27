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
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestAssuredTest {

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

    // =========================================================
    // POST /verification?userId={userId}
    // =========================================================

    // Crea una nueva cuenta y retorna su `userId`.
    private Long createAccount() {
        String body = """
            {
              "username": "testuser",
              "email": "test@example.com",
              "password": "secret123"
            }
            """;

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
}
