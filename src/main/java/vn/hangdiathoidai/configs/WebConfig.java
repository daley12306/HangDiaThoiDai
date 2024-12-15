package vn.hangdiathoidai.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình để phục vụ hình ảnh từ thư mục uploads
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:/src/main/resources/static/assets/img/avatar/");
        registry.addResourceHandler("/products/**")
        		.addResourceLocations("file:/src/main/resources/static/assets/img/products/");
    }
}

