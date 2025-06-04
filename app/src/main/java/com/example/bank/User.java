package com.example.bank;

public class User {
    private String username;
    private String password;
    private String name;
    private String phone;
    private String address;
    private double balance;

    public User(String username, String password, String name, String phone, String address, double balance) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public double getBalance() { return balance; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setBalance(double balance) { this.balance = balance; }
}
