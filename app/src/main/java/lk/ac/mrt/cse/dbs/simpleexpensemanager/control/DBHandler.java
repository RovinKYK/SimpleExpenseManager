package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

//This is a helper class handling all interactions with the database
public class DBHandler extends SQLiteOpenHelper {
    private static DBHandler dbHandler;

    //Constructor made private
    private DBHandler(@Nullable Context context) {
        super(context, "200543U", null, 1);
    }

    //DBHandler made a singleton to ensure only one instance created
    public static DBHandler getDBHandler(@Nullable Context context) {
        if (dbHandler == null) {
            dbHandler = new DBHandler(context);
        }
        return dbHandler;
    }

    //Creating tables in the first time when app is opened
    @Override
    public void onCreate(SQLiteDatabase database) {
        String createAccTable = "CREATE TABLE Accounts(Account_No TEXT PRIMARY KEY, Bank TEXT, Account_Holder TEXT, Balance REAL)";
        String createTransactionTable = "CREATE TABLE Transactions(Transaction_ID INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT, Account_No TEXT, Type TEXT, Amount REAL, FOREIGN KEY(Account_No) REFERENCES Accounts(Account_No))";
        database.execSQL(createAccTable);
        database.execSQL(createTransactionTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    //Returning all acoounts in the database as a list
    public List<Account> getAllAccounts() {
        List<Account> initialAccounts = new ArrayList<>();
        String getAllAccounts = "SELECT * FROM Accounts";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(getAllAccounts, null);

        if(cursor.moveToFirst()) {
            do {
                Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
                initialAccounts.add(account);
            } while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return initialAccounts;
    }

    //Adding a new account to the database
    public void addAccount(Account account) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Account_No", account.getAccountNo());
        values.put("Bank", account.getBankName());
        values.put("Account_Holder", account.getAccountHolderName());
        values.put("Balance", account.getBalance());

        database.insert("Accounts", null, values);
        database.close();
    }

    //Removing an account from the database
    public void removeAccount(String accountNo) {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteAccount = "DELETE FROM Accounts WHERE Account_No = '" + accountNo + "'";
        database.execSQL(deleteAccount);
        database.close();
    }

    //Updating the balance column of an account entry in the database
    public void updateAccountBalance(Account account) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Balance", account.getBalance());
        database.update("Accounts",  values, "Account_No = '"+ account.getAccountNo() + "'", null);
        database.close();
    }

    //Returning all transactions in the database as a list
    public List<Transaction> getAllTransactions() throws ParseException {
        List<Transaction> initialTransactions = new ArrayList<>();
        String getAllTransactions = "SELECT * FROM Transactions";
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery(getAllTransactions, null);

        if(cursor.moveToFirst()) {
            do {
                String strDate = cursor.getString(1);
                Date date=new SimpleDateFormat("dd-MM-yyyy").parse(strDate);
                Transaction transaction = new Transaction(date, cursor.getString(2), ExpenseType.valueOf(cursor.getString(3)), cursor.getDouble(4));
                initialTransactions.add(transaction);
            } while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return initialTransactions;
    }

    //Adding a transaction to the database
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String strDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        values.put("Date", strDate);
        values.put("Account_No", accountNo);
        values.put("Type", String.valueOf(expenseType));
        values.put("Amount", amount);

        database.insert("Transactions", null, values);
        database.close();
    }
}
