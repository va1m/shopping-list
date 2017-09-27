package com.github.va1m.shopping;

import com.github.va1m.shopping.entities.EntityScanMarker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * The startup class with main method
 */
@SpringBootApplication
@EntityScan(basePackageClasses = EntityScanMarker.class)
public class TheApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheApplication.class, args);
    }

}
