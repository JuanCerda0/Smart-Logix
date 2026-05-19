package smartlogix.inventory_service.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartlogix.inventory_service.shared.ConflictException;
import smartlogix.inventory_service.shared.NotFoundException;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return ProductResponse.fromEntity(findProductById(id));
    }

    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsBySkuIgnoreCase(request.sku())) {
            throw new ConflictException("Product SKU already exists: " + request.sku());
        }

        Product product = new Product(
                request.sku(),
                request.name(),
                request.description(),
                request.category(),
                request.unitPrice(),
                request.stock());

        return ProductResponse.fromEntity(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findProductById(id);
        productRepository.findBySkuIgnoreCase(request.sku())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Product SKU already exists: " + request.sku());
                });

        product.setSku(request.sku());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(request.category());
        product.setUnitPrice(request.unitPrice());
        product.setStock(request.stock());

        return ProductResponse.fromEntity(product);
    }

    public ProductResponse updateStock(Long id, StockUpdateRequest request) {
        Product product = findProductById(id);
        product.setStock(request.stock());
        return ProductResponse.fromEntity(product);
    }

    public void delete(Long id) {
        Product product = findProductById(id);
        productRepository.delete(product);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }
}
