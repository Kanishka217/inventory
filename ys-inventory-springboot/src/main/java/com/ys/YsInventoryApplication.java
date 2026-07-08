package com.ys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This one class replaces everything web.xml + Tomcat used to do.
 * Running this file's main() starts an embedded Tomcat server on port 8080
 * (or whatever PORT env var Render gives us) — no separate Tomcat install,
 * no WAR file, no deployment directory.
 */
@SpringBootApplication
public class YsInventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(YsInventoryApplication.class, args);
    }
}
