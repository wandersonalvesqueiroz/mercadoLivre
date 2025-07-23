package meli.service.impl;

import meli.dto.ProductDetail;
import meli.model.PaymentMethod;
import meli.model.Product;
import meli.model.Rating;
import meli.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService {

    private final List<Product> products;

    public ProductServiceImpl() {
        ObjectMapper mapper = Json.mapper();
        try {
            products = mapper.readValue(
                    new ClassPathResource("products.json").getInputStream(),
                    new TypeReference<List<Product>>() {
                    }
            );
        } catch (IOException e) {
            System.err.println("Erro ao carregar o JSON: " + e.getMessage());
            e.printStackTrace(); // Para ver a causa real
            throw new RuntimeException("Erro ao carregar products.json", e);
        }
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Optional<ProductDetail> findById(String id) {
        return products.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .map(product -> {
                    final var payments = product.getPaymentMethods().stream()
                            .map(PaymentMethod::name)
                            .toList();

                            return ProductDetail.builder()
                                        .id(product.getId())
                                        .title(product.getTitle())
                                        .description(product.getDescription())
                                        .lastPrice(product.getLastPrice().floatValue())
                                        .price(product.getPrice().floatValue())
                                        .discounted(getDiscount(product.getLastPrice().floatValue(), product.getPrice().floatValue()))
                                        .paymentMethods(payments)
                                        .splitPayment(product.getSplitPayment())
                                        .seller(product.getSeller())
                                        .stock(product.getStock())
                                        .images(product.getImages())
                                        .details(product.getDetails())
                                        .rating(getAverageRating(product.getRating()))
                                        .totalRatings(getSumRating(product.getRating()))
                                        .build();
                        }
                );
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
