package de.neotop.textalyze;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class TextalyzeConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("de.neotop.textalyze"))
                .paths(PathSelectors.any()).build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.
          addResourceHandler("/swagger-ui/**")
          .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
          .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/swagger-ui/")
          .setViewName("forward:/swagger-ui/index.html");
    }

}
