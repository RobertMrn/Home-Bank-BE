package org.Service.Repositories;

import org.Service.Entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public interface UserRepo extends CrudRepository<User, Integer> {

     User findUserByEmail(String email);
}
