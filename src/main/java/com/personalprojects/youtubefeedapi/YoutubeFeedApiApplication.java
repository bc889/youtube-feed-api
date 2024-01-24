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
		NGROK_BASE_URL,
	}

	public static void main(final String[] args) {
		dotEnvSafeCheck();

		SpringApplication.run(YoutubeFeedApiApplication.class, args);
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
