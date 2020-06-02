import java.io.*;
import java.util.Date;

/**
 * @author Pengxinjie
 * @DATE 2020/5/29 - 14:52
 */
public class ATM {
    private Account acc;
    private File dataFile;
    private String filePath = "data.txt";

    public ATM() throws IOException {
        this.acc = new Account();
        try {
            this.dataFile = new File(this.filePath);
            if (!this.dataFile.exists()) {
                this.dataFile.createNewFile();
            }
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
            if (AccountName.equals("") || password.equals("")){
                System.err.println("账号或密码输入错误!请重新输入！");
            }
            else if (!this.acc.isValid(Long.parseLong(AccountName.trim()), password.trim())) {
                System.err.println("账号或密码输入错误!请重新输入！");
            }else{
                break;
            }
        }
        int c = 100;
        while (c != 0) {
            System.out.println("                ----------------------------------------");
            System.out.println("                |1. 查询余额                 2. ATM取款 |\n                " +
                    "|3. ATM存款                  4. 修改密码|\n                |5. 查询记录" +
                    "                 0. 退出程序|");
            System.out.print("                ----------------------------------------    请输入：");
            String str = br.readLine();
            try {
                c = Integer.parseInt(str.trim());
            } catch (NumberFormatException nfe) {
                System.err.println("无效的选择！");
                System.out.println();
                continue;
            }
            switch (c) {
                case 0:
                    System.out.println("祝您愉快!");
                    break;
                case 1:
                    System.out.println("余额： " + this.acc.getBalance());
                    break;
                case 2:
                    System.out.print("取款数额：");
                    String Num = br.readLine();
                    try {
                        long Amm = Long.parseLong(Num.trim());
                        if(this.acc.withdraw(Amm)) {
                            FileWriter fw = new FileWriter("data.txt",true);
                            //更新记录文件
                            fw.write("账号: " + this.acc.getAccNo() + " " + new Date() + "   取款：" + Amm + "元，" + "账户余额：" + this.acc.getBalance() + "元\n");
                            fw.close();
                        }
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
                        if(this.acc.deposit(Amm)) {
                            FileWriter fw = new FileWriter("data.txt",true);
                            //更新记录文件
                            fw.write("账号: " + this.acc.getAccNo() + " " + new Date() + "   存款：" + Amm + "元，" + "账户余额：" + this.acc.getBalance() + "元\n");
                            fw.close();
                        }
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
                case 5:
                    FileReader fileReader = new FileReader("data.txt");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    int len;
                    char[] c1 = new char[10];
                    System.out.println("当前账户的存取款记录如下：");
                    while ((len = bufferedReader.read(c1)) != -1){
                        System.out.print(new String(c1,0,len));
                    }
                    bufferedReader.close();
                    fileReader.close();
                    break;
                default:
                    System.err.println("无效的输入！");
                    System.out.println();
                    break;
            }
        }
        //更新账户文件
        FileWriter writer = new FileWriter("account.txt");
        writer.write(this.acc.toString());
        writer.close();
    }
}


class Account {
    private long accNo;
    private String pass;
    private long balance;

    public long getAccNo() {
        return accNo;
    }

    public Account() throws IOException {
        File file = new File("account.txt");
        try {
            if (!file.exists()){
                file.createNewFile();
                FileWriter fw = new FileWriter("account.txt",true);
                fw.write("accNo   : 123456\n");
                fw.write("pass    : 123456\n");
                fw.write("balance : 10000");
                fw.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        FileReader fr = new FileReader("account.txt");
        BufferedReader br = new BufferedReader(fr);

        String acc = br.readLine().replace(" ","");
        acc = acc.substring(6);
        this.accNo = Integer.parseInt(acc);

        String pa = br.readLine().replace(" ","");
        pa = pa.substring(5);
        this.pass = pa;

        String ba = br.readLine();
        ba = ba.substring(10);
        this.balance = Integer.parseInt(ba);

        br.close();
        fr.close();
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
        else if (password.length() < 6) {
            System.err.println("新密码不能小于6位数！");
            System.out.println();
            return;
        }
        else if (password.length() > 10) {
            System.err.println("新密码不能大于10位数！");
            System.out.println();
            return;
        }
        else if (password.indexOf(" ") != -1){
            System.err.println("密码中不允许使用空格！");
            System.out.println();
            return;
        }
        else if (password.equals(this.pass)) {
            System.err.println("新密码不能与原密码重复！");
            System.out.println();
            return;
        }
        else if (!password.equals(conformPass)){
            System.err.println("新密码两次输入结果不同！");
            System.out.println();
            return;
        }
        char[] arr = password.toCharArray();
        int i;
        for (i = 1; i < arr.length; i++) {
            if (arr[0] != arr[i]){
                break;
            }
        }
        if (i == arr.length){
            System.err.println("密码的每一位不能都相同！");
            System.out.println();
            return;
        }
        System.out.println("密码修改成功！");
        this.pass = password;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        return this.balance;
    }

    public boolean withdraw(long amount) {
        if (amount > 5000 || amount < 0) {
            System.err.println("取款数额范围: $0-$5000");
            System.out.println();
            return false;
        }
        if ((amount % 100) != 0) {
            System.err.println("此数额不是100的整数倍数！");
            System.out.println();
            return false;
        }
        long newBalance = this.balance - amount;
        if (newBalance < 0) {
            System.err.println("账户余额不足！");
            System.out.println();
            return false;
        }
        this.balance = newBalance;
        return true;
    }

    public boolean deposit(long amount) {
        if (amount < 0) {
            System.err.println("数额不能为负数！");
            System.out.println();
            return false;
        }
        if ((amount % 100) != 0) {
            System.err.println("此数额不是100的整数倍数！");
            System.out.println();
            return false;
        }
        this.balance += amount;
        return true;
    }

    @Override
    public String toString() {
        return ( "accNo   : "+this.accNo+"\npass    : "+this.pass+"\nbalance : "+this.balance);
    }
}
