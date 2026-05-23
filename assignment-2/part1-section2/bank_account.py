class BankAccount:
    def __init__(self, user, balance):
        print(f"Creating account for user {user} with ${balance} amount.")
        self.user = user
        self._balance = balance

    def deposit(self, amount):
        print(f"Adding ${amount} to your account.")
        self._balance += amount

    def withdraw(self, amount):
        print(f"Trying to withdraw ${amount} amount.")
        if amount <= self._balance:
            print("Valid withdrawal amount!")
            self._balance -= amount
        else:
            print("Insufficient funds.")

    def display_balance(self):
        print(f"{self.user}'s balance: ${self._balance}.")


account = BankAccount("Furqaan", 1000)
account.deposit(500)
account.withdraw(300)
account.display_balance()
