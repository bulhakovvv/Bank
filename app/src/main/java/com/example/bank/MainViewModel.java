package com.example.bank;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Transaction>> transactionsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Credit>> creditsLiveData = new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
    }

    public LiveData<User> getUser() { return userLiveData; }
    public LiveData<List<Transaction>> getTransactions() { return transactionsLiveData; }
    public LiveData<List<Credit>> getCredits() { return creditsLiveData; }

    // Helper method to read a file from internal storage and return content as String
    private String readFile(String filename) throws IOException {
        InputStream is = getApplication().openFileInput(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            builder.append(line);
        }
        br.close();
        is.close();
        return builder.toString();
    }

    public void loadUserData() {
        try {
            String content = readFile("user.json");
            JSONObject obj = new JSONObject(content);
            // Parse user data
            String username = obj.getString("username");
            String password = obj.getString("password");
            String name = obj.getString("name");
            String phone = obj.getString("phone");
            String address = obj.getString("address");
            double balance = obj.getDouble("balance");
            User user = new User(username, password, name, phone, address, balance);
            userLiveData.setValue(user);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadTransactionsData() {
        try {
            String content = readFile("transactions.json");
            JSONObject obj = new JSONObject(content);
            JSONArray transArray = obj.getJSONArray("transactions");
            List<Transaction> list = new ArrayList<>();
            for (int i = 0; i < transArray.length(); i++) {
                JSONObject t = transArray.getJSONObject(i);
                String date = t.getString("date");
                String description = t.getString("description");
                double amount = t.getDouble("amount");
                list.add(new Transaction(date, description, amount));
            }
            // Sort transactions by date descending (newest first)
            Collections.sort(list, (t1, t2) -> t2.getDate().compareTo(t1.getDate()));
            transactionsLiveData.setValue(list);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadCreditsData() {
        try {
            String content = readFile("credits.json");
            JSONObject obj = new JSONObject(content);
            JSONArray creditsArray = obj.getJSONArray("credits");
            List<Credit> creditsList = new ArrayList<>();
            for (int i = 0; i < creditsArray.length(); i++) {
                JSONObject c = creditsArray.getJSONObject(i);
                String name = c.getString("name");
                double initial = c.getDouble("initial");
                double remaining = c.getDouble("remaining");
                JSONArray scheduleArr = c.getJSONArray("schedule");
                List<Payment> schedule = new ArrayList<>();
                for (int j = 0; j < scheduleArr.length(); j++) {
                    JSONObject p = scheduleArr.getJSONObject(j);
                    String payDate = p.getString("date");
                    double payAmount = p.getDouble("amount");
                    schedule.add(new Payment(payDate, payAmount));
                }
                creditsList.add(new Credit(name, initial, remaining, schedule));
            }
            creditsLiveData.setValue(creditsList);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    // Method to perform a transfer and update data
    public boolean makeTransfer(String recipient, double amount) {
        User user = userLiveData.getValue();
        if (user == null) return false;
        // Check balance
        if (amount > user.getBalance()) {
            return false;
        }
        // Deduct balance
        double newBalance = user.getBalance() - amount;
        user.setBalance(newBalance);
        userLiveData.setValue(user);
        // Create new transaction
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String description = "Transfer to " + recipient;
        Transaction newTrans = new Transaction(currentDate, description, amount);
        // Update transactions list
        List<Transaction> list = transactionsLiveData.getValue();
        if (list == null) list = new ArrayList<>();
        list.add(0, newTrans); // add latest at top
        transactionsLiveData.setValue(list);
        // Save updated user and transactions to JSON
        saveUserData();
        saveTransactionsData();
        return true;
    }

    public void updateUserProfile(String name, String phone, String address) {
        User user = userLiveData.getValue();
        if (user == null) return;
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);
        userLiveData.setValue(user);
        // Save to JSON file
        saveUserData();
    }

    private void saveUserData() {
        User user = userLiveData.getValue();
        if (user == null) return;
        try {
            JSONObject obj = new JSONObject();
            obj.put("username", user.getUsername());
            obj.put("password", user.getPassword());
            obj.put("name", user.getName());
            obj.put("phone", user.getPhone());
            obj.put("address", user.getAddress());
            obj.put("balance", user.getBalance());
            String jsonString = obj.toString();
            // Write to internal file
            getApplication().openFileOutput("user.json", Application.MODE_PRIVATE).write(jsonString.getBytes());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveTransactionsData() {
        List<Transaction> list = transactionsLiveData.getValue();
        if (list == null) return;
        try {
            JSONObject root = new JSONObject();
            JSONArray transArray = new JSONArray();
            for (Transaction t : list) {
                JSONObject obj = new JSONObject();
                obj.put("date", t.getDate());
                obj.put("description", t.getDescription());
                obj.put("amount", t.getAmount());
                transArray.put(obj);
            }
            root.put("transactions", transArray);
            String jsonString = root.toString();
            getApplication().openFileOutput("transactions.json", Application.MODE_PRIVATE).write(jsonString.getBytes());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
