class BankAccount {
    private String user;
    private double balance;

    private static String formatAmount(double amount) {
        if (amount == Math.rint(amount)) {
            return String.valueOf((long) amount);
        }
        return String.valueOf(amount);
    }

    public BankAccount(String user, double balance) {
        this(user, balance, true);
    }

    public BankAccount(String user, double balance, boolean verbose) {
        if (verbose) {
            System.out.println("Creating account for user " + user + " with $" + formatAmount(balance) + " amount.");
        }
        this.user = user;
        this.balance = balance;
    }

    public void deposit(double amount) {
        System.out.println("Adding $" + formatAmount(amount) + " to your account.");
        this.balance += amount;
    }

    public void withdraw(double amount) {
        System.out.println("Trying to withdraw $" + formatAmount(amount) + " amount.");
        if (amount <= this.balance) {
            System.out.println("Valid withdrawal amount!");
            this.balance -= amount;
        } else {
            System.out.println("Insufficient funds");
        }
    }

    public void displayBalance() {
        System.out.println(this.user + "'s balance: $" + formatAmount(this.balance));
    }
}

public class JavaMemoryDemo {
    private static void printMemoryUsage(String label) {
        Runtime runtime = Runtime.getRuntime();
        long usedBytes = runtime.totalMemory() - runtime.freeMemory();
        long usedKb = usedBytes / 1024;
        System.out.println(label + " used memory: " + usedKb + " KB");
    }

    public static void main(String[] args) throws InterruptedException {
        printMemoryUsage("Before allocation");

        BankAccount account = new BankAccount("Furqaan", 1000.0);
        account.deposit(500.0);
        account.withdraw(300.0);
        account.displayBalance();

        BankAccount[] accounts = new BankAccount[100000];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new BankAccount("User" + i, i, false);
        }
        printMemoryUsage("After creating 100000 account objects");

        accounts = null;
        account = null;
        System.gc();
        Thread.sleep(1000);
        printMemoryUsage("After clearing references and requesting garbage collection");
    }
}
