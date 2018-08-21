/*
 * Copyright 2018 konraifen88
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.konraifen88.home.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR;

@Component
@Slf4j
public class LoadBalancerClientFilterExtension extends LoadBalancerClientFilter {

    private final LoadBalancerClient loadBalancerClient;

    public LoadBalancerClientFilterExtension(@Autowired LoadBalancerClient loadBalancer) {
        super(loadBalancer);
        this.loadBalancerClient = loadBalancer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(GATEWAY_SCHEME_PREFIX_ATTR, "https");
        logSomeValuesUsedInDefaultLoadBalancerFilter(exchange);
        return super.filter(exchange, chain);
    }

    private void logSomeValuesUsedInDefaultLoadBalancerFilter(ServerWebExchange exchange) {
        log.info("LB impl: {}", loadBalancerClient.getClass());
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        log.info("uri: {}", url);
        log.info("schemePrefix: {}", schemePrefix);
        log.info("Condition: {}", url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix)));

        exchange.getAttributes().forEach((k, v) -> log.warn("Key: {} Value: {}", k, v));
    }
}
