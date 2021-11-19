package com.example.springbooteurekaspaceship.rest;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/spaceship")
public class SpaceShipRestController {

    public SpaceShipRestController(DiscoveryClient discoveryClient, EurekaClient eurekaClient) {
        this.discoveryClient = discoveryClient;
        this.eurekaClient = eurekaClient;
    }

    private final DiscoveryClient discoveryClient;
    private final EurekaClient eurekaClient;



    @GetMapping("/greeting")
    public String greeting(){
        for (String serv : this.discoveryClient
                .getServices()){
            System.out.println("Service: " + serv);
        }

        List<InstanceInfo> instances = eurekaClient.getApplication("spacestation")
                .getInstances();
        System.out.println("We have found number of instances: " + instances.size());
        for (InstanceInfo spacestation : instances){
            System.out.println("Instance: " + spacestation.getPort());
        }

        InstanceInfo spacestation = instances.get(0);
        String host = spacestation.getHostName();
        int port = spacestation.getPort();
        String url = "http://" + host + ":" + port + "/spacestation/destination";
        String result = new RestTemplate().getForObject(url, String.class);
        System.out.println("The result for url: " + url + " is: " + result);
        return "The result for url: " + url + " is: " + result;
    }
}
