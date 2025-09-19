package com.shop.product_service.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "product")
@AllArgsConstructor // constructor injection of all fileds
@NoArgsConstructor // constructor with no fileds (overloading)
@Builder // enables builder patterm
@Data // getters setters tostring etc.
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private double price;
}
