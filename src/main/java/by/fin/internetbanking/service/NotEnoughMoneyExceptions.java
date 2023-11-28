package by.fin.internetbanking.service;

public class NotEnoughMoneyExceptions extends Exception{
    public NotEnoughMoneyExceptions() {
    }

    public NotEnoughMoneyExceptions(String message) {
        super(message);
    }
}
