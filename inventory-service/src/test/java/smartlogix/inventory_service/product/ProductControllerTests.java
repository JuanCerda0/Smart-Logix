package smartlogix.inventory_service.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldRejectProductsWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldCreateAndListProducts() throws Exception {
        productRepository.deleteAll();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sku": "SKU-TEST-001",
                                  "name": "Test Product",
                                  "description": "Created from test",
                                  "category": "Testing",
                                  "unitPrice": 9990,
                                  "stock": 12
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("SKU-TEST-001"))
                .andExpect(jsonPath("$.stock").value(12));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sku").value("SKU-TEST-001"));
    }

    @Test
    @WithMockUser
    void shouldUpdateProductStock() throws Exception {
        productRepository.deleteAll();
        Product product = productRepository.save(new Product(
                "SKU-STOCK-001",
                "Stock Product",
                "Product used for stock update test",
                "Testing",
                java.math.BigDecimal.valueOf(14990),
                5));

        mockMvc.perform(patch("/products/{id}/stock", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "stock": 18
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(18));
    }
}
