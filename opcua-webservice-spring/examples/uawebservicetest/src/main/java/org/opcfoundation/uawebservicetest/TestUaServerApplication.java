package org.opcfoundation.uawebservicetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages  = {
        "org.opcfoundation.webapi",
        "org.opcfoundation.uawebservicetest"
})
public class TestUaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestUaServerApplication.class, args);
    }
}
