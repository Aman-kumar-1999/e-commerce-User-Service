package com.eqipped.userservice.repositories;

import com.eqipped.userservice.entities.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role,String> {
}
