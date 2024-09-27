<h1 align="center" id="title">User's tasks management API</h1>

<p id="description">Simple REST API built based on JWT Authentication.</p>

<h2>API Endpoints documentation</h2>

```bash
https://zabkins.github.io/usm/
```

<h2>💻 Built with</h2>

Technologies used in the project:

*   Java 21
*   Spring Boot 3.3.3
*   Spring Security 6.3.3
*   JUnit5
*   JJWT 0.12.6
*   OpenAPI 3.0.0

<h2>🛠️ Run Locally using IDE</h2>
Clone the project

```bash
  git clone https://github.com/zabkins/usm.git
```

[Option A] Then open project using IDE of your choice and run UsmApplication.<br>
[Option B] Go to the project directory using
```bash
  cd usm/
```
Then build project using
```bash
  mvn clean install
```
Run command
```bash
  java -jar /target/ustm-0.0.1-SNAPSHOT.jar
```
<h2>🛠️ Run Locally using Docker Image</h2>

Pull image from Dockerhub using
```bash
  docker pull zabkins/ustm-app
```
And run the container
