package ru.aperepelkin.revolut.account;

import ru.aperepelkin.revolut.account.bootstrap.Bootstrap;

public class Application {

    public static void main(String[] args) {
        Bootstrap bootstrap = Bootstrap.builder()
                .daemon()
                .httpServer().port(8080)
                    .and()
                .database().jdbc("jdbc:h2:mem:accounts").migrate().demo()
                .build();
        bootstrap.run();
    }
}
