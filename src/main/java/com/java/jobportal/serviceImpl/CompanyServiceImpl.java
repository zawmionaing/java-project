package com.java.jobportal.serviceImpl;

import com.java.jobportal.form.CompanyEditForm;
import com.java.jobportal.model.*;
import com.java.jobportal.repository.CompanyRepository;
import com.java.jobportal.service.CompanyService;
import com.java.jobportal.service.JobService;
import com.java.jobportal.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserService userService;

    @Autowired
    JobService jobService;
    private final String UPLOAD_DIRECTORY = "src/main/resources/static/company/img/";


    @Override
    public Page<Company> getCompany(int page,int limit) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
        Pageable pageable= PageRequest.of(page,limit,Sort.by(order));
        return companyRepository.findAll(pageable);
    }

    @Override
    public Company getCompanyById(Long id) throws Exception {
        Optional<Company> result = companyRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new Exception("Couldn't find ID " + id);
    }
    @Override
    public Company getCompanyById(AuthUser authUser) throws Exception {
        User user = userService.getByUserNameOrEmail(authUser.getUsername(),authUser.getUsername());
        Long companyId = user.getCompany().getId();
        Optional<Company> result = companyRepository.findById(companyId);
        if (result.isPresent()) {
            return result.get();
        }
        throw new Exception("Couldn't find ID " + companyId);
    }

    @Override
    public void saveOrUpdate(Company company) {
        companyRepository.save(company);
    }

    @Override
    public void toggleStatusAndRole(Long id) throws Exception {
        Optional<Company> companyOptional = companyRepository.findById(id);

        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            User user = userService.getById(company.getUser().getId());

            if (company.getStatus() == Status.APPROVED) {
                company.setStatus(Status.REJECTED);
            } else {
                company.setStatus(Status.APPROVED);
            }

            if (company.getStatus() == Status.APPROVED) {
                user.setRole(Role.COMPANY);
            } else {
                user.setRole(Role.USER);
            }

            companyRepository.save(company);
            userService.save(user);
        }
    }

    @Override
    public void saveOrUpdate(CompanyEditForm companyEditForm, MultipartFile logo, MultipartFile cover) throws IOException {
        Company existingCompany = companyRepository.findById(companyEditForm.getId()).orElse(null);
        existingCompany.setName(companyEditForm.getName());
        existingCompany.setPhone(companyEditForm.getPhone());
        existingCompany.setAddress(companyEditForm.getAddress());
        if (logo != null && !logo.isEmpty()) {
            String logoFileName = saveFile(logo,UPLOAD_DIRECTORY);
            existingCompany.setLogo(logoFileName);

            if (existingCompany != null && !Objects.equals(existingCompany.getLogo(), logoFileName)) {
                deleteFile(existingCompany.getLogo(),UPLOAD_DIRECTORY);
            }
        }
        if (cover != null && !cover.isEmpty()) {
            String coverFileName = saveFile(cover,UPLOAD_DIRECTORY);
            existingCompany.setCoverPhoto(coverFileName);

            if (existingCompany != null && !Objects.equals(existingCompany.getCoverPhoto(), coverFileName)) {
                deleteFile(existingCompany.getCoverPhoto(),UPLOAD_DIRECTORY);
            }
        }
        companyRepository.save(existingCompany);
    }

    private String saveFile(MultipartFile file,String uploadDirectory) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String filePath = uploadDirectory + fileName;
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        // Determine the target directory within your project
        String targetDirectory = "target/classes/static/company/img/";
        String targetFilePath = targetDirectory + fileName;
        Files.copy(file.getInputStream(), Paths.get(targetFilePath), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    private void deleteFile(String filePath,String uploadDirectory) {
        if (StringUtils.isNotBlank(filePath)) {
            File fileToDelete = new File(uploadDirectory + filePath);
            if (fileToDelete.exists() && fileToDelete.isFile()) {
                fileToDelete.delete();
            }
        }
    }

    @Override
    public boolean isCompanyNameUnique(String name) {
        return  companyRepository.findByName(name) == null;
    }

}
