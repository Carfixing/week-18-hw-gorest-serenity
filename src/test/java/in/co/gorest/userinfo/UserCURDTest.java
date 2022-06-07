package in.co.gorest.userinfo;

import in.co.gorest.constants.EndPoints;
import in.co.gorest.model.UserPojo;
import in.co.gorest.testbase.TestBase;
import in.co.gorest.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasValue;


public class UserCURDTest extends TestBase {


    static String name = "Sorya" + TestUtils.getRandomValue();
    static String email = TestUtils.getRandomValue() + "sorya@gmail.com";
    static String gender = "male";
    static String status = "active";
    static int userId;

    @Title("This will create a new user")
    @Test
    public void test001() {
        UserPojo userPojo = new UserPojo();
        userPojo.setName(name);
        userPojo.setEmail(email);
        userPojo.setGender(gender);
        userPojo.setStatus(status);

        HashMap<String, Object> header1 = new HashMap<>();
        header1.put("Content-Type", "application/json");
        header1.put("Accept", "application/json");
        header1.put("Authorization", "Bearer 116561e2ecb0e825b61a7a3c3f97b96db243d76f8979fe4e8865272be0ca3b0f");
        SerenityRest.given().log().all()
                .headers(header1)
                .body(userPojo)
                .when()
                .post(EndPoints.CREATE_USER)
                .then().statusCode(201).log().all();
    }

    @Title("Verify if the user was added to the application for name=Himadri Kocchar")
    @Test
    public void test002() {
        name = "Himadri Kocchar";
        String p1 = "findAll{it.name=='";
        String p2 = "'}.get(0)";
        HashMap<String, Object> userMap = SerenityRest.given().log().all()
                .when()
                .get(EndPoints.GET_ALL_USERS)
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + name + p2);
        Assert.assertThat(userMap, hasValue(name));
        userId = (int) userMap.get("id");
        System.out.println(userId);
    }

    @Title("Update the user information and verify the updated information for ID=3866")
    @Test
    public void test003() {
        name = "Snehal";
        gender="female";
        email="snehal@gmail.com";
        status="active";
        userId = 3866;
        UserPojo userPojo = new UserPojo();
        userPojo.setName(name);
        userPojo.setEmail(email);
        userPojo.setGender(gender);
        userPojo.setStatus(status);

        HashMap<String, Object> header1 = new HashMap<>();
        header1.put("Accept", "application/json");
        header1.put("Authorization", "Bearer 116561e2ecb0e825b61a7a3c3f97b96db243d76f8979fe4e8865272be0ca3b0f");

        SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .headers(header1)
                .pathParam("userID", userId)
                .body(userPojo)
                .when()
                .put(EndPoints.UPDATE_USER_BY_ID)
                .then().statusCode(200).log().body().body("name", equalTo(name), "email", equalTo(email));

//        String p1 = "findAll{it.name=='";
//        String p2 = "'}.get(0)";
//        HashMap<String, Object> userMap = SerenityRest.given().log().all()
//                .when()
//                .get(EndPoints.GET_ALL_USERS)
//                .then()
//                .statusCode(200)
//                .extract()
//                .path(p1 + name + p2);
//        Assert.assertThat(userMap, hasValue(name));
//        userId = (int) userMap.get("id");
//        System.out.println(userId);

    }

    @Title("Delete the user and verify if the user is deleted! for ID=3865")
    @Test
    public void test004() {
        userId = 3865;

        HashMap<String, Object> header1 = new HashMap<>();
        header1.put("Accept", "application/json");
        header1.put("Authorization", "Bearer 116561e2ecb0e825b61a7a3c3f97b96db243d76f8979fe4e8865272be0ca3b0f");

        SerenityRest.given().log().all()
                .headers(header1)
                .pathParam("userID", userId)
                .when()
                .delete(EndPoints.DELETE_USER_BY_ID)
                .then().statusCode(204)
                .log().status();

        SerenityRest.given().log().all()
                .pathParam("userID", userId)
                .when()
                .get(EndPoints.GET_SINGLE_USER_BY_ID)
                .then()
                .statusCode(404).log().status();
    }
}
