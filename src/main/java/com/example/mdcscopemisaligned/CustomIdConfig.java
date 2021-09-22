package com.example.mdcscopemisaligned;

import brave.baggage.BaggageField;
import brave.baggage.CorrelationScopeConfig;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.CurrentTraceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomIdConfig {

	public final static String CUSTOM_ID_KEY = "customId";

	@Bean
	BaggageField customIdField() {
		return BaggageField.create(CUSTOM_ID_KEY);
	}

	@Bean
	CurrentTraceContext.ScopeDecorator mdcScopeDecorator() {
		return MDCScopeDecorator.newBuilder()
			.clear()
			.add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(customIdField())
				.flushOnUpdate()
				.build())
			.build();
	}
}