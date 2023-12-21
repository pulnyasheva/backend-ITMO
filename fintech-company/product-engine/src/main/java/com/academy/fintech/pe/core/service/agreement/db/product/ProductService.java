package com.academy.fintech.pe.core.service.agreement.db.product;

import com.academy.fintech.pe.core.service.agreement.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.entity.EntityProduct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Получает продукт по его коду.
     *
     * @param codeProduct Код продукта.
     * @return Продукт.
     * @throws EntityNotFoundException Исключение, если продукт не найден.
     */
    public Optional<Product> get(String codeProduct) throws EntityNotFoundException {
        Optional<EntityProduct> entityProduct = productRepository.findById(codeProduct);
        return entityProduct.map(this::mapEntityToProduct);
    }

    private Product mapEntityToProduct(EntityProduct entityProduct) {
        return Product.builder()
                .code(entityProduct.getCode())
                .minLoanTerm(entityProduct.getMinLoanTerm())
                .maxLoanTerm(entityProduct.getMaxLoanTerm())
                .minPrincipalAmount(entityProduct.getMinPrincipalAmount())
                .maxPrincipalAmount(entityProduct.getMaxPrincipalAmount())
                .minInterest(entityProduct.getMinInterest())
                .maxInterest(entityProduct.getMaxInterest())
                .minOriginationAmount(entityProduct.getMinOriginationAmount())
                .maxOriginationAmount(entityProduct.getMaxOriginationAmount())
                .build();
    }
}
