package cafe.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	//Coi như chưa làm gì
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/v1/**") // Chỉnh sửa đường dẫn nếu cần
//                .allowedOrigins("http://127.0.0.1:5500") // Thêm địa chỉ nguồn mà anh muốn cho phép
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
}
