package ru.aperepelkin.revolut.account;

import javax.ws.rs.BadRequestException;

public class NonZeroBalanceException extends BadRequestException {
}
