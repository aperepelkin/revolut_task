package demo.db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.aperepelkin.revolut.account.AccountService;

import java.math.BigDecimal;
import java.sql.Connection;

public class R__DemoData implements JdbcMigration {
    @Override
    public void migrate(Connection connection) {
        AccountService service = new AccountService(DSL.using(connection, SQLDialect.H2));
        Long id = service.create().getId();
        service.add(id, BigDecimal.valueOf(2000));
        id = service.create().getId();
        service.add(id, BigDecimal.valueOf(3000));
        id = service.create().getId();
        service.add(id, BigDecimal.valueOf(4000));
    }
}
