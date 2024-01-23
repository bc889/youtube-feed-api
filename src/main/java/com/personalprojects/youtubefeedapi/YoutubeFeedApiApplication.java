package com.personalprojects.youtubefeedapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import static java.util.Arrays.stream;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
public class YoutubeFeedApiApplication {

	private static final Logger logger = LoggerFactory.getLogger(YoutubeFeedApiApplication.class);

	enum AppEnv {
		SERVER_PORT,
		APPLICATION_CLIENT_ORIGIN_URL,
		NGROK_BASE_URL,
		NGROK_AUTHTOKEN,
		NGROK_DOMAIN
	}

	enum AuthEnv {
		// Can conditionally disable auth0 for ease of demonstration.
		// Note: this project is not intended for use in production environments.
		APPLICATION_DISABLE_AUTH,
		OAUTH2_ISSUER,
		OAUTH2_AUDIENCE,
		OAUTH2_GROUPS_CLAIM,
	}

	public static void main(final String[] args) {
		dotEnvSafeCheck();
		authEnvSafeCheck();

		SpringApplication.run(YoutubeFeedApiApplication.class, args);
	}

	private static void authEnvSafeCheck() {
		final var dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		var disableAuth = Boolean.parseBoolean(dotenv.get(AuthEnv.APPLICATION_DISABLE_AUTH.name(), ""));

		if (!disableAuth) {
			stream(AuthEnv.values())
				.map(AuthEnv::name)
				.filter(varName -> dotenv.get(varName, "").isEmpty())
				.findFirst()
				.ifPresent(varName -> {
					logger.error("[Fatal] Cannot enable Auth0, missing or empty environment variable: {}", varName);
					System.exit(1);
				});
		}
	}

	private static void dotEnvSafeCheck() {
		final var dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		stream(AppEnv.values())
				.map(AppEnv::name)
				.filter(varName -> dotenv.get(varName, "").isEmpty())
				.findFirst()
				.ifPresent(varName -> {
					logger.error("[Fatal] Missing or empty environment variable: {}", varName);
					System.exit(1);
				});
	}
}
