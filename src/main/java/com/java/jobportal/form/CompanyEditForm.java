package com.java.jobportal.form;

import com.java.jobportal.model.Company;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@NoArgsConstructor
public class CompanyEditForm {

    private Long id;

    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    @Column(nullable = false)
    private String name;

    @Pattern(regexp = "^[0-9]+$", message = "Phone must contain only digits")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(nullable = false)
    private String address;

    private String logo;

    private String coverPhoto;

    private MultipartFile logoImg;

    private MultipartFile coverPhotoImg;

    public CompanyEditForm(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.phone = company.getPhone();
        this.address = company.getAddress();
        this.logo = company.getLogo();
        this.coverPhoto = company.getCoverPhoto();
        this.logoImg = null;
        this.coverPhotoImg = null;
    }


    public boolean isImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String contentType = file.getContentType();
        return contentType != null && (contentType.startsWith("image/jpeg") ||
                contentType.startsWith("image/png") ||
                contentType.startsWith("image/gif"));
    }

    public boolean isSizeValid(MultipartFile file, long maxFileSize) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        return file.getSize() <= maxFileSize;
    }
}
