import java.io.*;

/**
 * @author Pengxinjie
 * @DATE 2020/6/4 - 9:46
 */

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
