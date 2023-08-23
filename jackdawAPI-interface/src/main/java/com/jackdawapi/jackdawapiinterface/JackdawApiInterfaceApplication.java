package com.jackdawapi.jackdawapiinterface;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.File;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JackdawApiInterfaceApplication {

    public static void main(String[] args)  {
        SpringApplication.run(JackdawApiInterfaceApplication.class, args);
    }

}
