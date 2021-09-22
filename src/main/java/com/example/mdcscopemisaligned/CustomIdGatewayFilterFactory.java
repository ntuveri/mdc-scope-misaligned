package com.example.mdcscopemisaligned;

import brave.baggage.BaggageField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomIdGatewayFilterFactory extends AbstractGatewayFilterFactory {

	private final BaggageField customIdField;

	public CustomIdGatewayFilterFactory(BaggageField customIdField) {
		this.customIdField = customIdField;
	}

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			String customIdValue = exchange.getRequest().getQueryParams().getFirst(CustomIdConfig.CUSTOM_ID_KEY);
			this.customIdField.updateValue(customIdValue);

			this.log.info("CustomIdField value set to: " + customIdValue);

			return chain.filter(exchange);
		};
	}
}
