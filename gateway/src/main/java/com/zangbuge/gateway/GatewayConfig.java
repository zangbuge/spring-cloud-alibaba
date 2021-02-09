package com.zangbuge.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        String id = "path_route_lhm";
        String routePath = "/store/**";
        String uri = "http://www.fxitalk.com/store";
        routes.route(id, route -> route.path(routePath).uri(uri)).build();
        return routes.build();
    }

}
