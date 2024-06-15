# Kotlin TestNG Selenium - Demo

## Description

Brief description of the project.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- JDK (Java Development Kit) installed
- IntelliJ IDEA or any preferred IDE
- Maven or Gradle (optional, if using for build management)

### Installing

1. Clone the repository to your local machine:

   ```bash
   git clone https://github.com/yourusername/your-repository.git
   ```

2. Open the project in IntelliJ IDEA.

3. Resolve dependencies using Maven or Gradle.

### Configuration

1. **Ensure `config.properties` exists:**

    - The `config.properties` file should be located in the `src/main/resources/` directory.
    - If it doesn't exist, create a new file named `config.properties` under `src/main/resources/` and add your configuration properties.

   Example `config.properties`:
   ```properties
   key1=value1
   key2=value2
   ```

2. **Verify `PropertiesReader.kt`:**

    - Make sure `PropertiesReader.kt` correctly loads `config.properties`.
    - Adjust the file path in `PropertiesReader.kt` if necessary to match your project structure.

   Example `PropertiesReader.kt`:
   ```kotlin
   object PropertiesReader {
       private val properties = Properties()

       init {
           val inputStream = PropertiesReader::class.java.classLoader.getResourceAsStream("config.properties")
           if (inputStream != null) {
               properties.load(inputStream)
           } else {
               throw FileNotFoundException("config.properties file not found")
           }
       }

       fun getProperty(key: String): String {
           return properties.getProperty(key)
       }

       // Add other methods as needed...
   }
   ```

### Running Tests

1. **TestNG Configuration:**

    - Ensure your `testng.xml` or annotations correctly specify the test suite and include necessary dependencies.

   Example `testng.xml`:
   ```xml
   <!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
   <suite name="Test Suite">
       <test name="Login Test">
           <classes>
               <class name="com.cmccarthy.kotlin.test.LoginTest"/>
           </classes>
       </test>
       <!-- Include other tests as necessary -->
   </suite>
   ```

2. **Run the tests:**

    - Execute your TestNG tests from IntelliJ IDEA or from command line using Maven or Gradle.

### Troubleshooting

- If you encounter `FileNotFoundException: config.properties not found`, double-check:
    - The location of `config.properties`.
    - The file path specified in `PropertiesReader.kt`.

- Kill the chrome processes 
       - `taskkill /im chromedriver.exe /f`
## Built With

- Kotlin
- TestNG
- Selenium WebDriver

---