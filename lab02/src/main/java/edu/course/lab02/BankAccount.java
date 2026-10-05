package edu.course.lab02;
//- приватное поле `int balance`;
//- конструктор, запрещающий отрицательный начальный баланс;
//- метод `deposit(int amount)`, принимающий только положительную сумму;

//- метод `withdraw(int amount)`, запрещающий неположительную сумму и превышение остатка;
//- метод `getBalance()` без setter.



public class BankAccount{
    private int balance;
    BankAccount(int balance){
        if (balance < 0){
            throw new IllegalArgumentException("Balance can not be lower then 0");

        }
        this.balance = balance;
    }

    public void deposit(int amount){
        if (amount <= 0 ){
            throw new IllegalArgumentException("Amount can not be lower then 0");
        }
        this.balance += amount;
    }

    public void withdraw(int amount){
        if (amount <= 0 || amount >this.balance){
            throw new IllegalArgumentException("Amount can not be higher then balance or lower than zero");
        } 
        this.balance -= amount;
    }


    public int getBalance(){
        return this.balance;
    }
}