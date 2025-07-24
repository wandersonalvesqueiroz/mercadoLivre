package meli.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import lombok.extern.slf4j.Slf4j;
import meli.dto.ProductDetail;
import meli.model.PaymentMethod;
import meli.model.Product;
import meli.model.Rating;
import meli.service.ProductService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final List<Product> products;

    public ProductServiceImpl() {
        ObjectMapper mapper = Json.mapper();
        try {
            log.info("Carregando arquivo json");
            products = mapper.readValue(
                    new ClassPathResource("products.json").getInputStream(),
                    new TypeReference<>() {}
            );
            log.info("Total produtos: {}", products.size());
        } catch (IOException e) {
            log.error("Erro ao carregar json: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao carregar json", e);
        }
    }

    @Override
    public List<Product> findAll() {
        log.info("Listar produtos");
        return products;
    }

    @Override
    public Optional<ProductDetail> findById(String id) {
        log.info("Buscando produto por id: {}", id);

        Optional<Product> produto = products.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst();

        if (produto.isEmpty()) {
            log.warn("Produto id '{}' não encontrado", id);
            return Optional.empty();
        }

        log.debug("Produto encontrado: {}", produto.get().getTitle());
        ProductDetail productDetail = getProductDetail(produto.get(), getPayments(produto.get()));
        log.debug("Produto com desconto de {}%", productDetail.getDiscounted());

        return Optional.of(productDetail);
    }

    private ProductDetail getProductDetail(Product product, List<String> payments) {
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

    private List<String> getPayments(Product product) {
        log.trace("Métodos de pagamento para produto '{}'", product.getId());
        return product.getPaymentMethods().stream()
                .map(PaymentMethod::name)
                .toList();
    }

    private float getAverageRating(Rating rating) {
        int totalRatings = getSumRating(rating);
        int sum = rating.getRating1() + rating.getRating2() * 2 + rating.getRating3() * 3 +
                rating.getRating4() * 4 + rating.getRating5() * 5;

        float media = totalRatings == 0 ? 0f : (float) sum / totalRatings;
        float arredondado = Math.round(media * 10) / 10f;

        log.trace("Calculando avaliações: {} total, média {}", totalRatings, arredondado);
        return arredondado;
    }

    private int getSumRating(Rating rating) {
        int total = Stream.of(
                rating.getRating0(), rating.getRating1(), rating.getRating2(),
                rating.getRating3(), rating.getRating4(), rating.getRating5()
        ).mapToInt(Integer::intValue).sum();

        log.trace("Soma avaliações: {}", total);
        return total;
    }

    private int getDiscount(float lastPrice, float price) {
        if (lastPrice <= price) {
            log.trace("Sem desconto: preço atual ({}) >= último preço ({})", price, lastPrice);
            return 0;
        }

        int desconto = Math.round(((lastPrice - price) / lastPrice) * 100);
        log.trace("Desconto calculado: {}% de {} para {}", desconto, lastPrice, price);
        return desconto;
    }
}
