package ru.aperepelkin.revolut.account;

import org.apache.commons.lang3.tuple.Pair;
import org.jooq.DSLContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.aperepelkin.revolut.account.bootstrap.Bootstrap;
import ru.aperepelkin.revolut.account.bootstrap.DatabaseComponent;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class AccountEndpointTest {

    private Bootstrap bootstrap;

    private DSLContext dsl;
    private AccountEndpoint endpoint;

    private AccountModel example1;
    private AccountModel example2;

    @BeforeClass
    public void init() {
        bootstrap = Bootstrap.builder()
                .database()
                .jdbc("jdbc:h2:mem:accounts")
                .migrate()
                .build();

        bootstrap.run();

        assertThat(bootstrap.getComponent(DatabaseComponent.class).isPresent(), is(true));

        dsl = bootstrap.getComponent(DatabaseComponent.class).get().dsl();
        endpoint = new AccountEndpoint(new AccountService(dsl));

        example1 = new AccountModel();
        example1.setId(1L);
        example1.setNumber("0000000001");
        example1.setBalance(BigDecimal.valueOf(2000.00));

        example2 = new AccountModel();
        example2.setId(2L);
        example2.setNumber("0000000002");
        example2.setBalance(BigDecimal.valueOf(3000.00));
    }

    @AfterClass
    public void cleanup() {
        bootstrap.shutdown();
    }

    @Test
    public void list() {
        assertThat(endpoint.list(), hasItems(example1, example2));
    }

    @Test
    public void get() {
        assertThat(endpoint.get(1L), equalTo(example1));
    }

    @Test(dependsOnMethods = "list")
    public void create() {
        AccountModel accountModel = endpoint.create();
        assertThat(accountModel, notNullValue());
        assertThat(accountModel.getId(), equalTo(3L));
        assertThat(accountModel.getNumber(), equalTo("0000000003"));
        assertThat(accountModel.getBalance(), equalTo(BigDecimal.ZERO));
    }

    @Test()
    public void enroll() {
        AccountModel accountModel = endpoint.enroll(1L, BigDecimal.valueOf(1000.0));
        assertThat(accountModel, notNullValue());
        assertThat(accountModel.getBalance(), equalTo(BigDecimal.valueOf(300000, 2)));
    }

    @Test(dependsOnMethods = "enroll")
    public void withdraw() {
        AccountModel accountModel = endpoint.withdraw(1L, BigDecimal.valueOf(1000.0));
        assertThat(accountModel, notNullValue());
        assertThat(accountModel.getBalance(), equalTo(BigDecimal.valueOf(200000, 2)));
    }

    @Test(dependsOnMethods = {"enroll", "withdraw"})
    public void transfer() {
        Pair<AccountModel, AccountModel> transfer = endpoint.transfer(1L, 2L, BigDecimal.valueOf(400.0));
        assertThat(transfer, notNullValue());
        assertThat(transfer.getLeft().getId(), equalTo(1L));
        assertThat(transfer.getRight().getId(), equalTo(2L));
        assertThat(transfer.getLeft().getBalance(), equalTo(BigDecimal.valueOf(160000, 2)));
        assertThat(transfer.getRight().getBalance(), equalTo(BigDecimal.valueOf(340000, 2)));
    }
}
