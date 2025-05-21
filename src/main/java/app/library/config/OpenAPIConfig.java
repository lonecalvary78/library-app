package app.library.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.tags.Tag;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(OpenAPIConfig.OpenAPIProperties.class)
@RequiredArgsConstructor
public class OpenAPIConfig {
    
    private final OpenAPIProperties properties;

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title(properties.getInfo().getTitle())
                        .description(properties.getInfo().getDescription())
                        .version(properties.getInfo().getVersion())
                        .termsOfService(properties.getInfo().getTermsUrl()));
                
        // Set contact info if available
        if (properties.getInfo().getContact() != null) {
            openAPI.getInfo().contact(new Contact()
                    .name(properties.getInfo().getContact().getName())
                    .email(properties.getInfo().getContact().getEmail())
                    .url(properties.getInfo().getContact().getUrl()));
        }
        
        // Set license info if available
        if (properties.getInfo().getLicense() != null) {
            openAPI.getInfo().license(new License()
                    .name(properties.getInfo().getLicense().getName())
                    .url(properties.getInfo().getLicense().getUrl()));
        }
        
        // Set external docs if available
        if (properties.getExternalDocs() != null) {
            openAPI.externalDocs(new ExternalDocumentation()
                    .description(properties.getExternalDocs().getDescription())
                    .url(properties.getExternalDocs().getUrl()));
        }
        
        // Set servers if available
        if (properties.getServers() != null && !properties.getServers().isEmpty()) {
            List<Server> servers = new ArrayList<>();
            properties.getServers().forEach(serverProperties -> 
                servers.add(new Server()
                    .url(serverProperties.getUrl())
                    .description(serverProperties.getDescription())));
            openAPI.servers(servers);
        }
        
        // Set tags if available
        if (properties.getTags() != null && !properties.getTags().isEmpty()) {
            List<Tag> tags = new ArrayList<>();
            properties.getTags().forEach(tagProperties -> 
                tags.add(new Tag()
                    .name(tagProperties.getName())
                    .description(tagProperties.getDescription())));
            openAPI.tags(tags);
        }
        
        // Add security components
        openAPI.components(new Components()
                .addSecuritySchemes("bearer-jwt", 
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Authorization header using the Bearer scheme.")));
        
        return openAPI;
    }
    
    @ConfigurationProperties(prefix = "app.openapi")
    @Data
    public static class OpenAPIProperties {
        private InfoProperties info;
        private ExternalDocsProperties externalDocs;
        private List<ServerProperties> servers;
        private List<TagProperties> tags;
        
        @Data
        public static class InfoProperties {
            private String title;
            private String description;
            private String version;
            private String termsUrl;
            private ContactProperties contact;
            private LicenseProperties license;
            
            @Data
            public static class ContactProperties {
                private String name;
                private String email;
                private String url;
            }
            
            @Data
            public static class LicenseProperties {
                private String name;
                private String url;
            }
        }
        
        @Data
        public static class ExternalDocsProperties {
            private String description;
            private String url;
        }
        
        @Data
        public static class ServerProperties {
            private String url;
            private String description;
        }
        
        @Data
        public static class TagProperties {
            private String name;
            private String description;
        }
    }
} 