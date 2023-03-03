package org.Service.Repositories;

import org.Service.Entities.User;
import org.Service.Entities.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepo extends CrudRepository<UserRole, Integer> {
    UserRole findUserRoleByUser(User user);
}
