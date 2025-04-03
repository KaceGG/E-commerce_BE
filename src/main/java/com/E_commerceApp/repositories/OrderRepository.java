package com.E_commerceApp.repositories;

import com.E_commerceApp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByIdAndUserId(int orderId, String userId);
}
