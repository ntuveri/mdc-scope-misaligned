package com.example.mdcscopemisaligned;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
@TestPropertySource(properties = {
	"spring.sleuth.reactor.instrumentation-type=decorate_queues",
	"logging.pattern.console=[%X{traceId:-},%X{spanId:-},%X{customId:-}] %m"
})
class DecorateQueuesTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void assertionError() {
		this.webTestClient.get()
			.uri("/get?customId=1")
			.exchange()
			.expectStatus()
			.isEqualTo(HttpStatus.OK)
			.expectBody()
			.jsonPath("$.headers.Customid", "1")
			.hasJsonPath()
			.jsonPath("$.headers.X-B3-Traceid")
			.hasJsonPath()
			.jsonPath("$.headers.X-B3-Spanid");

		// expect log output to look like:
		// [68e9e4dddc25f9ef,68e9e4dddc25f9ef,1] CustomIdField value set to: 1
	}
}
