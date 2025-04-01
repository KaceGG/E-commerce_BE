package com.E_commerceApp.repositories;

import com.E_commerceApp.DTOs.response.ProductResponse;
import com.E_commerceApp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, int id);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    List<Product> findByCategoryId(int categoryId);
}
