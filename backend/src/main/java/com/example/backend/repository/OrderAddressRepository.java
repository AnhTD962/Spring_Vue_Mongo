package com.example.backend.repository;

import com.example.backend.model.entity.OrderAddress;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderAddressRepository extends MongoRepository<OrderAddress, String> {


}
