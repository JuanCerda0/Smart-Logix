package smartlogix.inventory_service.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank
        @Size(max = 80)
        String sku,

        @NotBlank
        @Size(max = 160)
        String name,

        @Size(max = 500)
        String description,

        @NotBlank
        @Size(max = 100)
        String category,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal unitPrice,

        @NotNull
        @Min(0)
        Integer stock
) {
}
