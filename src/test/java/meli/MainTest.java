package meli;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MainApplicationTest {

    @Test
    void shouldStartSpringContextSuccessfully() {
        assertThat(true).isTrue();
    }
}
