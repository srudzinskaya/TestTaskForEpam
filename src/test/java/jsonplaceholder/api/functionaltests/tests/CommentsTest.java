package jsonplaceholder.api.functionaltests.tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jsonplaceholder.api.functionaltests.model.CommentItem;
import jsonplaceholder.api.functionaltests.model.PostItem;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;

@Execution(ExecutionMode.CONCURRENT)
public class CommentsTest extends BaseTest {
    @Test
    public void getCommentsByPostIdRequestTest() {
        //find existed post resource
        int id = given().contentType(ContentType.JSON).when().get("/posts").jsonPath().getList("", PostItem.class).get(0).getId();
        CommentItem expectedCommentObject = new CommentItem(1, 1, "id labore ex et quam laborum", "Eliseo@gardner.biz", "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium");

        Response response = given().contentType(ContentType.JSON).pathParam("id", id).when().get("/posts/{id}/comments");

        //verify that status 200 is returned and response body is List of comment resources, verify correctness of one of those resources
        response.then().statusCode(HttpStatus.SC_OK);
        Assertions.assertFalse(response.jsonPath().getList("", CommentItem.class).isEmpty());
        Assertions.assertEquals(expectedCommentObject, response.jsonPath().getList("", CommentItem.class).get(0));
    }

    @Test
    public void getCommentsByNotExistedPostIdRequestTest() {
        //id for not existed post resource
        int id = 10000;

        Response response = given().contentType(ContentType.JSON).pathParam("id", id).when().get("/posts/{id}/comments");

        //verify that 200 status is returned with empty body
        response.then().statusCode(HttpStatus.SC_OK);
        Assertions.assertEquals("[]", response.then().extract().body().asString());
    }

    @Test
    public void getFilteringCommentsByPostIdRequestTest() {
        //find existed post resource
        int id = given().contentType(ContentType.JSON).when().get("/posts").jsonPath().getList("", PostItem.class).get(1).getId();
        CommentItem expectedCommentObject = new CommentItem(2, 6, "et fugit eligendi deleniti quidem qui sint nihil autem", "Presley.Mueller@myrl.com", "doloribus at sed quis culpa deserunt consectetur qui praesentium\naccusamus fugiat dicta\nvoluptatem rerum ut voluptate autem\nvoluptatem repellendus aspernatur dolorem in");

        Response response = given().contentType(ContentType.JSON).pathParam("id", id).when().get("/comments?postId={id}");

        //verify that status 200 is returned and response body is List of comment resources, verify correctness of one of those resources
        response.then().statusCode(HttpStatus.SC_OK);
        Assertions.assertFalse(response.jsonPath().getList("", CommentItem.class).isEmpty());
        Assertions.assertEquals(expectedCommentObject, response.jsonPath().getList("", CommentItem.class).get(0));
    }
}
