package org.opcfoundation.uawebservicedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages  = {
        "org.opcfoundation.webapi",
        "org.opcfoundation.uawebservicedemo"
})
public class UaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UaServerApplication.class, args);
    }
}
