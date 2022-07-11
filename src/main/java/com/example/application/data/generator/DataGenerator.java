package com.example.application.data.generator;

import com.example.application.data.entity.Car;
import com.example.application.data.entity.User;
import com.example.application.data.service.CarRepository;
import com.example.application.data.service.CarService;
import com.example.application.data.service.CarSimulator;
import com.example.application.data.service.UserRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringComponent
@Profile("default")
public class DataGenerator {

    @Autowired
    Environment env;

    @Bean
    public CommandLineRunner loadData(ApplicationContext applicationContext,
                                      PasswordEncoder passwordEncoder,
                                      UserRepository userRepository,
                                      CarRepository carRepository,
                                      SimpleAsyncTaskExecutor simpleAsyncTaskExecutor) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 1 User entities...");
            User user = new User();
            user.setName("John Normal");
            user.setUsername("user");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            user.setRole(User.Role.TEAM);
            userRepository.save(user);

            logger.info("Generated demo data");

            Car car1 = new Car();
            car1.setName("RedBull - Max Verstappen");

            carRepository.save(car1);

            simpleAsyncTaskExecutor.execute(new CarSimulator(car1, applicationContext.getBean(CarService.class)));
        };
    }

}