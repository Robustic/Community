package projekti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
public class MyApplication {    

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class);
    }
    
//@Configuration
//public class WebConfiguration extends WebMvcConfigurationSupport {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**")
//                 .addResourceLocations("classpath:/");
//    }
//}

}
