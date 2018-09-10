package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.aperepelkin.revolut.account.jooq.tables.records.AccountsRecord;

import java.math.BigDecimal;
import java.sql.Connection;

import static ru.aperepelkin.revolut.account.jooq.Sequences.ACCOUNT_ID_SEQ;

public class R__TestData implements JdbcMigration {
    @Override
    public void migrate(Connection connection) {
        DSLContext dsl = DSL.using(connection, SQLDialect.H2);
        AccountsRecord record = new AccountsRecord();
        record.setId(1L);
        record.setNumber("0000000001");
        record.setBalance(BigDecimal.valueOf(2000.00));

        dsl.executeInsert(record);

        record = new AccountsRecord();
        record.setId(2L);
        record.setNumber("0000000002");
        record.setBalance(BigDecimal.valueOf(3000.00));

        dsl.executeInsert(record);

        dsl.select(ACCOUNT_ID_SEQ.nextval()).fetchOne();
        dsl.select(ACCOUNT_ID_SEQ.nextval()).fetchOne();
    }
}
