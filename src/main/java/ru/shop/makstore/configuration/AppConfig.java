package ru.shop.makstore.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public class AppConfig {

    @Bean
    public GroupedOpenApi getVapeGroup() {
        return GroupedOpenApi.builder()
                .displayName("vape")
                .pathsToMatch("/vape/**")
                .group("vape")
                .build();
    }

    @Bean
    public GroupedOpenApi getOtherGroup() {
        return GroupedOpenApi.builder()
                .displayName("other")
                .pathsToMatch("/other/**")
                .group("other")
                .build();
    }

}
