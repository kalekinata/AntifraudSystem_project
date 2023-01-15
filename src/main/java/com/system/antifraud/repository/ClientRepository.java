package com.system.antifraud.repository;

import com.system.antifraud.model.db.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, String> {

}
