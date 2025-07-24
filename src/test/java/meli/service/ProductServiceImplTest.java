package meli.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import meli.dto.ProductDetail;
import meli.model.*;
import meli.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceImplTest {

    private static final String PRODUCT_ID = "1";
    private static final String JSON = "products.json";

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        productService = new ProductServiceImpl();
    }

    @Test
    void shouldReturnAllProductsFromList() {
        List<Product> result = productService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(PRODUCT_ID);
    }

    @Test
    void shouldReturnProductDetailMappedById() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Product> jsonProducts = mapper.readValue(
                new ClassPathResource(JSON).getInputStream(),
                new TypeReference<>() {}
        );

        Product expected = jsonProducts.stream()
                .filter(p -> p.getId().equals(PRODUCT_ID))
                .findFirst()
                .orElseThrow();

        Optional<ProductDetail> detail = productService.findById(PRODUCT_ID);

        assertThat(detail).isPresent();
        assertThat(detail.get().getId()).isEqualTo(expected.getId());
        assertThat(detail.get().getTitle()).isEqualTo(expected.getTitle());
        assertThat(detail.get().getDescription()).isEqualTo(expected.getDescription());
        assertThat(detail.get().getPrice()).isEqualTo(expected.getPrice().floatValue());
        assertThat(detail.get().getLastPrice()).isEqualTo(expected.getLastPrice().floatValue());
        assertThat(detail.get().getSeller()).isEqualTo(expected.getSeller());
        assertThat(detail.get().getDetails()).isEqualTo(expected.getDetails());
        assertThat(detail.get().getPaymentMethods()).containsExactlyElementsOf(
                expected.getPaymentMethods().stream().map(Enum::name).toList()
        );
        assertThat(detail.get().getImages()).containsExactlyElementsOf(expected.getImages());

        int expectedDiscount = getDiscount(expected.getLastPrice().floatValue(), expected.getPrice().floatValue());

        assertThat(detail.get().getDiscounted()).isEqualTo(expectedDiscount);
        assertThat(detail.get().getTotalRatings()).isEqualTo(getSumRating(expected.getRating()));
        assertThat(detail.get().getRating()).isEqualTo(getAverageRating(expected.getRating()));
    }

    @Test
    void shouldReturnEmptyOptionalWhenIdIsInvalid() {
        Optional<ProductDetail> detail = productService.findById("invalid");

        assertThat(detail).isEmpty();
    }

    private float getAverageRating(Rating rating) {
        int totalRatings = getSumRating(rating);
        int sum = rating.getRating1() + rating.getRating2() * 2 + rating.getRating3() * 3 + rating.getRating4() * 4 + rating.getRating5() * 5;

        if (totalRatings == 0) {
            return 0f;
        }

        final var media = (float) sum / totalRatings;

        return Math.round(media * 10) / 10f;
    }

    private int getSumRating(Rating rating) {
        return Stream.of(rating.getRating0(), rating.getRating1(), rating.getRating2(), rating.getRating3(), rating.getRating4(), rating.getRating5())
                .mapToInt(Integer::intValue)
                .sum();
    }

    private int getDiscount(float lastPrice, float price) {
        if (lastPrice <= price) {
            return 0;
        }

        double discount = ((lastPrice - price) / lastPrice) * 100;

        return (int) Math.round(discount);
    }
}
