package ru.aperepelkin.revolut.account;

import ru.aperepelkin.revolut.account.bootstrap.Bootstrap;

import java.io.IOException;
import java.util.logging.LogManager;

public class Application {

    public static void main(String[] args) throws IOException {
        LogManager.getLogManager().readConfiguration(ClassLoader.getSystemResourceAsStream("log.properties"));
        Bootstrap bootstrap = Bootstrap.builder()
                .daemon()
                .httpServer().port(8080)
                    .and()
                .database().jdbc("jdbc:h2:mem:accounts").migrate().demo()
                .build();
        bootstrap.run();
    }
}
