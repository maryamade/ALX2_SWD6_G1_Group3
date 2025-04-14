SeleniumTest Project
This project is a Selenium-based automation test project using Java, Maven, and TestNG.
It automates scenarios on the Demoblaze website such as user sign-up, login, product selection, adding to cart, and purchase.


## Prerequisites

- **Java** – Ensure Java is installed and JAVA_HOME is set.
- **Maven** – Used to build the project and manage dependencies.
- **Google Chrome** – The browser used for tests.
- **ChromeDriver** – Must match your installed Chrome version.
  - Download ChromeDriver from [ChromeDriver Downloads](https://sites.google.com/chromium.org/driver/downloads)
  - Place the ChromeDriver binary in the `drivers` folder (or update the path in your test code accordingly). (Please note that the chromedriver that included in this project right now have the version 134.0.6998.165 )
- **Internet Connection** – Required to download Maven dependencies. 

## Installing Dependencies

This project uses Maven to manage dependencies. Key dependencies include:
- **Selenium Java**
```
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>4.12.1</version>
        </dependency>
```
- **TestNG**
```
        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>7.7.1</version>
            <scope>test</scope>
        </dependency>
```
- **ExtentReport** (For generating Html Dashboard Report)
```
        <dependency>
            <groupId>com.relevantcodes</groupId>
            <artifactId>extentreports</artifactId>
            <version>2.41.2</version>
            <scope>test</scope>
        </dependency>
```
- **Java Faker** (for generating fake data)
```
       <dependency>
            <groupId>com.github.javafaker</groupId>
            <artifactId>javafaker</artifactId>
            <version>1.0.2</version>
        </dependency>
```


Maven will download these dependencies from pom.xml file automatically when you build the project.

## Building the Project

To build the project, navigate to the project root and run:
```
mvn clean compile
```
or use the blue icon 
![image](https://github.com/user-attachments/assets/b251954d-9ac5-4b39-ad74-f486d95cee33)

### Run the Test Cases 

To run the test cases, executed the following command 

``` 
mvn clean test
```
or run as current file of the "ProductPurchaseTest" class
![image](https://github.com/user-attachments/assets/542eaf58-8d0c-422c-8269-93dbc5ab9b5c)

### View the Report 
To view the report , navigate to `./Reports/Name_of_test_case.txt` to open it

![image](https://i.ibb.co/kgmqfS6Z/b2.png)
![image](https://i.ibb.co/qMKVJjtJ/b1.png)

