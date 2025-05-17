package projetoteste;

import framework.anotations.type.SpringApplication;
import framework.container.SpringApplicationRun;

import java.io.IOException;

@SpringApplication
public class Main {
    public static void main(String[] args) throws IOException {
        SpringApplicationRun.start();
    }
}