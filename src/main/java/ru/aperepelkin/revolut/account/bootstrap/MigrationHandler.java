package ru.aperepelkin.revolut.account.bootstrap;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;

public class MigrationHandler implements Consumer<DataSource> {

    private static final Logger logger = LoggerFactory.getLogger(MigrationHandler.class);

    private List<String> locations = new ArrayList<>(singletonList("classpath:db.migration"));

    @Override
    public void accept(DataSource dataSource) {
        logger.info("Migrating database");
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations(locations.toArray(new String[locations.size()]));
        flyway.migrate();
        logger.info("Database migrated");
    }

    void addLocation(String location) {
        locations.add(location);
    }
}
