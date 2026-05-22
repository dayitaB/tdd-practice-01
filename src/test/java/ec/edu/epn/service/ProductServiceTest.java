package ec.edu.epn.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ec.edu.epn.model.Category;
import ec.edu.epn.model.Product;
import ec.edu.epn.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("findBySku returns the product when it exists")
    void findBySkuReturnsProductWhenItExists() {
        Product product = createProduct("SKU-001", true);
        when(productRepository.findBySku("SKU-001")).thenReturn(Optional.of(product));
        Product result = productService.findBySku("SKU-001");
        assertEquals("SKU-001", result.getSku());
        assertEquals("Laptop", result.getName());
    }

    @Test
    @DisplayName("findBySku throws RuntimeException cuando el producto no existe")
    void findBySkuThrowsWhenProductDoesNotExist() {
        when(productRepository.findBySku("SKU-404")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productService.findBySku("SKU-404"));
    }

    @Test
    @DisplayName("findActiveProducts devuelve solo los productos activos")
    void findActiveProductsReturnsOnlyActiveProducts() {
        Product activeProduct = createProduct("SKU-001", true);
        Product anotherActiveProduct = createProduct("SKU-002", true);
        when(productRepository.findByActiveTrue()).thenReturn(List.of(activeProduct, anotherActiveProduct));
        List<Product> result = productService.findActiveProducts();
        assertEquals(2, result.size());
        assertEquals("SKU-001", result.get(0).getSku());
        assertEquals("SKU-002", result.get(1).getSku());
    }

    private Product createProduct(String sku, boolean active) {
        Category category = new Category("Electrónica", "Categoría de ejemplo");
        Product product = new Product(sku, "Laptop", new BigDecimal("1200.00"), 10, category);
        product.setActive(active);
        return product;
    }
}