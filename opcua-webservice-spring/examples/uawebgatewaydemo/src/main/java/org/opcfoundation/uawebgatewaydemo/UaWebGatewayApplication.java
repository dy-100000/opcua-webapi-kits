package org.opcfoundation.uawebgatewaydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages  = {
        "org.opcfoundation.webapi",
        "org.opcfoundation.uawebgatewaydemo",
})
public class UaWebGatewayApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(UaWebGatewayApplication.class, args);
    }
}
