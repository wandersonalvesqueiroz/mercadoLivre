package meli.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class CorsConfigTest {

    private static final String PATH_PATTERN = "/api/**";
    private static final String ORIGIN = "http://localhost:3000";
    private static final String[] METHODS = {"GET", "POST", "PUT", "DELETE"};

    @Mock
    private CorsRegistry corsRegistry;

    @Mock
    private CorsRegistration corsRegistration;

    @InjectMocks
    private CorsConfig corsConfig;

    @Test
    void shouldRegisterCorsMappingsForApiPath() {
        when(corsRegistry.addMapping(PATH_PATTERN))
                .thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins(ORIGIN))
                .thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods(METHODS))
                .thenReturn(corsRegistration);
        when(corsRegistration.allowCredentials(true))
                .thenReturn(corsRegistration);

        WebMvcConfigurer configurer = corsConfig.corsConfigurer();
        configurer.addCorsMappings(corsRegistry);

        verify(corsRegistry).addMapping(PATH_PATTERN);
        verify(corsRegistration).allowedOrigins(ORIGIN);
        verify(corsRegistration).allowedMethods(METHODS);
        verify(corsRegistration).allowCredentials(true);
    }
}

