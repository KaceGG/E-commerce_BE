package com.E_commerceApp.repositories;

import com.E_commerceApp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, int id);

    Optional<Category> findByName(String name);
}
