package com.rahulshettyacademy.Courses;

import java.util.Map;
import java.util.Optional;

import au.com.dius.pact.provider.junitsupport.VerificationReports;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.rahulshettyacademy.controller.AllCourseData;
import com.rahulshettyacademy.repository.CoursesRepository;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.StateChangeAction;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("CoursesCatalogue")
//@PactFolder("src/main/java/pacts")
@PactBroker(url="http://localhost:9292",
enablePendingPacts = "true", providerTags = "test")
public class NewProviderTest {

    @Autowired
    CoursesRepository repository;

    @LocalServerPort
    public int port;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    public void pactVerficationTest(PactVerificationContext context){
      context.verifyInteraction();
    }

    @BeforeEach
    public void setup(PactVerificationContext context){
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @State(value= "courses exist", action = StateChangeAction.SETUP)
    public void coursesExist(){

    }

    @State(value= "courses exist", action = StateChangeAction.TEARDOWN)
    public void coursesExistTearDown(){

    }

    @State(value= "Course Appium Exists", action = StateChangeAction.SETUP)
    public void appiumCoursesExist(){

    }

    @State(value= "Course Appium Exists", action = StateChangeAction.TEARDOWN)
    public void appiumCoursesExistTearDown(){

    }



    @State(value= "Course Appium does not exist", action = StateChangeAction.SETUP)
    public void appiumCoursesNotExist(Map<String,Object>params){

        String name =  (String)params.get("name");

        //to delete the appium record in database


        Optional<AllCourseData> del =repository.findById(name);//mock

        if (del.isPresent()) {
            repository.deleteById("Appium");
        }

    }

    @State(value= "Course Appium does not exist", action = StateChangeAction.TEARDOWN)
    public void appiumCoursesNotExistTearDown(Map<String,Object>params){

        String name =  (String)params.get("name");
        Optional<AllCourseData> del =repository.findById(name);//mock

        if (!del.isPresent()) {

            AllCourseData allCourseData = new AllCourseData();
            allCourseData.setCourse_name("Appium");
            allCourseData.setCategory("mobile");
            allCourseData.setPrice(120);
            allCourseData.setId("12");
            repository.save(allCourseData);

        }

    }
}
