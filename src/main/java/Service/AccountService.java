package Service;

import Model.Account;
import DAO.AccountDAO;
import java.util.List;

public class AccountService {

    private AccountDAO accountDAO; 
    
    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public Account registerAccount(String username, String password){
        //check to make sure the conditions for registering are met
        boolean usernameBlank = (username == "");
        boolean passwordTooShort = (password.length() < 4);
        boolean userNameTaken = (accountDAO.getAccountByUserName(username) != null);

        if(usernameBlank || passwordTooShort || userNameTaken){
            return null;
        }

        return accountDAO.registerAccount(username, password);
    }

    public Account login(String username, String password){
       return accountDAO.login(username, password);

    }


}
