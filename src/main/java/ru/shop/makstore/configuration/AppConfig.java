package ru.shop.makstore.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springshop-public")
                .pathsToMatch("/**")
                .build();
    }

//    @Bean
//    public GroupedOpenApi getVapeGroup() {
//        return GroupedOpenApi.builder()
//                .displayName("vape")
//                .pathsToMatch("/vape/**")
//                .group("vape")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi getOtherGroup() {
//        return GroupedOpenApi.builder()
//                .displayName("other")
//                .pathsToMatch("/other/**")
//                .group("other")
//                .build();
//    }

}
