/*
 * This file is generated by jOOQ.
 */
package ru.aperepelkin.revolut.account.jooq.tables;


import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import ru.aperepelkin.revolut.account.jooq.Indexes;
import ru.aperepelkin.revolut.account.jooq.Keys;
import ru.aperepelkin.revolut.account.jooq.Public;
import ru.aperepelkin.revolut.account.jooq.tables.records.AccountsRecord;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Accounts extends TableImpl<AccountsRecord> {

    private static final long serialVersionUID = 2146362812;

    /**
     * The reference instance of <code>PUBLIC.ACCOUNTS</code>
     */
    public static final Accounts ACCOUNTS = new Accounts();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AccountsRecord> getRecordType() {
        return AccountsRecord.class;
    }

    /**
     * The column <code>PUBLIC.ACCOUNTS.ID</code>.
     */
    public final TableField<AccountsRecord, Long> ID = createField("ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ACCOUNTS.NUMBER</code>.
     */
    public final TableField<AccountsRecord, String> NUMBER = createField("NUMBER", org.jooq.impl.SQLDataType.VARCHAR(10).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ACCOUNTS.BALANCE</code>.
     */
    public final TableField<AccountsRecord, BigDecimal> BALANCE = createField("BALANCE", org.jooq.impl.SQLDataType.DECIMAL(20, 2).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.DECIMAL)), this, "");

    /**
     * Create a <code>PUBLIC.ACCOUNTS</code> table reference
     */
    public Accounts() {
        this(DSL.name("ACCOUNTS"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.ACCOUNTS</code> table reference
     */
    public Accounts(String alias) {
        this(DSL.name(alias), ACCOUNTS);
    }

    /**
     * Create an aliased <code>PUBLIC.ACCOUNTS</code> table reference
     */
    public Accounts(Name alias) {
        this(alias, ACCOUNTS);
    }

    private Accounts(Name alias, Table<AccountsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Accounts(Name alias, Table<AccountsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Accounts(Table<O> child, ForeignKey<O, AccountsRecord> key) {
        super(child, key, ACCOUNTS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.CONSTRAINT_INDEX_A, Indexes.PRIMARY_KEY_A);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<AccountsRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_A;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<AccountsRecord>> getKeys() {
        return Arrays.<UniqueKey<AccountsRecord>>asList(Keys.CONSTRAINT_A, Keys.CONSTRAINT_AF);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Accounts as(String alias) {
        return new Accounts(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Accounts as(Name alias) {
        return new Accounts(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Accounts rename(String name) {
        return new Accounts(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Accounts rename(Name name) {
        return new Accounts(name, null);
    }
}