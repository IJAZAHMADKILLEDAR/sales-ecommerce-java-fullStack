package com.example.demo.repositary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CartItem;

import jakarta.transaction.Transactional;
@Repository
public interface CartRepositary extends JpaRepository<CartItem, Integer> {

	@Query("SELECT c FROM CartItem c where c.users.userId = :userId AND c.Products.productId = :productId")
	Optional<CartItem> findByUserAndProduct(@Param("userId")int userId, @Param("productId") int productId);
	
	@Query("SELECT COALESCE(SUM(c.quantity),0) FROM CartItem c WHERE c.users.userId =:userId")
	int countTotalItems(@Param("userId") int userId);
	
	
	@Query("SELECT c FROM CartItem c " +
		       "JOIN FETCH c.Products p " +
		       "LEFT JOIN FETCH p.productImages pi " +
		       "WHERE c.users.userId = :userId")
	List<CartItem> findCartItemswithProductDetails(int userId);
	
@Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.id = :cartItemId")
	void updateCartItemQuantity(int cartItemId,int quantity);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItem c WHERE c.users.userId = :userId AND c.Products.productId = :productId")
	void deleteCartItem(int userId, int productId);
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItem c WHERE c.users.userId = :userId")
	void deleteAllCartItemByUser(int userId);
}
