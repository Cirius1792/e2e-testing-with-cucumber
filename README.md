# Acceptance Tests with Cucumber and Spring Boot

## Description 
This projects shows how to use [Cucumber](https://cucumber.io/) with Spring Boot.

The Business Logic implements basics CRUD operations for a User master data management system. 

The project is organized as follows: 
- core: main library implementing the business logic
- persistence: concrete implementation of the repository interface in the core
- api-rest: exposes the funcitonality implemented in the core as rest apis. Implemented with Spring Web Flux
- spring-runner: contains the application configuration and the main class

## Integrating Cucumber 
Under 

`userprofile-spring-runner -> src -> main -> resources ->user-profile `
 you can find the *.feature files defining what the application is supposed to do. 
 
 For example: 
 ```gherkin
   Scenario: Create a new user
        Given A new user that wants to register
        And The user has a unique username
        When The user tries to register
        Then the outcome of the operation is "OK"

    Scenario: Create a new user with an already registered account
        Given A new user that wants to register
        And The user has not a unique username
        When The user tries to register
        Then the outcome of the operation is "KO"

 ```

states that if a new user tries to register with a username which is already not taken, it should be able to. If he's trying to register to the system with a username which is already taken, than the operation should fail. 

More detail on how translate a so defined specification in an executable test case can be find [here](https://cucumber.io/docs/guides/10-minute-tutorial/?lang=java). While there are required few more steps to make cucumber work with Spring levereging the CDI advantages that it offers. 

First, the cucumber-spring dependency is required: 
```xml
<dependency>
      <groupId>io.cucumber</groupId>
      <artifactId>cucumber-spring</artifactId>
      <version>${cucumber.version}</version>
      <scope>test</scope>
</dependency>
```
The full documentation can be found [here](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-spring).

Then, we need to annotate a glue class that will start the application context in which our cucumber Scenarios will be executed. 
In our case, for example: 
```java
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserProfileAT {

	@Test
	void contextLoads() {
	}

}
```
## Make our Cucumber Scenarios run during the Integration Test Phase
Considering that in our case the Cucuber tests interact with a fully running instance of the application, as we are using them as Acceptance Tests, the execution time of the test cases will be considerably higher than the execution time of a usual JUnit test. Considering that we want our build to be as fast as possible and to trigger, initially, only the unit tests, we need a way to tell maven to run only the unit tests insthead of our Acceptance Test. 

One way to reach this goal is to use the failsafe maven plug-in in addition to the surfire plugin. Failsafe, in contrast with surfire, executes by default all the test cases having a name like *IT.java.

In this specific example we configured failsafe to run test cases having name *AT.java as we are using it to run our acceptance tests. 
Therefore we add to the pom of the application: 

```xml
<profiles>
        <profile>
            <id>failsafe</id>
            <build>
                <plugins>
                    <plugin>
                        <artifactId>maven-failsafe-plugin</artifactId>
                        <version>3.0.0-M9</version>
                        <configuration>
                            <includes>
                                <include>**/*AT</include>
                            </includes>
                        </configuration>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>integration-test</goal>
                                    <goal>verify</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
```
Doing so, with the command: 

`
mvn integration-test -Pfailsafe
`

Maven will run under the profile "failsafe" configured to execute our Acceptance Test. 

## Configuring the CI/CD with the GitHub Actions
All we need to do is to instruct the pipeline to spin up the proper environment and then launch the same command used on our local machine to build the application an run the tests: 

```yaml
jobs:
  commit_stage:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v3
        with:
          distribution: 'oracle'
          java-version: 17 
      - name: Build
        run: mvn clean package
      - name: Acceptance Test
        run: mvn integration-test -Pfailsafe
```