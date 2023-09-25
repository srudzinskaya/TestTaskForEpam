package jsonplaceholder.api.functionaltests.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import jsonplaceholder.api.functionaltests.model.PostItem;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@Execution(ExecutionMode.CONCURRENT)
public class PostsTest extends BaseTest {

    @Test
    public void getPostsRequestTest() {
        Response response = given().contentType(JSON).when().get("/posts");

        //verify that status 200 is returned and response body is List of post resources
        response.then().statusCode(HttpStatus.SC_OK);
        Assertions.assertFalse(response.jsonPath().getList("", PostItem.class).isEmpty());
    }

    @Test
    public void getPostsByIdRequestTest() {
        //find id of existed post resource
        int id = given().contentType(JSON).
                when().get("/posts").jsonPath().getList("", PostItem.class).get(0).getId();

        Response response = given().contentType(JSON).pathParam("id", id).when().get("/posts/{id}");

        //verify that status 200 is returned and response body contains post resource with correct id
        response.then().statusCode(HttpStatus.SC_OK);
        PostItem responseObject = response.as(PostItem.class);
        Assertions.assertEquals(1, responseObject.getId());
    }

    @Test
    public void getPostsByNotExistedIdRequestTest() {
        //id of not existed post resource
        int id = 10000;

        Response response = given().contentType(JSON).pathParam("id", id).when().get("/posts/{id}");

        //verify that status 404 is returned
        response.then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void createPostTest() {
        //generate correct body with all accepted fields
        String requestBody = "{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}";

        Response response = given().contentType(JSON).body(requestBody).when().post("/posts");

        //verify that status 201 is returned and response body is correct
        response.then().statusCode(HttpStatus.SC_CREATED);
        PostItem responseBody = response.as(PostItem.class);
        Assertions.assertEquals("bar", responseBody.getBody());
        Assertions.assertEquals("foo", responseBody.getTitle());
        Assertions.assertEquals(1, responseBody.getUserId());
        Assertions.assertEquals(101, responseBody.getId());
    }

    @Test
    public void createPostWithIncorrectStructureTest() {
        //generate request body with not accepted field
        String requestBody = "{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1, \"value\" = \"value\"}";

        Response response = given().contentType(JSON).body(requestBody).when().post("/posts");

        //verify that status 500 is returned
        response.then().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void updatePostTest() throws JsonProcessingException {
        //find id of existed post resource and change value for one field
        PostItem postForUpdate = given().contentType(JSON).
                when().get("/posts").jsonPath().getList("", PostItem.class).get(0);
        int id = postForUpdate.getId();
        postForUpdate.setBody("new body");
        String requestBody = objectMapper.writeValueAsString(postForUpdate);

        Response response = given().contentType(JSON).body(requestBody).pathParam("id", id).
                when().put("/posts/{id}");

        //verify that status 200 is returned and body is correct
        response.then().statusCode(HttpStatus.SC_OK);
        PostItem responseBody = response.as(PostItem.class);
        Assertions.assertEquals(postForUpdate.getBody(), responseBody.getBody());
        Assertions.assertEquals(postForUpdate.getTitle(), responseBody.getTitle());
        Assertions.assertEquals(postForUpdate.getUserId(), responseBody.getUserId());
        Assertions.assertEquals(id, responseBody.getId());
    }

    @Test
    public void updateNotExistedPostTest() {
        //id of not existed post resource
        int id = 1000;
        String requestBody = "{\"userId\": 1000, \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}";

        Response response = given().contentType(JSON).body(requestBody).pathParam("id", id).
                when().put("/posts/{id}");

        //verify that status 500 is returned
        response.then().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void patchPostTest() {
        //find id of existed post resource
        PostItem postForUpdate = given().contentType(JSON).
                when().get("/posts").jsonPath().getList("", PostItem.class).get(0);
        int id = postForUpdate.getId();

        //value that should be updated
        String requestBody = "{\"body\": \"new body\"}";

        Response response = given().contentType(JSON).body(requestBody).pathParam("id", id).
                when().patch("/posts/{id}");

        //verify that status 200 is returned and body is correct
        response.then().statusCode(HttpStatus.SC_OK);
        PostItem responseBody = response.as(PostItem.class);
        Assertions.assertEquals("new body", responseBody.getBody());
        Assertions.assertEquals(postForUpdate.getTitle(), responseBody.getTitle());
        Assertions.assertEquals(postForUpdate.getUserId(), responseBody.getUserId());
        Assertions.assertEquals(id, responseBody.getId());
    }

    @Test
    public void deletePostTest() {
        //find id of existed post resource
        PostItem postForDelete = given().contentType(JSON).
                when().get("/posts").jsonPath().getList("", PostItem.class).get(0);
        int id = postForDelete.getId();

        Response response = given().pathParam("id", id).when().delete("/posts/{id}");

        //verify that status 200 is returned
        response.then().statusCode(HttpStatus.SC_OK);
    }

}

