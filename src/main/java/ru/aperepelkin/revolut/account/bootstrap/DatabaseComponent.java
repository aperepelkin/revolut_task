package ru.aperepelkin.revolut.account.bootstrap;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DatabaseComponent implements Component {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseComponent.class);

    private List<Consumer<DataSource>> handlers = new ArrayList<>();

    private String jdbcUrl;

    private HikariDataSource dataSource;

    DatabaseComponent() {
    }

    @Override
    public void run() {
        logger.info("Starting database");
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        handlers.forEach(h -> h.accept(dataSource));
        logger.info("Database started");
    }

    @Override
    public void shutdown() {
        dataSource.close();
        logger.info("Database stopped");
    }

    public DSLContext dsl() {
        return DSL.using(dataSource, SQLDialect.H2);
    }

    void setJdbc(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    void addAfterHandler(Consumer<DataSource> handler) {
        handlers.add(handler);
    }
}
