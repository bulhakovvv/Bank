package com.example.bank;

import java.util.List;

public class Credit {
    private String name;
    private double initialAmount;
    private double remainingAmount;
    private List<Payment> schedule;

    public Credit(String name, double initialAmount, double remainingAmount, List<Payment> schedule) {
        this.name = name;
        this.initialAmount = initialAmount;
        this.remainingAmount = remainingAmount;
        this.schedule = schedule;
    }

    public String getName() { return name; }
    public double getInitialAmount() { return initialAmount; }
    public double getRemainingAmount() { return remainingAmount; }
    public List<Payment> getSchedule() { return schedule; }
}
