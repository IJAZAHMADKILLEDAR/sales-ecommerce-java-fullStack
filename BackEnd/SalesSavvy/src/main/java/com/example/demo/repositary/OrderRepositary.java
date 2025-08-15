package com.example.demo.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Orders;
import com.razorpay.Order;
@Repository
public interface OrderRepositary extends JpaRepository<Orders, String> {

}
