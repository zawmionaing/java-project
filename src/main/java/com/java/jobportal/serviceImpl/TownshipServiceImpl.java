package com.java.jobportal.serviceImpl;

import com.java.jobportal.model.Township;
import com.java.jobportal.repository.TownshipRepository;
import com.java.jobportal.service.TownshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TownshipServiceImpl implements TownshipService {

    @Autowired
    TownshipRepository townshipRepository;

    @Override
    public Page<Township> getTownships(int page ,int limit) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
        Pageable pageable= PageRequest.of(page,limit,Sort.by(order));
        return townshipRepository.findAll(pageable);
    }
}
