package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import java.text.ParseException;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;


public class PersistentExpenseManager extends ExpenseManager{
    Context context;

    //Constructor
    public PersistentExpenseManager(Context context) throws ExpenseManagerException, ParseException {
        this.context = context;
        setup();
    }

    //Sets the data access objects
    @Override
    public void setup() throws ParseException {
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(context);
        setTransactionsDAO(persistentTransactionDAO);
        AccountDAO persistentAccountDAO = new PersistentAccountDAO(context);
        setAccountsDAO(persistentAccountDAO);
    }
}
