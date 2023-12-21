package com.academy.fintech.pe.core.service.agreement.db.product;

import com.academy.fintech.pe.core.service.agreement.db.product.entity.EntityProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<EntityProduct, String> {
}
