package smartlogix.BackendForFrontend.product;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class InventoryClient {

    private final RestClient inventoryRestClient;

    public InventoryClient(RestClient inventoryRestClient) {
        this.inventoryRestClient = inventoryRestClient;
    }

    public List<ProductResponse> findAll(String authorizationHeader) {
        return inventoryRestClient.get()
                .uri("/products")
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public ProductResponse findById(Long id, String authorizationHeader) {
        return inventoryRestClient.get()
                .uri("/products/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .body(ProductResponse.class);
    }

    public ProductResponse create(ProductRequest request, String authorizationHeader) {
        return inventoryRestClient.post()
                .uri("/products")
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .body(request)
                .retrieve()
                .body(ProductResponse.class);
    }

    public ProductResponse update(Long id, ProductRequest request, String authorizationHeader) {
        return inventoryRestClient.put()
                .uri("/products/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .body(request)
                .retrieve()
                .body(ProductResponse.class);
    }

    public ProductResponse updateStock(Long id, StockUpdateRequest request, String authorizationHeader) {
        return inventoryRestClient.patch()
                .uri("/products/{id}/stock", id)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .body(request)
                .retrieve()
                .body(ProductResponse.class);
    }

    public void delete(Long id, String authorizationHeader) {
        inventoryRestClient.delete()
                .uri("/products/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .toBodilessEntity();
    }
}
