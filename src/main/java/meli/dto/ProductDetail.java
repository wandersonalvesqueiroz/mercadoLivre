package meli.dto;

import meli.model.Details;
import meli.model.Seller;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductDetail {
    private String id;
    private String title;
    private String description;
    private float lastPrice;
    private float price;
    private int discounted;
    private List<String> paymentMethods;
    private int splitPayment;
    private Seller seller;
    private int stock;
    private List<String> images;
    private Details details;
    private float rating;
    private int totalRatings;
}
