package com.E_commerceApp.repositories;

import com.E_commerceApp.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    // Sử dụng JPQL để tìm Payment dựa trên orderToken
    @Query("SELECT p FROM Payment p WHERE p.orderToken = :orderToken")
    Optional<Payment> findByOrderToken(@Param("orderToken") String orderToken);

    // Native SQL
    @Query(value = "SELECT * FROM payment WHERE order_token = :orderToken", nativeQuery = true)
    Optional<Payment> findByOrderTokenNative(@Param("orderToken") String orderToken);
}
