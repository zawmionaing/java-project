package com.java.jobportal.form;

import com.java.jobportal.model.Job;
import com.java.jobportal.model.JobType;
import com.java.jobportal.model.Township;
import com.java.jobportal.model.SubCategory;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class JobForm {

    private Long id;

    @Size(min = 5, max = 100, message = "Job title must be between 5 and 100 characters")
    private String jobTitle;

    @Size(max = 255, message = "Job photo URL must not exceed 255 characters")
    private String jobPhoto;

    @Size(max = 255, message = "Short description must not exceed 255 characters")
    @NotBlank(message = "This field is required")
    private String shortDescription;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @NotBlank(message = "This field is required")
    private String description;

    @Size(max = 500, message = "Requirement must not exceed 500 characters")
    @NotBlank(message = "This field is required")
    private String requirement;
    
    @Pattern(regexp = "^[0-9]+(\\.[0-9]{1,2})?$", message = "Salary must be positive numbers and digits")
    private String salary;

    @NotNull(message = "Select jobtype")
    private JobType jobType;

    @NotNull(message = "Select township")
    private Township township;

    @NotNull(message = "Select subcategory")
    private SubCategory subCategory;

    public MultipartFile getJobPhotoImg() {
        return jobPhotoImg;
    }

    public void setJobPhotoImg(MultipartFile jobPhotoImg) {
        this.jobPhotoImg = jobPhotoImg;
    }

    private MultipartFile jobPhotoImg;

    public boolean isImage() {
        if (jobPhotoImg == null || jobPhotoImg.isEmpty()) {
            // If the file is empty, it's not considered an image
            return false;
        }

        // Get the content type of the file
        String contentType = jobPhotoImg.getContentType();

        // Check if the content type indicates an image (you can expand this list)
        return contentType != null && (contentType.startsWith("image/jpeg") ||
                contentType.startsWith("image/png") ||
                contentType.startsWith("image/gif"));
    }

    public boolean isSizeValid() {
        if (jobPhotoImg == null || jobPhotoImg.isEmpty()) {
            // If the file is empty or null, it's considered valid (no size limit)
            return true;
        }

        // Define the maximum allowed file size (2MB)
        long maxFileSize = 2 * 1024 * 1024; // 2MB in bytes

        // Check if the file size is less than or equal to the maximum size
        return jobPhotoImg.getSize() <= maxFileSize;
    }

    public JobForm(Job job) {
        this.id = job.getId();
        this.jobTitle = job.getJobTitle();
        this.jobPhoto = job.getJobPhoto();
        this.shortDescription = job.getShortDescription();
        this.description = job.getDescription();
        this.requirement = job.getRequirement();
        this.salary = job.getSalary();
        this.jobType = job.getJobType();
        this.township = job.getTownship();
        this.subCategory = job.getSubCategory();
        this.jobPhotoImg = null;
    }
}
