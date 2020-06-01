import java.io.*;
import java.util.Date;

/**
 * @author Pengxinjie
 * @DATE 2020/5/29 - 14:52
 */
public class ATM {
    private Account acc;
    private File dataFile;
    private FileWriter fw;
    private String filePath = "data.txt";

    public ATM() {
        this.acc = new Account();
        try {
            this.dataFile = new File(this.filePath);
            if (!this.dataFile.exists()) {
                this.dataFile.createNewFile();
            }
            this.fw = new FileWriter(this.filePath,true);
        } catch (IOException io) {
            System.err.println("文件无法打开！");
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
        while (true) {
            System.out.print("\n账号:");
            String AccountName = br.readLine();
            String password;
            System.out.print("密码：");
            password = br.readLine();
            if (!this.acc.isValid(Long.parseLong(AccountName.trim()), password.trim())) {
                System.err.println("账号或密码输入错误!请重新输入！");
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
            System.out.print("                0. 退出程序\n");
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
                    String Num = br.readLine();
                    try {
                        long Amm = Long.parseLong(Num.trim());
                        this.acc.withdraw(Amm);
                        fw.write("账号: " + this.acc.getAccNo() +" "+new Date()+"   取款："+Amm+"元，"+"账户余额："+this.acc.balanceInquery()+"元\n");
                        break;
                    } catch (NumberFormatException nfe) {
                        System.err.println("无效的数字！");
                        continue;
                    }
                case 3:
                    System.out.print("存款数额：");
                    Num = br.readLine();
                    try {
                        long Amm = Long.parseLong(Num.trim());
                        this.acc.deposit(Amm);
                        fw.write("账号: " + this.acc.getAccNo()+" " +new Date()+"   存款："+Amm+"元，"+"账户余额："+this.acc.balanceInquery()+"元\n");
                        break;
                    } catch (NumberFormatException nfe) {
                        System.err.print("无效的数字！\n");
                        continue;
                    }
                case 4:
                    System.out.print("密码：");
                    String oldPassword = br.readLine();
                    System.out.print("新密码：");
                    String newPassword = br.readLine();
                    System.out.print("请再输入一遍：");
                    String confirmPass = br.readLine();
                    this.acc.changePassword(oldPassword,newPassword,confirmPass);
                    break;
                default:
                    System.out.println("无效的输入！");
                    break;
            }
        }
        fw.close();
    }
}


class Account {
    private long accNo = 123456;
    private String pass = "123456";
    private long balance = 10000;

    public long getAccNo() {
        return accNo;
    }

    public Account() {

    }

    public boolean isValid(long accNo, String pass) {
        return (this.accNo == accNo) && (pass.equals(this.pass));
    }

    public void changePassword(String oldPass, String password,String conformPass) {
        if (!oldPass.equals(this.pass)) {
            System.err.println("密码错误！");
            System.out.println();
            return;
        }
        if (password.length() < 6) {
            System.err.println("新密码不能小于6位数！");
            System.out.println();
            return;
        }
        if (password.equals(this.pass)) {
            System.err.println("新密码不能与原密码重复！");
            System.out.println();
            return;
        }
        if (!password.equals(conformPass)){
            System.err.println("新密码两次输入结果不同！");
            System.out.println();
            return;
        }
        System.out.println("密码修改成功！");
        this.pass = password;
    }

    public long balanceInquery() {
        return this.balance;
    }

    public void withdraw(long amount) {
        if (amount > 5000 || amount < 0) {
            System.err.println("取款数额范围: $0-$5000");
            System.out.println();
            return;
        }
        if ((amount % 100) != 0) {
            System.err.println("此数额不是100的整数倍数！");
            System.out.println();
            return;
        }
        long newBalance = this.balance - amount;
        if (newBalance < 0) {
            System.err.println("账户余额不足！");
            System.out.println();
            return;
        }
        this.balance = newBalance;
    }

    public void deposit(long amount) {
        if (amount < 0) {
            System.err.println("数额不能为负数！");
            System.out.println();
            return;
        }
        if ((amount % 100) != 0) {
            System.err.println("此数额不是100的整数倍数！");
            System.out.println();
            return;
        }
        this.balance += amount;
    }

    @Override
    public String toString() {
        return ( "账号: " + this.accNo + "     " + "密码: " + this.pass + "    " + "余额: " + this.balance+"\n");
    }
}
