package com.example.demo.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.OrderItem;
@Repository
public interface OrderItemRepositary extends JpaRepository<OrderItem, Integer> {
@Query("SELECT oi FROM OrderItem oi WHERE oi.orders.orderId = :orderId ")
	List<OrderItem> findByOrderId(String orderId);

@Query("SELECT oi FROM OrderItem oi WHERE oi.orders.userId = :userId AND oi.orders.status = 'SUCCESS'")
		List<OrderItem> findSuccessfulOrderItemsByUserId(int userId);
}
