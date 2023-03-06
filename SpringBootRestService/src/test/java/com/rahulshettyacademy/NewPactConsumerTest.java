package com.rahulshettyacademy;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahulshettyacademy.controller.LibraryController;
import com.rahulshettyacademy.controller.ProductsPrices;
import com.rahulshettyacademy.controller.SpecificProduct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "CoursesCatalogue")
public class NewPactConsumerTest {

    @Autowired
    private LibraryController libraryController;


    @Pact(consumer ="BooksCatalogue")
    public RequestResponsePact pactAllCoursesDetailsPriceCheck(PactDslWithProvider builder){
        return builder.given("courses exist")
                .uponReceiving("getting all course details")
                .path("/allCourseDetails")
                .willRespondWith()
                .status(200)
                .body(PactDslJsonArray.arrayMinLike(3)
                        .integerType("price",12)
                        .closeObject())
                .toPact();

    }

    @Pact(consumer = "BooksCatalogue")
    public RequestResponsePact getCourseByName(PactDslWithProvider builder){
        return builder.given("Course Appium Exists")
                .uponReceiving("Get the appium course details")
                .path("/getCourseByName/Appium")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .integerType("price", 44)
                        .stringType("category", "mobile")
                        ).toPact();
    }

    @Pact(consumer = "BooksCatalogue")
    public RequestResponsePact getCourseByNameNotExist(PactDslWithProvider builder)

    {
        return builder.given("Course Appium does not exist","name","Appium")
                .uponReceiving("Appium course Does not exist")
                .path("/getCourseByName/Appium")
                .willRespondWith()
                .status(404)
                .toPact();

    }
   @Test
   @PactTestFor(pactMethod = "pactAllCoursesDetailsPriceCheck", port = "9999")
    public void testAllProductSum(MockServer mockServer) throws JsonProcessingException {


       String expectedJson = "{\"booksPrice\":250,\"coursesPrice\":36}";

        libraryController.setBaseUrl(mockServer.getUrl());
        ProductsPrices productsPrices =libraryController.getProductPrices();
        ObjectMapper obj = new ObjectMapper();
        String jsonActual = obj.writeValueAsString(productsPrices);

       Assertions.assertEquals(expectedJson,jsonActual);
   }

   @Test
   @PactTestFor(pactMethod = "getCourseByName", port = "9999")
    public void testByProductName(MockServer mockServer) throws JsonProcessingException {
       String expectJson = "{\"product\":{\"book_name\":\"Appium\",\"id\":\"ttefs36\",\"isbn\":\"ttefs\",\"aisle\":36,\"author\":\"Shetty\"},\"price\":44,\"category\":\"mobile\"}";
       libraryController.setBaseUrl(mockServer.getUrl());
        SpecificProduct specificProduct = libraryController.getProductFullDetails("Appium");
        ObjectMapper obj = new ObjectMapper();
        String actualJson = obj.writeValueAsString(specificProduct);

        Assertions.assertEquals(expectJson, actualJson);
    }

    @Test
    @PactTestFor(pactMethod = "getCourseByNameNotExist", port = "9999")
    public void testByProductNameNotExists(MockServer mockServer) throws JsonProcessingException {
        String expectJson = "{\"product\":{\"book_name\":\"Appium\",\"id\":\"ttefs36\",\"isbn\":\"ttefs\",\"aisle\":36,\"author\":\"Shetty\"},\"msg\":\"AppiumCategory and price details are not available at this time - thanks\"}";
        libraryController.setBaseUrl(mockServer.getUrl());
        SpecificProduct specificProduct = libraryController.getProductFullDetails("Appium");

        ObjectMapper obj = new ObjectMapper();
        String actualJson = obj.writeValueAsString(specificProduct);

        Assertions.assertEquals(expectJson, actualJson);
    }




}
