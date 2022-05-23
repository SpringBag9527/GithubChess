package view;

import model.ChessColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Pattern;

public class User_Login extends JFrame{
    private JButton submit;
    private JButton reset;
    private JButton exit;
    private JLabel nameLab;
    private JLabel passLab;
    private JLabel infoLab;
    private JTextField nameText;
    private JPasswordField passText;
	private String Title;
	private static ChessPlayer[]  player=new ChessPlayer[2];
	LoginCheck login;
	public  BgSound BackGroundSound;
    private ImageIcon icon=new ImageIcon(this.getClass().getResource("/images/icon.png"));

    public User_Login(int width,int height,String title) {
        setSize(width,height);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);

        setTitle(title);
        this.Title=title;   //记录Title，从而知道当前所在步骤
        Font f=new Font("宋体", Font.BOLD, 20);
        UIManager.put("Label.font",f);
        UIManager.put("Button.font",f);
        UIManager.put("TextField.font",f);
        UIManager.put("PasswordField.font",f);
        UIManager.put("List.font",f);

        nameLab=new JLabel("选手名：");
        passLab=new JLabel("密码：");
        infoLab=new JLabel("");
        submit=new JButton("登录");
        reset=new JButton("重置");
        exit=new JButton("退出");
        nameText=new JTextField();
        passText=new JPasswordField();
        BackGroundSound=new BgSound();

        //登录按钮处理
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BackGroundSound.play("button");
                String name = nameText.getText();
                if(name.isEmpty()){
                    JOptionPane.showMessageDialog(null,"选手名不能为空！",
                            "Dear Sir",JOptionPane.INFORMATION_MESSAGE,icon);
                    return;
                }
                //控制选手名不超过6位，便于排行榜控制显示
                if(name.length()>6){
                    JOptionPane.showMessageDialog(null,"选手名请不要超过6位！",
                            "Dear Sir",JOptionPane.INFORMATION_MESSAGE,icon);
                    return;
                }
                String regEx = "[a-zA-Z0-9]*";
                //name中不只有字母和数字
                if(!(Pattern.compile(regEx).matcher(name).matches())){
                    JOptionPane.showMessageDialog(null,"选手名中不能有非法字符，只能用字母和数字!",
                            "Dear Sir",JOptionPane.INFORMATION_MESSAGE,icon);
                    return;
                }

