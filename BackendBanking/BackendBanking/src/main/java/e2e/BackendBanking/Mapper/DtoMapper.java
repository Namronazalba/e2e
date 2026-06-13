package e2e.BackendBanking.Mapper;

import e2e.BackendBanking.Dto.Account.AccountDto;
import e2e.BackendBanking.Dto.User.UserDto;
import e2e.BackendBanking.Model.Account.Account;
import e2e.BackendBanking.Model.User.User;
import e2e.BackendBanking.Dto.Transaction.TransactionResponse;
import e2e.BackendBanking.Model.Transaction.Transaction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DtoMapper {

    public static AccountDto toAccountDto(Account acc) {
        return new AccountDto(
                acc.getId(),
                acc.getAccountNumber(),
                acc.getAccountType().name(),
                acc.getBalance(),
                acc.getStatus().name(),
                "PHP",
                acc.getCreatedAt()
        );
    }

    public static UserDto toUserDto(User user) {

        List<AccountDto> accounts = new ArrayList<>();

        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getPhone(),
                user.getAddress(),
                user.getCreatedAt(),
                user.getLastLogin(),
                user.getRole()
        );
    }

    public TransactionResponse toResponse(
            Transaction transaction,
            Account fromAccount,
            Account toAccount,
            Double balance) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getAmount(),
                transaction.getTimestamp(),
                fromAccount != null
                        ? fromAccount.getAccountNumber()
                        : null,
                toAccount != null
                        ? toAccount.getAccountNumber()
                        : null,
                transaction.getReference()
        );
    }
}