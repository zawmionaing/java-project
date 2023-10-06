package com.java.jobportal.service;

import com.java.jobportal.model.Township;
import com.java.jobportal.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TownshipService {

    Page<Township> getTownships(int page , int limit);

}
