package com.java.jobportal.config;

import com.java.jobportal.model.*;
import com.java.jobportal.repository.CategoryRepository;
import com.java.jobportal.repository.LocationRepository;
import com.java.jobportal.repository.TownshipRepository;
import com.java.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TownshipRepository townshipRepository;

    @Autowired
    LocationRepository locationRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder() ;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(userRepository.count() == 0){
            String encodePassword1 = bCryptPasswordEncoder.encode("admin123");
            User user1 = new User("admin","admin@gmail.com",encodePassword1, Role.ADMIN);

            String encodePassword2 = bCryptPasswordEncoder.encode("mgmg1234");
            User user2 = new User("mgmg","mgmg@gmail.com",encodePassword2, Role.USER);

            userRepository.save(user1);
            userRepository.save(user2);
        }

        if (categoryRepository.count() == 0) {
            Category category1 = new Category("Sales Business development");
            Category category2 = new Category("Marketing Media Creative");
            Category category3 = new Category("IT Hardware Software");
            Category category4 = new Category("Education Teaching Childcare");
            Category category5 = new Category("Administrative");

            categoryRepository.save(category1);
            categoryRepository.save(category2);
            categoryRepository.save(category3);
            categoryRepository.save(category4);
            categoryRepository.save(category5);
        }

        if (townshipRepository.count() == 0) {
            Location location1 = new Location("Yangon");
            locationRepository.save(location1);

            Location location2 = new Location("Mandalay");
            locationRepository.save(location2);

            String[] location1TownshipNames = {
                    "Mayangone", "Dagon", "Thingangyun", "Kyeemyidaing", "Hlaing"
            };

            String[] location2TownshipNames = {
                    "Patheingyi", "Amarapura", "Chanayethazen", "Pyigyidagun", "MahaAungmye"
            };

            for (String townshipName : location1TownshipNames) {
                Township township = new Township(townshipName, location1);
                townshipRepository.save(township);
            }

            for (String townshipName : location2TownshipNames) {
                Township township = new Township(townshipName, location2);
                townshipRepository.save(township);
            }
        }
    }
}
