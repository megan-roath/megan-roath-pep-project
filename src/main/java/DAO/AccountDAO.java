package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    private Connection connection = ConnectionUtil.getConnection();

/*
* Retrieve an account given the username and password.
*/
public Account getAccounts(Account account){
    try{
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        PreparedStatement ps = this.connection.prepareStatement(sql);

        String user = account.getUsername();
        String pass = account.getPassword();
        ps.setString(1, user);
        ps.setString(2, pass);

        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Account idAccount = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            return idAccount;
        }
    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
}

/*
 * Persist a new account to the database.
 */
public Account newAccount(Account account){
    try{
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        String user = account.getUsername();
        String pass = account.getPassword();
        ps.setString(1, user);
        ps.setString(2, pass);

        ps.executeUpdate();
        ResultSet pkeyResultSet = ps.getGeneratedKeys();
        if(pkeyResultSet.next()){
            int generated_account_id = (int) pkeyResultSet.getLong(1);
            return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

/*
 * Retrieve an account using its account_id.
 * 
 * @param account_id an account id, linked to the messages they post.
 */
public Account getAccountByID(int account_id){
    try{
        String sql = "SELECT * FROM account WHERE account_id = ?";
        PreparedStatement ps = this.connection.prepareStatement(sql);

        ps.setInt(1, account_id);

        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Account account = new Account(rs.getInt("account_id"), 
                        rs.getString("username"), 
                        rs.getString("password"));
            return account;
        }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
}

/*
 * Delete an account by account_id.
 * 
 * @param account_id an account id, linked to the messages they post.
 */
public Account deleteAccount(Account account){
    try{
        String sql = "DELETE FROM account WHERE account_id = ?";
        PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        int id = account.getAccount_id();
        ps.setInt(1, id);

        int deleted = ps.executeUpdate();
        if(deleted > 0){
            System.out.println("Account Deleted");
            return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
}

}
