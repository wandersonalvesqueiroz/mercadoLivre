package meli.dto;

import meli.model.Details;
import meli.model.Seller;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDetailTest {

    private static final String PRODUCT_ID = "1";
    private static final String PRODUCT_TITLE = "Smartphone Top";
    private static final String PRODUCT_DESCRIPTION = "Celular com ótima câmera e bateria";
    private static final float LAST_PRICE = 1800.00f;
    private static final float CURRENT_PRICE = 1500.00f;
    private static final int DISCOUNTED = 1;
    private static final List<String> PAYMENT_METHODS = List.of("PIX", "Cartão");
    private static final int SPLIT_PAYMENT = 6;
    private static final String SELLER_ID = "123";
    private static final String SELLER_NAME = "Loja Oficial";
    private static final int STOCK = 20;
    private static final List<String> IMAGES = List.of("img1.png", "img2.png");
    private static final String PRETO = "Preto";
    private static final List<String> FEATURES = List.of( "Compatível com redes 5G", "128GB de memória interna");
    private static final float RATING = 4.7f;
    private static final int TOTAL_RATINGS = 320;

    @Test
    void shouldBuildProductDetailWithExpectedValues() {
        Seller seller = new Seller();
        seller.setId(SELLER_ID);
        seller.setTradeName(SELLER_NAME);

        Details details = new Details();
        details.setColor(PRETO);
        details.setFeatures(FEATURES);

        ProductDetail product = ProductDetail.builder()
                .id(PRODUCT_ID)
                .title(PRODUCT_TITLE)
                .description(PRODUCT_DESCRIPTION)
                .lastPrice(LAST_PRICE)
                .price(CURRENT_PRICE)
                .discounted(DISCOUNTED)
                .paymentMethods(PAYMENT_METHODS)
                .splitPayment(SPLIT_PAYMENT)
                .seller(seller)
                .stock(STOCK)
                .images(IMAGES)
                .details(details)
                .rating(RATING)
                .totalRatings(TOTAL_RATINGS)
                .build();

        assertThat(product.getId()).isEqualTo(PRODUCT_ID);
        assertThat(product.getTitle()).isEqualTo(PRODUCT_TITLE);
        assertThat(product.getDescription()).isEqualTo(PRODUCT_DESCRIPTION);
        assertThat(product.getLastPrice()).isEqualTo(LAST_PRICE);
        assertThat(product.getPrice()).isEqualTo(CURRENT_PRICE);
        assertThat(product.getDiscounted()).isEqualTo(DISCOUNTED);
        assertThat(product.getPaymentMethods()).containsExactlyElementsOf(PAYMENT_METHODS);
        assertThat(product.getSplitPayment()).isEqualTo(SPLIT_PAYMENT);
        assertThat(product.getSeller().getId()).isEqualTo(SELLER_ID);
        assertThat(product.getSeller().getTradeName()).isEqualTo(SELLER_NAME);
        assertThat(product.getStock()).isEqualTo(STOCK);
        assertThat(product.getImages()).containsExactlyElementsOf(IMAGES);
        assertThat(product.getDetails().getColor()).isEqualTo(PRETO);
        assertThat(product.getDetails().getFeatures()).isEqualTo(FEATURES);
        assertThat(product.getRating()).isEqualTo(RATING);
        assertThat(product.getTotalRatings()).isEqualTo(TOTAL_RATINGS);
    }

    @Test
    void shouldMatchEqualsAndHashCodeForSameData() {
        ProductDetail productDetail1 = ProductDetail.builder()
                .id(PRODUCT_ID)
                .title(PRODUCT_TITLE)
                .build();

        ProductDetail productDetail2 = ProductDetail.builder()
                .id(PRODUCT_ID)
                .title(PRODUCT_TITLE)
                .build();

        assertThat(productDetail1).isEqualTo(productDetail2);
        assertThat(productDetail1.hashCode()).isEqualTo(productDetail2.hashCode());
    }
}
