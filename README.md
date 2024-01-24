# Youtube Feed API (YFA)

A REST API built around subscribing to RSS feeds for Youtube channels using google's [PubSubHubbub hub](https://pubsubhubbub.appspot.com/).

`POST /users/{userId}/subscriptions` will subscribe to the given channels on the users behalf and:

1.  Save updates to a pageable feed users can access at any time with `GET /users/{userId}/feed`.
2.  Send notificataions to the user via [Pushover](https://pushover.net/), must provide a userId and key from `PATCH /users/{{test_user_id}}/notification-settings`.

## Running Locally (With no Auth)
The RSS Feed requires a publicly accessible url to call back to:

### Ngrok Account Setup

1. **Create a Ngrok Account:**
   - Visit [Ngrok](https://ngrok.com/) and sign up for a new account.
   - Follow the registration process to create your Ngrok account.

2. **Obtain Ngrok Auth Token:**
   - After logging in, navigate to the [Ngrok Auth](https://dashboard.ngrok.com/get-started/your-authtoken) page.
   - Copy the authentication token provided on the page.

### Ngrok Static Domain

3. **Generate a Static Domain:**
   - On the Ngrok dashboard, navigate to [Your Domains](https://dashboard.ngrok.com/cloud-edge/domains).
   - Create a new domain (e.g., `your-ngrok-domain.ngrok.io`).

### Ngrok Command for Local Development

4. **Run Ngrok Locally:**
   - [Installing the ngrok command locally](https://dashboard.ngrok.com/get-started/setup)
   - Open a terminal and navigate to your project directory.
   - Run the following Ngrok command to expose your local server (e.g., running on port 8080) to the internet over HTTPS:

     ```bash
     ngrok http --hostname=your-ngrok-domain.ngrok.io 8080
     ```

### Environment Variables

5. **Set Environment Variables:**
   - In the app's root folder, create a `.env` file and set the environment variables:

     ```env
     NGROK_BASE_URL=https://your-ngrok-domain.ngrok.io
     SPRING_PROFILES_ACTIVE=local
     SERVER_PORT=8080
     ```

   - Replace `your-ngrok-domain` with the static domain you generated.

### Running the Spring Boot Application

1. **Build:**
   - Run the following Gradle command in the project directory:

     ```bash
     ./gradlew build -x test
     ```

   Replace `./gradlew` with `gradlew` if you are on Windows.

2. **Run the Spring Boot Application:**
   - After a successful build, navigate to the `build/libs` directory:

     ```bash
     cd build/libs
     ```

   - Run the Spring Boot application JAR file:

     ```bash
     java -jar youtube-feed-api-0.0.1-SNAPSHOT.jar
     ```

   - Replace `youtube-feed-api-0.0.1-SNAPSHOT.jar` with the actual JAR file generated by Gradle.
   - API docs are accessible from http://localhost:8080/swagger-ui/index.html.