                String password = new String(passText.getPassword());
                if(password.isEmpty()){
                    JOptionPane.showMessageDialog(null,"密码不能为空！",
                            "Dear Sir",JOptionPane.INFORMATION_MESSAGE,icon);
                    return;
                }
                //控制密码不超过6位，避免
                if(password.length()>6){
                    JOptionPane.showMessageDialog(null,"密码长度请不要超过6位！",
                            "Dear Sir",JOptionPane.INFORMATION_MESSAGE,icon);
                    return;
                }
                if(!(Pattern.compile(regEx).matcher(password).matches())){
                    JOptionPane.showMessageDialog(null,"密码中有非法字符，只能用字母和数字!",
                            "Dear Sir",JOptionPane.INFORMATION_MESSAGE,icon);
                    return;
                }
                login = new LoginCheck(name, password);
                try{
                    //判断用户名密码是否一致 0一致 -1不一致  1新用户
                    int ret=login.validate();
                    //验证不一致
                    if(ret==-1){
                        infoLab.setText("选手名或密码错误，请重新输入");
                        nameText.setText(null);
                        passText.setText(null);
                        return;
                    }
                    //新用户
                    else if(ret==1){
                        int n=JOptionPane.showConfirmDialog(null,"新选手，是否需要注册？","Dear Sir",
                                JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,icon);
                        if(n!=0){
                            return;
                        }
                        else{
                            login.addUser();
                        }
                    }
                    //infoLab.setText("登录成功,欢迎光临");
                    //登录成功后，做的下一步操作
                    Do_Next();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BackGroundSound.play("button");
                if (e.getSource() == reset) {
                    nameText.setText(null);
                    passText.setText(null);
                    infoLab.setText("选手登录系统");
                }
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BackGroundSound.play("button");
                System.exit(0);
            }
        });

        nameLab.setBounds(5, 5, 90, 34);
        passLab.setBounds(5, 55, 90, 34);
        infoLab.setBounds(5, 110, 220, 35);
        nameText.setBounds(95,5,120,35);
        passText.setBounds(95,55,120,35);
        submit.setBounds(220,5,80,34);
        reset.setBounds(220,55,80,34);
        exit.setBounds(220,105,80,34);
        add(nameLab);
        add(passLab);
        add(infoLab);
        add(nameText);
        add(passText);
        add(submit);
        add(reset);
        add(exit);
    }
    //确保点击右上角关闭钮不会被关闭
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        } else {
            super.processWindowEvent(e);
        }
    }
    //登录校验成功后，做的下一步操作
    public void Do_Next() throws FileNotFoundException {
        //当前是黑方登录,下一步进入棋局
        if(Title.equals("黑方选手登录：")){
            player[1]=login.getMyPlayer();
            player[1].setChessColor(ChessColor.BLACK);
            //如果2个用户名一致
            if(player[1].getName().equals(player[0].getName())){
                JOptionPane.showMessageDialog(null,"白、黑双方是同一人？你不会是在耍我吧？",
                        "Dear Sir",JOptionPane.INFORMATION_MESSAGE,icon);
                nameText.setText(null);
                passText.setText(null);
                return;
            }
            dispose();
            ChessGameFrame mainFrame = new ChessGameFrame(player,1000, 720);
            mainFrame.addImageByRepaint();
        }
        //当前应该是白方登录，下一步 黑方登录
        else{
            player[0]=login.getMyPlayer();
            player[0].setChessColor(ChessColor.WHITE);
            dispose();
            User_Login logFrame=new User_Login(350,200,"黑方选手登录：");
            logFrame.setVisible(true);
        }
    }
}
//登录检测用户名和密码
class LoginCheck{
    private String name;
    private String password;
    ChessPlayer  MyPlayer;
    public LoginCheck(String name,String password){
        this.name=name;
        this.password=password;
    }
    public ChessPlayer getMyPlayer(){
        return(this.MyPlayer);
    }
    //判断用户名密码是否一致 0一致 -1不一致  1新用户
    public int validate() throws IOException {
        MyPlayer=new ChessPlayer(this.name);
        IsValidate isv=MyPlayer.validate(this.password);
        int ret=isv.getResult();
        MyPlayer=isv.getPlayer();

        return(ret);
    }
    //用户信息   用户名=用户密码,比赛场次,胜利场次，平局场次
    public void addUser() throws IOException {
        MyPlayer=new ChessPlayer(this.name,0,0,0);
        MyPlayer.UpdatePlayer(this.password);
    }
    //从存储的配置串中，获得Player信息
    public ChessPlayer GetPlayerInfo(String name,String[] sinfo){
        int fGms=Integer.parseInt(sinfo[1]);
        int wGms=Integer.parseInt(sinfo[2]);
        int tGms=Integer.parseInt(sinfo[3]);
        ChessPlayer player=new ChessPlayer(name,fGms,wGms,tGms);
        return(player);
    }
}

class ChessPlayer{
    private String Name;
    public int    FinishGames;     //完成比赛数
    public int    WinGames;        //获胜数
    public int    TieGames;        //平局数
    public ChessColor chessColor;  //本棋局属于哪一方

