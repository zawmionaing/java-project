package com.java.jobportal.service;

import com.java.jobportal.form.CompanyEditForm;
import com.java.jobportal.model.AuthUser;
import com.java.jobportal.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public interface CompanyService {
    
    Page<Company> getCompany(int page,int limit);
    
    Company getCompanyById(Long id) throws Exception;

    Company getCompanyById(AuthUser authUser) throws Exception;

    void saveOrUpdate(Company company);

    void toggleStatusAndRole(Long id) throws Exception;

    void saveOrUpdate(CompanyEditForm companyEditForm, MultipartFile logo, MultipartFile cover) throws IOException;

    boolean isCompanyNameUnique(String name);
}
