class BankAccount {
    #balance;

    constructor(user, balance) {
        console.log(`Creating account for user ${user} with $${balance} amount.`);
        this.user = user;
        this.#balance = balance;
    }

    deposit(amount) {
        console.log(`Adding $${amount} to your account.`);
        this.#balance += amount;
    }

    withdraw(amount) {
        console.log(`Trying to withdraw $${amount} amount.`);
        if (amount <= this.#balance) {
            console.log("Valid withdrawal amount!");
            this.#balance -= amount;
        } else {
            console.log("Insufficient funds.");
        }
    }

    displayBalance() {
        console.log(`${this.user}'s balance: $${this.#balance}.`);
    }
}

const account = new BankAccount("Furqaan", 1000);
account.deposit(500);
account.withdraw(300);
account.displayBalance();
