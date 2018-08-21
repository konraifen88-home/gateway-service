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

import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.adapter.DefaultServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;
import org.springframework.web.server.session.WebSessionManager;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR;

public class DefaultServerWebExchangeDecorator extends DefaultServerWebExchange {

    public DefaultServerWebExchangeDecorator(ServerHttpRequest request, ServerHttpResponse response, WebSessionManager sessionManager, ServerCodecConfigurer codecConfigurer, LocaleContextResolver localeContextResolver) {
        super(request, response, sessionManager, codecConfigurer, localeContextResolver);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String name) {
        if (GATEWAY_SCHEME_PREFIX_ATTR.equals(name)) {
            String schemePrefix = (String) getAttributes().get(name);
            return schemePrefix == null ? (T) "https" : (T) schemePrefix;
        }
        return (T) getAttributes().get(name);
    }
}
