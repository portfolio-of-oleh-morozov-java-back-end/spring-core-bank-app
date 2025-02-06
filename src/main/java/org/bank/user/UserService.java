package org.bank.user;

import org.bank.account.Account;
import org.bank.account.AccountService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final Map<Integer, User> userMap;
    private final Set<String> takenLogins;
    private int idCounter;
    private final AccountService accountService;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
        this.takenLogins = new HashSet<>();
        this.userMap = new HashMap<>();
        this.idCounter = 0;
    }

    public User createUser(String login) {
        if (takenLogins.contains(login)) {
            throw new IllegalArgumentException("User already exists with login=%s"
                    .formatted(login));
        }
        takenLogins.add(login);
        idCounter++;
        User newUser = new User(idCounter, login, new ArrayList<>());
        Account newAccount = accountService.createAccount(newUser);
        newUser.getAccountList().add(newAccount);
        userMap.put(newUser.getId(), newUser);
        return newUser;
    }

    public Optional<User> findUsersById(int id) {
        return Optional.ofNullable(userMap.get(id));
    }

    public List<User> getAllUsers() {
        return userMap.values().stream().toList();
    }
}
