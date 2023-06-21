package org.Service.Services;

import org.Service.Entities.PhotoProfile;
import org.Service.Repositories.PhotoProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhotoProfileService {
    @Autowired
    private PhotoProfileRepo photoProfileRepo;

    public PhotoProfile addPhotoProfile(PhotoProfile photoProfile){
        return photoProfileRepo.save(photoProfile);
    }

    public Optional<PhotoProfile> getPhotoPathById(int userId){
        return photoProfileRepo.findById(userId);
    }
}
