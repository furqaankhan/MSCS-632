#include <iostream>
#include <string>
using namespace std;

class BankAccount {
private:
    string user;
    double balance;

public:
    BankAccount(string accountUser, double initialBalance) {
        cout << "Creating account for user " << accountUser << " with $" << initialBalance << " amount." << endl;
        user = accountUser;
        balance = initialBalance;
    }

    void deposit(double amount) {
        cout << "Adding $" << amount << " to your account." << endl;
        balance += amount;
    }

    void withdraw(double amount) {
        cout << "Trying to withdraw $" << amount << " amount." << endl;
        if (amount <= balance) {
            cout << "Valid withdrawal amount!" << endl;
            balance -= amount;
        } else {
            cout << "Insufficient funds." << endl;
        }
    }

    void displayBalance() {
        cout << user << "'s balance: $" << balance << "." << endl;
    }
};

int main() {
    BankAccount account("Furqaan", 1000);
    account.deposit(500);
    account.withdraw(300);
    account.displayBalance();

    return 0;
}
