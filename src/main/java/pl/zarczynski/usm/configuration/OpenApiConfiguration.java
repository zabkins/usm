package pl.zarczynski.usm.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@OpenAPIDefinition(info = @Info(title = "UsersTaskManagement API",
		description = "JWT authentication based REST API. Unprotected /signup and /login endpoints are meant to be used for authentication"))
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer"
)
public class OpenApiConfiguration {

	@Bean
	public OpenApiCustomizer bearerCustomizer() {
		return openApi -> {
			List<Tag> customOrderTags = new ArrayList<>();
			openApi.getTags().stream().filter(tag -> "Authentication".equals(tag.getName())).findFirst().ifPresent(customOrderTags::add);
			openApi.getTags().stream().filter(tag -> "User".equals(tag.getName())).findFirst().ifPresent(customOrderTags::add);
			openApi.getTags().stream().filter(tag -> "Tasks".equals(tag.getName())).findFirst().ifPresent(customOrderTags::add);
			openApi.getTags().stream().filter(tag -> "SubTasks".equals(tag.getName())).findFirst().ifPresent(customOrderTags::add);
			openApi.setTags(customOrderTags);
		};
	}
}
