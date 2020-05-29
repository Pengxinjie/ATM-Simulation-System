import java.io.*;

/**
 * @author Pengxinjie
 * @DATE 2020/5/29 - 14:52
 */
public class ATM {

    private Account acc;

    private File dataFile;
    private FileWriter fw;
    private BufferedWriter bw;

    private String filePath = "./data.txt";

    public ATM() {
        this.acc = new Account();
        try {
            this.dataFile = new File(this.filePath);
            if (!this.dataFile.exists()) {
                this.dataFile.createNewFile();
            }
            this.fw = new FileWriter(this.filePath);
            this.bw = new BufferedWriter(this.fw);
        } catch (IOException io) {
            System.err.println("Cannot open file");
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new ATM().interact();
    }

    public void interact() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("账号:");
        String Account = br.readLine();
        String password;
        while (true) {
            System.out.print("密码：");
            password = br.readLine();
            if (!this.acc.isValid(Long.parseLong(Account.trim()), password.trim())) {
                System.err.println("密码错误!请重新输入！");
                System.out.println();
            }else{
                break;
            }
        }
        int c = 100;
        while (c != 0) {
            System.out.print("                1. 查询余额.");
            System.out.println("                2. ATM取款");
            System.out.print("                3. ATM存款");
            System.out.println("                  4. 修改密码");
            System.out.print("                5. 保存数据");
            System.out.println("                 0. 退出程序");
            String str = br.readLine();
            try {
                c = Integer.parseInt(str.trim());
            } catch (NumberFormatException nfe) {
                System.err.println("无效的选择！");
                continue;
            }
            switch (c) {
                case 0:
                    System.out.println("祝您愉快");
                    break;
                case 1:
                    System.out.println("余额： " + this.acc.balanceInquery());
                    break;
                case 2:
                    System.out.print("取款数额：");
                    String temp3 = br.readLine();
                    try {
                        long ammount = Long.parseLong(password.trim());
                        this.acc.withdraw(ammount);
                        break;
                    } catch (NumberFormatException nfe) {
                        System.err.println("无效的数字！");
                        continue;
                    }
                case 3:
                    System.out.print("存款数额：");
                    String temp4 = br.readLine();
                    try {
                        long ammount = Long.parseLong(password.trim());
                        this.acc.deposit(ammount);
                        break;
                    } catch (NumberFormatException nfe) {
                        System.err.print("无效的数字！");
                        continue;
                    }
                case 4:
                    System.out.print("旧密码：");
                    String temp5 = br.readLine();
                    System.out.print("新密码：");
                    String temp6 = br.readLine();
                    this.acc.changePassword(Account, password);
                    break;
                case 5:
                    this.bw.write(this.acc.toString());
                    break;
                default:
                    break;
            }
        }
    }

}

class Account {

    private long accNo = 123456;
    private String pass = "123456";
    private long balance = 10000;

    public Account() {

    }

    public boolean isValid(long accNo, String pass) {
        return (this.accNo == accNo) && (pass.equals(this.pass));
    }

    public void changePassword(String oldPass, String password) {
        if (!oldPass.equals(this.pass)) {
            System.err.println("密码错误！");
            return;
        }
        if (password.length() < 6) {
            System.err.println("密码小于6位数！");
            return;
        }
        if (password.equals(this.pass)) {
            System.err.println("新密码不能与原密码重复！");
            return;
        }
        this.pass = password;
    }

    public long balanceInquery() {
        return this.balance;
    }

    public void withdraw(long amount) {
        if (amount > 5000 || amount < 0) {
            System.err.println("数额范围: $0-$5000");
            return;
        }
        if ((amount % 100) != 0) {
            System.err.println("这个数额不是100的整数倍数！");
            return;
        }
        long newBalance = this.balance - amount;
        if (newBalance < 0) {
            System.err.println("账户余额不足！");
            return;
        }
        this.balance = newBalance;
    }

    public void deposit(long amount) {
        if (amount < 0) {
            System.err.println("数额不能为负数！");
            return;
        }
        this.balance += amount;
    }

    @Override
    public String toString() {
        return ("Account #: " + this.accNo + "\n" + "Password: " + this.pass + "\n" + "Balance: " + this.balance);
    }
}
