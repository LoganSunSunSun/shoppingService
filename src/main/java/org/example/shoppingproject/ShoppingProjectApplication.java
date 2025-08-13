package org.example.shoppingproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class ShoppingProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingProjectApplication.class, args);
    }

}
