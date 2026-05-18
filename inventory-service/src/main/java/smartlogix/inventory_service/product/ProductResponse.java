package smartlogix.inventory_service.product;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        String category,
        BigDecimal unitPrice,
        Integer stock,
        Boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getUnitPrice(),
                product.getStock(),
                product.getActive(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }
}