    public ChessPlayer(String name){
        this.Name=name;
    }
    public ChessPlayer(String name,int fGms,int wGms,int tGms){
        this.Name=name;
        this.FinishGames=fGms;
        this.WinGames=wGms;
        this.TieGames=tGms;
    }
    public void setChessColor(ChessColor cscolor){
        this.chessColor=cscolor;
    }
    public String getName(){return Name;};
	//判断用户信息是否合法(用户名密码是否一致) 0一致 -1不一致  1新用户
    public IsValidate validate(String password) throws IOException {
        IsValidate isv;
        //打开并获取配置文件内容
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("user.properties");
        prop.load(fis);
        fis.close();

        String IniTxt=prop.getProperty(Name);
		//没找到用户
        if(IniTxt==null) {
            isv=new IsValidate(null,1);
        }
        else {
            String[] ss=IniTxt.split(",");
			//密码一致
            if(password.equals(ss[0])) {  //老用户获取成功 生产MyPlayer
                ChessPlayer MyPlayer=GetPlayerInfo(Name,ss);
                isv=new IsValidate(MyPlayer,0);
            }
            else {	//密码不一致
                isv=new IsValidate(null,-1);
            }
        }
        return(isv);
    }
    //从存储的配置串中，获得指定Player信息
    public ChessPlayer GetPlayerInfo(String name,String[] sinfo){
        int fGms=Integer.parseInt(sinfo[1]);
        int wGms=Integer.parseInt(sinfo[2]);
        int tGms=Integer.parseInt(sinfo[3]);
        ChessPlayer player=new ChessPlayer(name,fGms,wGms,tGms);
        return(player);
    }
	//带密码更新本用户信息,一般用于新用户
    public void UpdatePlayer(String password) throws IOException {
        //打开并获取配置文件内容
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("user.properties");
        prop.load(fis);
        prop.remove("--");
        fis.close();
        String sval=password+","+this.FinishGames+","+this.WinGames+","+this.TieGames;
        //添加，生产MyPlayer
        //MyPlayer=new ChessPlayer(this.name,0,0,0);
        prop.setProperty(this.Name,sval);
        try {
            PrintStream fW = new PrintStream(new File("user.properties"));
            prop.list(fW );
            fW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	//无密码更新本用户信息，一般用户后期用户属性更新，此时没有密码，只能
    public void UpdatePlayer() throws IOException {
        //打开并获取配置文件内容
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("user.properties");
        prop.load(fis);
        prop.remove("--");
        fis.close();
        String s=prop.getProperty(this.Name);
        String[] ss=s.split(",");
        String sval=ss[0]+","+this.FinishGames+","+this.WinGames+","+this.TieGames;
        //添加，生产MyPlayer
        //MyPlayer=new ChessPlayer(this.name,0,0,0);
        prop.setProperty(this.Name,sval);
        try {
            PrintStream fW = new PrintStream(new File("user.properties"));
            prop.list(fW );
            fW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //从配置中获取所有选手信息
    public ArrayList<ChessPlayer> GetAllPlayer() throws IOException {
        ArrayList<ChessPlayer> plist=new ArrayList<ChessPlayer>();

        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("user.properties");
        prop.load(fis);
        prop.remove("--");
        fis.close();

        Enumeration enum1 = prop.propertyNames();//枚举所有项目名

        while(enum1.hasMoreElements()) {
            String strKey = (String) enum1.nextElement();
            String strValue = prop.getProperty(strKey);
            String[] ss=strValue.split(",");
            ChessPlayer player=GetPlayerInfo(strKey,ss);
            plist.add(player);
        }
        return(plist);
    }

    public int getFinishGames(){return (FinishGames);}
    public int getWinGames(){return (WinGames);}
    public float getWinRate(){
        if(FinishGames==0)
            return((float) 0);
        else
            return((float)(WinGames*1.0/FinishGames) );
    }
}

//用户密码校验
class IsValidate{
    private ChessPlayer ThePlayer;
	//校验结果 用户名密码是否一致 0一致 -1不一致  1新用户
    private int Result=-100;

    public IsValidate(ChessPlayer player,int ret){
        this.ThePlayer=player;
        this.Result=ret;
    }
    public int getResult(){return Result;}
    public ChessPlayer getPlayer(){return ThePlayer;}
}
