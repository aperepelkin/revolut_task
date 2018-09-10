package ru.aperepelkin.revolut.account;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import ru.aperepelkin.revolut.account.jooq.tables.records.AccountsRecord;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.util.List;

import static ru.aperepelkin.revolut.account.jooq.Sequences.ACCOUNT_ID_SEQ;
import static ru.aperepelkin.revolut.account.jooq.tables.Accounts.ACCOUNTS;

@Named
public class AccountService {

    private DSLContext dsl;

    @Inject
    public AccountService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<AccountModel> list() {
        return dsl.selectFrom(ACCOUNTS)
                .fetch()
                .map(this::toModel);
    }

    public AccountModel get(Long id) {
        return dsl.selectFrom(ACCOUNTS).where(ACCOUNTS.ID.eq(id))
                .fetchOptional()
                .orElseThrow(NotFoundException::new)
                .map(record -> toModel((AccountsRecord) record));
    }

    public AccountModel create() {
        Long id = dsl.select(ACCOUNT_ID_SEQ.nextval()).fetchOne().value1();
        String number = StringUtils.leftPad(id.toString(), 10, '0');

        AccountsRecord record = new AccountsRecord();
        record.setId(id);
        record.setNumber(number);
        record.setBalance(BigDecimal.ZERO);

        dsl.executeInsert(record);

        return toModel(record);
    }

    public AccountModel add(Long id, BigDecimal amount) {
        return dsl.transactionResult(tr -> {
            AccountsRecord record = DSL.using(tr).selectFrom(ACCOUNTS)
                    .where(ACCOUNTS.ID.eq(id))
                    .forUpdate()
                    .fetchOne();
            record.setBalance(record.getBalance().add(amount));

            if(record.getBalance().compareTo(BigDecimal.ZERO) < 0)
                throw new NegativeBalanceException();

            record.update();
            return toModel(record);
        });
    }

    public void close(Long id) {
        AccountsRecord record = dsl.selectFrom(ACCOUNTS).where(ACCOUNTS.ID.eq(id))
                .fetchOptional()
                .orElseThrow(NotFoundException::new);
        if(record.getBalance().compareTo(BigDecimal.ZERO) > 0)
            throw new NonZeroBalanceException();
        record.delete();
    }

    public Pair<AccountModel, AccountModel> transfer(Long fromId, Long toId, BigDecimal amount) {
        return dsl.transactionResult(tr -> {
            AccountsRecord from;
            AccountsRecord to;

            // preventing from deadlock
            if(fromId > toId) {
                to = fetchForUpdate(toId, DSL.using(tr));
                from = fetchForUpdate(fromId, DSL.using(tr));
            } else {
                from = fetchForUpdate(fromId, DSL.using(tr));
                to = fetchForUpdate(toId, DSL.using(tr));
            }

            from.setBalance(from.getBalance().subtract(amount));
            if(from.getBalance().compareTo(BigDecimal.ZERO) < 0)
                throw new NegativeBalanceException();
            to.setBalance(to.getBalance().add(amount));
            from.update();
            to.update();

            return Pair.of(toModel(from), toModel(to));
        });
    }

    private AccountModel toModel(AccountsRecord record) {
        AccountModel result = new AccountModel();
        result.setId(record.getId());
        result.setBalance(record.getBalance());
        result.setNumber(record.getNumber());
        return result;
    }

    private AccountsRecord fetchForUpdate(Long id, DSLContext dsl) {
        return dsl.selectFrom(ACCOUNTS)
                .where(ACCOUNTS.ID.eq(id))
                .forUpdate()
                .fetchOptional()
                .orElseThrow(NotFoundException::new);
    }
}
