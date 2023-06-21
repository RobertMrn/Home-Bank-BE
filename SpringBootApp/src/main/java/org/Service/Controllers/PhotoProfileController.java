package org.Service.Controllers;

import org.Service.Entities.PhotoProfile;
import org.Service.Services.PhotoProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
public class PhotoProfileController {
    @Autowired
    PhotoProfileService photoProfileService;

    @PostMapping("/addPhotoProfile")
    public PhotoProfile addPhotoProfile(@RequestBody PhotoProfile photoProfile){
        return photoProfileService.addPhotoProfile(photoProfile);
    }

    @GetMapping("/getPhotoPath")
    public Optional<PhotoProfile> getPhotoPath(@RequestParam int userId){
        return photoProfileService.getPhotoPathById(userId);
    }
}
