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
