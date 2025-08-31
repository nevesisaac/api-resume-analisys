package org.acme;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.util.UUID;




@QuarkusTest
class GreetingResourceTest {

  // @Test
  void testHelloEndpoint() {
    given()
        .when().get("/hello")
        .then()
        .statusCode(200)
        .body(is("Hello from Quarkus REST"));
  }

  //  @Test
    void testUploadEndpoint() {
        File testFile = new File("src/test/resources/curriculo.pdf");

        given()
            .multiPart("userId", UUID.fromString("2eb4e53a-d3a8-4524-9bdc-3383bae498bc").toString())
            .multiPart("file", testFile, "application/pdf") // campo do tipo FileUpload
            .multiPart("fileName", "curriculo.pdf") // campo extra se tiver
            .when()
            .post("/resume/upload")
            .then()
            .statusCode(202);
    }

}