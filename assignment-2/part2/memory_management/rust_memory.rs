struct BankAccount {
    user: String,
    balance: f64,
}

impl BankAccount {
    fn new(user: &str, balance: f64) -> BankAccount {
        println!("Creating account for user {} with ${} amount.", user, balance);
        BankAccount {
            user: user.to_string(),
            balance,
        }
    }

    fn deposit(&mut self, amount: f64) {
        println!("Adding ${} to your account.", amount);
        self.balance += amount;
    }

    fn withdraw(&mut self, amount: f64) {
        println!("Trying to withdraw ${} amount.", amount);
        if amount <= self.balance {
            println!("Valid withdrawal amount!");
            self.balance -= amount;
        } else {
            println!("Insufficient funds");
        }
    }

    fn display_balance(&self) {
        println!("{}'s balance: ${}", self.user, self.balance);
    }
}

fn inspect_account(account: &BankAccount) {
    println!("Borrowing account data without taking ownership.");
    account.display_balance();
}

fn main() {
    let mut account = Box::new(BankAccount::new("Furqaan", 1000.0));
    account.deposit(500.0);
    account.withdraw(300.0);

    inspect_account(&account);
    println!("Ownership returned to main after the borrow ends.");

    drop(account);
    println!("Account memory was released when ownership was dropped.");
}
