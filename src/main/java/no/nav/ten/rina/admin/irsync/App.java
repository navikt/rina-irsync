package no.nav.ten.rina.admin.irsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
@EnableResilientMethods
public class App {
  static void main(String[] args) {
    SpringApplication.run(App.class, args);
  } }
