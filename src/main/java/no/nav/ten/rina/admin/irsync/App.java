package no.nav.ten.rina.admin.irsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class App { public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  } }
