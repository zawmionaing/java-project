package com.java.jobportal.service;

import com.java.jobportal.model.UserProfile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public interface UserProfileService {
    
    
    void saveOrUpdate(UserProfile userProfile,
                      @RequestParam("photo") MultipartFile new_photo,
                      @RequestParam("cvForm") MultipartFile new_cvForm) throws IOException;
    
    UserProfile getById(Long id) throws Exception;
    
}
