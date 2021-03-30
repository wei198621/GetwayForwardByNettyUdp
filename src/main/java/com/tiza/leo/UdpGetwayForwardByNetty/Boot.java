package com.tiza.leo.UdpGetwayForwardByNetty;

import com.tiza.leo.UdpGetwayForwardByNetty.deamon.GatewayDeamon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author leowei
 * @date 2021/3/30  - 23:00
 */
@SpringBootApplication
public class Boot implements ApplicationRunner {

    @Autowired
    GatewayDeamon gatewayDeamon;

    public static void main(String[] args) {
        SpringApplication.run(Boot.class,args);
    }

    //执行网关守护进程
    @Override
    public void run(ApplicationArguments args) throws Exception {
        gatewayDeamon.start();
    }
}
