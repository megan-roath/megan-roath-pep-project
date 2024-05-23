package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /*
     * Create a new account.
     * 
     * @param account the account to be created.
     * @return returns an account with it's account_id unless the 
     *         username is blank or password is less than 4 characters.
     */
    public Account addAccount(Account account){
        //Checks for restraints on the accout requirements
        if(account.getUsername().equals("") || account.getPassword().length() < 4){
            return null;
        } else {
            return this.accountDAO.newAccount(account);
        }
    }

    /*
     * Add a new account to the database.
     * 
     * @param account: an object representing a new Account.
     * @return the new account
     */
    public Account login(Account account){
        return this.accountDAO.getAccounts(account);
    }

}
