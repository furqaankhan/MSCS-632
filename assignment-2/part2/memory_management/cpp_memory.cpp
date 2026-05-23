#include <iostream>
#include <string>
using namespace std;

class BankAccount {
private:
    string user;
    double balance;

public:
    BankAccount(string user, double balance, bool verbose = true) {
        if (verbose) {
            cout << "Creating account for user " << user << " with $" << balance << " amount." << endl;
        }
        this->user = user;
        this->balance = balance;
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
            cout << "Insufficient funds" << endl;
        }
    }

    void displayBalance() {
        cout << user << "'s balance: $" << balance << endl;
    }
};

int main() {
    BankAccount* account = new BankAccount("Furqaan", 1000.0);
    account->deposit(500.0);
    account->withdraw(300.0);
    account->displayBalance();

    delete account;
    account = nullptr;
    cout << "Single account memory was manually released with delete." << endl;

    const int count = 100000;
    BankAccount** accounts = new BankAccount*[count];
    for (int i = 0; i < count; i++) {
        accounts[i] = new BankAccount("User" + to_string(i), i, false);
    }
    cout << "Created " << count << " account objects with new." << endl;

    for (int i = 0; i < count; i++) {
        delete accounts[i];
    }
    delete[] accounts;
    accounts = nullptr;
    cout << "All dynamically allocated accounts were manually released." << endl;

    return 0;
}
