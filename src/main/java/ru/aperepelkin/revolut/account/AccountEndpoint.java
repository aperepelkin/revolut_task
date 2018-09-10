package ru.aperepelkin.revolut.account;

import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.math.BigDecimal;
import java.util.List;

@Path("/accounts")
@Produces("application/json")
@Consumes("application/json")
public class AccountEndpoint {

    private AccountService service;

    @Inject
    public AccountEndpoint(AccountService service) {
        this.service = service;
    }

    @GET
    public List<AccountModel> list() {
        return service.list();
    }

    @GET
    @Path("{id}")
    public AccountModel get(@PathParam("id") Long id) {
        return service.get(id);
    }

    @GET
    @Path("create")
    public AccountModel create() {
        return service.create();
    }

    @PUT
    @Path("{id}/enroll")
    @Consumes("text/plain")
    public AccountModel enroll(@PathParam("id") Long id, BigDecimal amount) {
        checkForNegativeAmount(amount);
        return service.add(id, amount);
    }

    @PUT
    @Path("{id}/withdraw")
    @Consumes("text/plain")
    public AccountModel withdraw(@PathParam("id") Long id, BigDecimal amount) {
        checkForNegativeAmount(amount);
        return service.add(id, amount.negate());
    }

    @DELETE
    @Path("{id}")
    public void close(@PathParam("id") Long id) {
        try {
            service.close(id);
        } catch (NonZeroBalanceException e) {
            throw new BadRequestException(e);
        }
    }

    @PUT
    @Path("{id}/transfer/{to}")
    @Consumes("text/plain")
    public Pair<AccountModel, AccountModel> transfer(@PathParam("id") Long id, @PathParam("to") Long to, BigDecimal amount) {
        checkForNegativeAmount(amount);
        return service.transfer(id, to, amount);
    }

    private void checkForNegativeAmount(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0)
            throw new BadRequestException("negative value could not be used");

    }
}
