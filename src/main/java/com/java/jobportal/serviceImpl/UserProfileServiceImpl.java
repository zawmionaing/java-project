package com.java.jobportal.serviceImpl;

import com.java.jobportal.model.UserProfile;
import com.java.jobportal.repository.UserProfileRepository;
import com.java.jobportal.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    UserProfileRepository userProfileRepository;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Override
    public void saveOrUpdate(UserProfile userProfile,
                             @RequestParam("photo") MultipartFile new_photo,
                             @RequestParam("cvForm") MultipartFile new_cvForm) throws IOException {
        
        Long userId = userProfile.getUser().getId();
        UserProfile existingUserProfile = userProfileRepository.findByUserId(userId);
        
        String photoName = null;
        String cvFormName = null;
        
        if (new_photo != null && !new_photo.isEmpty() && StringUtils.hasText(new_photo.getOriginalFilename())) {
            photoName = UUID.randomUUID().toString() + '_' + new_photo.getOriginalFilename();
        }
        
        if (new_cvForm != null && !new_cvForm.isEmpty() && StringUtils.hasText(new_cvForm.getOriginalFilename())) {
            cvFormName = UUID.randomUUID().toString() + '_' + new_cvForm.getOriginalFilename();
        }
        
        Path projectPath = Paths.get("src/main/resources/static/frontend/user/img/");
        
        if (photoName != null) {
            if(existingUserProfile != null){
                deleteExistingPhoto(existingUserProfile);
            }
            Resource photoResource = resourceLoader.getResource("classpath:/static/frontend/user/img/");
            URI uri = photoResource.getURI();
            Path path= Path.of(uri);
            Path filename=path.resolve(photoName);
            Files.copy(new_photo.getInputStream(), filename, StandardCopyOption.REPLACE_EXISTING);
            
            
            Path projectDirectoryFilename = projectPath.resolve(photoName);
            Files.copy(new_photo.getInputStream(), projectDirectoryFilename, StandardCopyOption.REPLACE_EXISTING);
        }
        
        if (cvFormName != null) {
            if(existingUserProfile != null){
                deleteExistingCvForm(existingUserProfile);
            }
            Resource cvFormResource = resourceLoader.getResource("classpath:/static/frontend/user/img/");
            URI uri = cvFormResource.getURI();
            Path path= Path.of(uri);
            Path filename=path.resolve(cvFormName);
            Files.copy(new_cvForm.getInputStream(), filename, StandardCopyOption.REPLACE_EXISTING);
            
            Path projectDirectoryFilename = projectPath.resolve(cvFormName);
            Files.copy(new_cvForm.getInputStream(), projectDirectoryFilename, StandardCopyOption.REPLACE_EXISTING);
        }
        
        if (existingUserProfile != null) {
            
            if (photoName != null && cvFormName != null) {
                existingUserProfile.setPhoto(photoName);
                existingUserProfile.setCvForm(cvFormName);
            }else if(photoName != null) {
                existingUserProfile.setPhoto(photoName);
            }
            else if(cvFormName != null) {
                existingUserProfile.setCvForm(cvFormName);
            }
            
            existingUserProfile.setDateOfBirth(userProfile.getDateOfBirth());
            existingUserProfile.setNrc(userProfile.getNrc());
            existingUserProfile.setAddress(userProfile.getAddress());
            existingUserProfile.setPhone(userProfile.getPhone());
            
            userProfileRepository.saveAndFlush(existingUserProfile);
        } else {
            userProfile.setCvForm(cvFormName);
            userProfile.setPhoto(photoName);
            userProfileRepository.saveAndFlush(userProfile);
        }
    }
    
    private void deleteExistingPhoto(UserProfile userProfile) {
        if (StringUtils.hasText(userProfile.getPhoto())) {
            String photoFileName = userProfile.getPhoto();
            
            // Delete from the classpath directory
            Resource photoResourceClasspath = resourceLoader.getResource("classpath:/static/frontend/user/img/" + photoFileName);
            if (photoResourceClasspath.exists()) {
                try {
                    Files.deleteIfExists(photoResourceClasspath.getFile().toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            // Delete from the project directory
            Path projectPath = Paths.get("src/main/resources/static/frontend/user/img/");
            Path projectPhotoPath = projectPath.resolve(photoFileName);
            
            if (Files.exists(projectPhotoPath)) {
                try {
                    Files.deleteIfExists(projectPhotoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void deleteExistingCvForm(UserProfile userProfile) {
        if (StringUtils.hasText(userProfile.getCvForm())) {
            String cvFileName = userProfile.getCvForm();
            
            // Delete from the classpath directory
            Resource cvResourceClasspath = resourceLoader.getResource("classpath:/static/frontend/user/img/" + cvFileName);
            if (cvResourceClasspath.exists()) {
                try {
                    Files.deleteIfExists(cvResourceClasspath.getFile().toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            // Delete from the project directory
            Path projectPath = Paths.get("src/main/resources/static/frontend/user/img/");
            Path projectPhotoPath = projectPath.resolve(cvFileName);
            
            if (Files.exists(projectPhotoPath)) {
                try {
                    Files.deleteIfExists(projectPhotoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public UserProfile getById(Long id) throws Exception {
        Optional<UserProfile> result = userProfileRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new Exception("Couldn't find Profile ");
    }
}