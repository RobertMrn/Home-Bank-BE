package org.Service.Repositories;

import org.Service.Entities.PhotoProfile;
import org.springframework.data.repository.CrudRepository;

public interface PhotoProfileRepo extends CrudRepository<PhotoProfile, Integer> {
}
