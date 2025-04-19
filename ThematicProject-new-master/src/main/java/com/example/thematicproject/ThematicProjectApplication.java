package com.example.thematicproject;

import jakarta.persistence.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SpringBootApplication
public class ThematicProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThematicProjectApplication.class, args);
    }

}
