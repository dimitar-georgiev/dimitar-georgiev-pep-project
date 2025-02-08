package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account userRegistration(Account account) {
        return accountDAO.userRegistration(account);
    }

    public Account userLogin (Account account) {
        return accountDAO.userLogin(account);
    }
}
