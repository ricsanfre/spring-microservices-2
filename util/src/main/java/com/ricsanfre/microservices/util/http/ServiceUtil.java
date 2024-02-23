package com.ricsanfre.microservices.util.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ServiceUtil {

    // Port where the microservice is running on obtained from application.yaml
    @Value("${server.port}")
    private String port;

    // Microservice Service address: Hostname + IP Address
    private String serviceAddress = null;

    public String getPort() {
        return port;
    }

    // Get ServiceAddress
    public String getServiceAddress() {
        if (serviceAddress == null) {
            serviceAddress = findMyHostname() + "/" + findMyIpAddress() + ":" + port;
        }
        return serviceAddress;
    }
    private String findMyHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown host name";
        }
    }

    private String findMyIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown IP address";
        }
    }
}
