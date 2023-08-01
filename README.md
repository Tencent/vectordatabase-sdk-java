# Tencent VectorDB Java SDK


### Prerequisites

    -   Java 8 or higher
    -   Apache Maven or Gradle/Grails

### Install Java SDK

 - settings.xml add repository
    ```xml
    <repository>
      <id>vectordb</id>
      <url>https://mirrors.tencent.com/repository/maven/vectordb-sdk-java</url>
      <releases>
        <enabled>true</enabled>
      </releases>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
    </repository>
    ```

You can use **Apache Maven** or **Gradle**/**Grails** to download the SDK.

   - Apache Maven

       ```xml
        <dependency>
            <groupId>com.tencentcloudapi</groupId>
            <artifactId>vectordb-sdk-java</artifactId>
            <version>0.0.1</version>
        </dependency>
       ```

   - Gradle/Grails

        ```gradle
        compile 'com.tencentcloudapi:vectordb-sdk-java:0.0.1'
        ```

### Examples

Please refer to [examples](./examples) folder for Java SDK examples.