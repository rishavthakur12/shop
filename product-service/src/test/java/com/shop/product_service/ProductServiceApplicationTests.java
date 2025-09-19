package com.shop.product_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.product_service.dto.ProductRequestDto;
import com.shop.product_service.model.Product;
import com.shop.product_service.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // this is not uint testing but complete integration testing
@Testcontainers // informs Junit that we are using test container
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6"); // take the image from the container

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource //this will add the properties dynamically
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl); //dynamically provide the uri of that image for running the test
    }

	@Test
	void shouldCreateProduct() throws Exception {
        ProductRequestDto productRequestDto = getproductRequest();
        String productReqString = objectMapper.writeValueAsString(productRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON).content(productReqString))
                .andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
    }


    private ProductRequestDto getproductRequest() {
        return ProductRequestDto.builder()
                .name("Iphone 17")
                .description("Latest Iphone")
                .price(1199)
                .build();
    }

}
