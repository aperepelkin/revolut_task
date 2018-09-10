package ru.aperepelkin.revolut.account;

import org.jooq.DSLContext;
import ru.aperepelkin.revolut.account.bootstrap.Bootstrap;
import ru.aperepelkin.revolut.account.bootstrap.DatabaseComponent;

import javax.enterprise.inject.Produces;

public class DSLContextProducer {

    @Produces
    public DSLContext produceDSLContext() {
        return Bootstrap.getInstance().getComponent(DatabaseComponent.class)
                .map(DatabaseComponent::dsl)
                .orElseThrow(() -> new RuntimeException("No DSLContext found"));
    }
}
