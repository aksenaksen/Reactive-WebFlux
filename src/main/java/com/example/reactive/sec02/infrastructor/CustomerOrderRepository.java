package com.example.reactive.sec02.infrastructor;

import com.example.reactive.sec02.domain.CustomerOrder;
import com.example.reactive.sec02.domain.Product;
import com.example.reactive.sec02.dto.OrderDetails;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface CustomerOrderRepository extends ReactiveCrudRepository<CustomerOrder, UUID> {

    @Query("""
            SELECT p.*
            FROM product p 
            JOIN customer_order co ON p.id = co.product_id
            JOIN customer c ON co.customer_id = c.id
            WHERE c.name = :name
            """)
    Flux<Product> getProductsByCustomerName(String name);

    @Query("""
            SELECT
                co.order_id,
                c.name AS customer_name,
                p.description AS product_name,
                co.amount,
                co.order_date
            FROM
                customer c
            INNER JOIN customer_order co ON c.id = co.customer_id
            INNER JOIN product p ON p.id = co.product_id
            WHERE
                p.description = :description
            ORDER BY co.amount DESC
            """)
    Flux<OrderDetails> findOrderDetailsByProduct(String description);
}
