package view;

//import com.sun.org.apache.xpath.internal.operations.String;
import controller.GameController;
import model.ChessColor;
import model.ChessComponent;

import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;

public class ChessGameFrame extends JFrame {
    private int DownTimes=-5;
    private final int WIDTH;
    private final int HEIGHT;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;
    private Dimension frameSize;
    private ImageIcon BKimage;
	private ImagePanel imagePanel;
    private ImageIcon icon=new ImageIcon(this.getClass().getResource("/images/icon.png"));
    public Chessboard chessboard;
    private JLabel roundLabel;  //显示回合数的Label
    private JLabel sideLabel;   //显示当前走棋方的Label
    private JLabel timeLabel;   //显示倒计时90s
    public ChessPlayer[] player;
    private ChessGameFrame Me;
    private boolean TimeThreadExist=false;      //时间线程是否存在
    public  BgSound BackGroundSound;
    private JButton SoundBtn;
    public ProgressStep prgstep;
    public int SwapLoopTime=20;
    public boolean ReviewThreadExist=false;

    //窗体属性
    public ChessGameFrame(ChessPlayer[] pler,int width,int height){
        Me=this;
        this.player=pler;
        setTitle("Chess");
        frameSize=new Dimension(width,height);
        BKimage=new ImageIcon(this.getClass().getResource("/images/bg1.jpg"));
        this.WIDTH = width;
        this.HEIGHT = height;
        this.CHESSBOARD_SIZE = HEIGHT * 4 / 5;
        setSize(WIDTH,HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);//center to the window??
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//关闭程序
        BackGroundSound=new BgSound();
        setLayout(null);
    }
    //确保点击右上角关闭钮不会被关闭
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        } else {
            super.processWindowEvent(e);
        }
    }

    //创建生成界面所有要素（注意，该方法不要重复使用，会造成多次创建)
    public void addImageByRepaint() {
        //设置背景图片
        imagePanel= new ImagePanel(frameSize, BKimage.getImage());
        setContentPane(imagePanel);
        setLayout(null);
		
        //设置棋盘四周黑框
        addHLable(16,0);
        addVLable(0,16);
        addHLable(16,CHESSBOARD_SIZE+16);
        addVLable(16+CHESSBOARD_SIZE,16);
        addBlockLable(16);

        addChessboard();
        //设置右侧显示Lable
        addRightLabel();
        //设置右侧按钮
        addNewGameButton();
        addRegretButton();
        addLoadButton();
        addSaveButton();
        addReviewButton();
        addRankButton();
        addChBGButton();
        addChChessBoardButton();
        addChFormButton();
        addBGSoundButton();
        addExitButton();

        setVisible(true);
    }

    public void setReviewThreadExist(boolean b){this.ReviewThreadExist=b;};
    public void setDownTimes(){
        DownTimes = SwapLoopTime;
    }
    public void setDownTimes(int time){DownTimes=time;}

    public int getDownTimes(){
        return DownTimes;
    }

    public void addChessboard(){
        chessboard = new Chessboard(this,CHESSBOARD_SIZE,CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGHT/10,HEIGHT/10);
        add(chessboard);
    }

    //设置棋盘上下横向黑框 x、y为左上角坐标，用于定位其他坐标
    public void addHLable(int x,int y){
        JLabel laa=new JLabel("a",JLabel.CENTER);laa.setLocation(x,y);laa.setSize(CHESSBOARD_SIZE/8,16);
        laa.setFont(new Font("Rockwell", Font.BOLD, 18));laa.setOpaque(true);laa.setBackground(Color.BLACK);
        laa.setForeground(Color.WHITE);add(laa);
        JLabel lab=new JLabel("b",JLabel.CENTER);lab.setLocation(x+CHESSBOARD_SIZE/8,y);lab.setSize(CHESSBOARD_SIZE/8,16);
        lab.setFont(new Font("Rockwell", Font.BOLD, 18));lab.setOpaque(true);lab.setBackground(Color.BLACK);
        lab.setForeground(Color.WHITE);add(lab);
        JLabel lac=new JLabel("c",JLabel.CENTER);lac.setLocation(x+CHESSBOARD_SIZE/8*2,y);lac.setSize(CHESSBOARD_SIZE/8,16);
        lac.setFont(new Font("Rockwell", Font.BOLD, 18));lac.setOpaque(true);lac.setBackground(Color.BLACK);
        lac.setForeground(Color.WHITE);add(lac);
        JLabel lad=new JLabel("d",JLabel.CENTER);lad.setLocation(x+CHESSBOARD_SIZE/8*3,y);lad.setSize(CHESSBOARD_SIZE/8,16);
        lad.setFont(new Font("Rockwell", Font.BOLD, 18));lad.setOpaque(true);lad.setBackground(Color.BLACK);
        lad.setForeground(Color.WHITE);add(lad);
        JLabel lae=new JLabel("e",JLabel.CENTER);lae.setLocation(x+CHESSBOARD_SIZE/8*4,y);lae.setSize(CHESSBOARD_SIZE/8,16);
        lae.setFont(new Font("Rockwell", Font.BOLD, 18));lae.setOpaque(true);lae.setBackground(Color.BLACK);
        lae.setForeground(Color.WHITE);add(lae);
        JLabel laf=new JLabel("f",JLabel.CENTER);laf.setLocation(x+CHESSBOARD_SIZE/8*5,y);laf.setSize(CHESSBOARD_SIZE/8,16);
        laf.setFont(new Font("Rockwell", Font.BOLD, 18));laf.setOpaque(true);laf.setBackground(Color.BLACK);
        laf.setForeground(Color.WHITE);add(laf);
        JLabel lag=new JLabel("g",JLabel.CENTER);lag.setLocation(x+CHESSBOARD_SIZE/8*6,y);lag.setSize(CHESSBOARD_SIZE/8,16);
        lag.setFont(new Font("Rockwell", Font.BOLD, 18));lag.setOpaque(true);lag.setBackground(Color.BLACK);
        lag.setForeground(Color.WHITE);add(lag);
        JLabel lah=new JLabel("h",JLabel.CENTER);lah.setLocation(x+CHESSBOARD_SIZE/8*7,y);lah.setSize(CHESSBOARD_SIZE/8,16);
        lah.setFont(new Font("Rockwell", Font.BOLD, 18));lah.setOpaque(true);lah.setBackground(Color.BLACK);
        lah.setForeground(Color.WHITE);add(lah);
    }
    //设置棋盘上下纵向黑框 x、y为左上角坐标，用于定位其他坐标
    public void addVLable(int x,int y){
        JLabel laa=new JLabel("8",JLabel.CENTER);laa.setLocation(x,y);laa.setSize(16,CHESSBOARD_SIZE/8);
        laa.setFont(new Font("Rockwell", Font.BOLD, 18));laa.setOpaque(true);laa.setBackground(Color.BLACK);
        laa.setForeground(Color.WHITE);add(laa);
        JLabel lab=new JLabel("7",JLabel.CENTER);lab.setLocation(x,y+CHESSBOARD_SIZE/8);lab.setSize(16,CHESSBOARD_SIZE/8);
        lab.setFont(new Font("Rockwell", Font.BOLD, 18));lab.setOpaque(true);lab.setBackground(Color.BLACK);
        lab.setForeground(Color.WHITE);add(lab);
        JLabel lac=new JLabel("6",JLabel.CENTER);lac.setLocation(x,y+CHESSBOARD_SIZE/8*2);lac.setSize(16,CHESSBOARD_SIZE/8);
        lac.setFont(new Font("Rockwell", Font.BOLD, 18));lac.setOpaque(true);lac.setBackground(Color.BLACK);
        lac.setForeground(Color.WHITE);add(lac);
        JLabel lad=new JLabel("5",JLabel.CENTER);lad.setLocation(x,y+CHESSBOARD_SIZE/8*3);lad.setSize(16,CHESSBOARD_SIZE/8);
        lad.setFont(new Font("Rockwell", Font.BOLD, 18));lad.setOpaque(true);lad.setBackground(Color.BLACK);
        lad.setForeground(Color.WHITE);add(lad);
        JLabel lae=new JLabel("4",JLabel.CENTER);lae.setLocation(x,y+CHESSBOARD_SIZE/8*4);lae.setSize(16,CHESSBOARD_SIZE/8);
        lae.setFont(new Font("Rockwell", Font.BOLD, 18));lae.setOpaque(true);lae.setBackground(Color.BLACK);
        lae.setForeground(Color.WHITE);add(lae);
        JLabel laf=new JLabel("3",JLabel.CENTER);laf.setLocation(x,y+CHESSBOARD_SIZE/8*5);laf.setSize(16,CHESSBOARD_SIZE/8);
        laf.setFont(new Font("Rockwell", Font.BOLD, 18));laf.setOpaque(true);laf.setBackground(Color.BLACK);
        laf.setForeground(Color.WHITE);add(laf);
        JLabel lag=new JLabel("2",JLabel.CENTER);lag.setLocation(x,y+CHESSBOARD_SIZE/8*6);lag.setSize(16,CHESSBOARD_SIZE/8);
        lag.setFont(new Font("Rockwell", Font.BOLD, 18));lag.setOpaque(true);lag.setBackground(Color.BLACK);
        lag.setForeground(Color.WHITE);add(lag);
        JLabel lah=new JLabel("1",JLabel.CENTER);lah.setLocation(x,y+CHESSBOARD_SIZE/8*7);lah.setSize(16,CHESSBOARD_SIZE/8);
        lah.setFont(new Font("Rockwell", Font.BOLD, 18));lah.setOpaque(true);lah.setBackground(Color.BLACK);
        lah.setForeground(Color.WHITE);add(lah);
    }
	//设置棋盘4角的4个黑块
    public void addBlockLable(int z){
        JLabel laa=new JLabel("",JLabel.CENTER);laa.setLocation(0,0);laa.setSize(z,z);
        laa.setOpaque(true);laa.setBackground(Color.BLACK);add(laa);
        JLabel lab=new JLabel("",JLabel.CENTER);lab.setLocation(0,z+CHESSBOARD_SIZE);lab.setSize(z,z);
        lab.setOpaque(true);lab.setBackground(Color.BLACK);add(lab);
        JLabel lac=new JLabel("",JLabel.CENTER);lac.setLocation(z+CHESSBOARD_SIZE,0);lac.setSize(z,z);
        lac.setOpaque(true);lac.setBackground(Color.BLACK);add(lac);
        JLabel lad=new JLabel("",JLabel.CENTER);lad.setLocation(z+CHESSBOARD_SIZE,z+CHESSBOARD_SIZE);lad.setSize(z,z);
        lad.setOpaque(true);lad.setBackground(Color.BLACK);add(lad);
    }

    public void addRightLabel(){
        roundLabel = new JLabel("Round:");
        roundLabel.setLocation(HEIGHT-50, 10);
        roundLabel.setSize(200, 40);
        roundLabel.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        roundLabel.setForeground(Color.RED);
        roundLabel.setBackground(Color.BLACK);
        add(roundLabel);

        String color = chessboard.getCurrentPlayer().toString();
        sideLabel = new JLabel("Side:");
        sideLabel.setLocation(HEIGHT-50, 40);
        sideLabel.setSize(200, 60);
        sideLabel.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        sideLabel.setForeground(Color.BLUE);
        add(sideLabel);

        timeLabel = new JLabel("Time: "+SwapLoopTime+"s");
        timeLabel.setLocation(HEIGHT + 100, 0);
        timeLabel.setSize(200, 60);
        timeLabel.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        timeLabel.setForeground(Color.BLUE);
        add(timeLabel);
    }

    public void addNewGameButton(){
        JButton button = new JButton("New Game");
        button.addActionListener((e) -> {
            if(!ButtonCommonProc())
                return;
            int n=JOptionPane.showConfirmDialog(this,"Start a new game?","Dear Sir",
                    JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,icon);
            if(n==0) {  //选了Yes
                NewGame();
                this.repaint();
                if(!TimeThreadExist) {
                    addTimerEventHandler(SwapLoopTime);
                    TimeThreadExist = true;
                }
                else
                    setDownTimes();
            }
        });
        button.setLocation(HEIGHT - 65, 90);
        button.setSize(310, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        button.setForeground(new Color(47, 47, 89));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(253, 0, 152));
                button.setFont(new Font("华文彩云", Font.PLAIN, 30));
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(47, 47, 89));
                button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
            }
        });
    }
    //悔棋
    public void addRegretButton(){
        JButton button = new JButton("Regret Step");
        button.setLocation(HEIGHT - 65, 140);
        button.setSize(310, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        button.setForeground(new Color(47, 47, 89));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(253, 0, 152));
                button.setFont(new Font("华文彩云", Font.PLAIN, 30));
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(47, 47, 89));
                button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ButtonCommonProc())
                    return;
                Stack<String> steps=chessboard.getStepList();
                if(steps.size()==0)
                    JOptionPane.showMessageDialog(null,"没有可悔的棋！",
                            "Dear Sir",JOptionPane.INFORMATION_MESSAGE,icon);
                else{
                    String info=JOptionPane.showInputDialog(null,"请输入要悔棋的步数：",
                            "Dear Sir",JOptionPane.INFORMATION_MESSAGE);
                    int step=Integer.parseInt(info);
                    //悔棋步数超过棋局步数
                    if(step>steps.size()) {
                        JOptionPane.showMessageDialog(null, "没有这么多步数可悔！",
                                "Dear Sir", JOptionPane.INFORMATION_MESSAGE, icon);
                        return;
                    }
                    else if(step>0)
                        chessboard.RegretStep(step);
                }
            }
        });
    }

    public void addLoadButton(){
        JButton button = new JButton("Load Game");
        button.setLocation(HEIGHT - 65, 190);
        button.setSize(310, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        button.setForeground(new Color(47, 47, 89));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(253, 0, 152));
                button.setFont(new Font("华文彩云", Font.PLAIN, 30));
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(47, 47, 89));
                button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
            }
        });

        button.addActionListener(e -> {
            if(!ButtonCommonProc())
                return;
            //载入文件
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("data"));
            chooser.setDialogTitle("Open file");
            FileNameExtensionFilter filter = new FileNameExtensionFilter(null,"txt","TXT");
            chooser.setFileFilter(filter);
            int result = chooser.showOpenDialog(this); // 打开”打开文件”对话框
// int result = dlg.showSaveDialog(this); // 打”开保存文件”对话框
            String absolutePath = "";
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                absolutePath = file.getAbsolutePath();
//                System.out.println("You chose to open this file: " +
//                        file.getName());
                String s=file.getName();

                if(!s.toLowerCase().contains(".txt")){
                    JOptionPane.showMessageDialog(this, "104.不符合存档文件格式，拒绝操作！",
                            "Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(prgstep==null) {
                    prgstep = new ProgressStep(Me, 400, 200,"载入游戏进度");
                    prgstep.setProgress("读取记录……",20);
                }
                List<String> loadGame = gameController.loadGameFromFile(absolutePath);
                String feedBack = gameController.errorCode.toString();
                prgstep.setProgress("存档加载完毕！",100);
                if (loadGame == null){
                    JOptionPane.showMessageDialog(this, feedBack, "Error",JOptionPane.ERROR_MESSAGE);
                }
                else{
                    try {
                        boolean checkok=CheckData(loadGame);
                        if(!checkok)
                            return;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    //chessboard.initiateEmptyChessboard();
                    displayGame(loadGame);
                    //触发界面paint，引起底层重新刷新显示
                    repaint();
                    if(!TimeThreadExist) {
                        addTimerEventHandler(SwapLoopTime);
                        TimeThreadExist = true;
                    }
                    else
                        setDownTimes();
                    //加载完毕，不再显示成功对话框
                    //JOptionPane.showMessageDialog(this, "Load successfully! ", "Tips",JOptionPane.INFORMATION_MESSAGE);
                }
            }
            prgstep=null;
            //这边只需要给一个文件路径，到gamecontrollor
            //判断文件格式错误，返回errorcode104

        });
    }

    public void addSaveButton(){
        JButton button = new JButton("Save Game");
        button.setLocation(HEIGHT - 65, 240);
        button.setSize(310, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        button.setForeground(new Color(47, 47, 89));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(253, 0, 152));
                button.setFont(new Font("华文彩云", Font.PLAIN, 30));
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(47, 47, 89));
                button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
            }
        });


        button.addActionListener(e -> {
            if(!ButtonCommonProc())
                return;
            String defaultFileName = player[0].getName() +"&"+ player[1].getName() + ".txt";

            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("data"));
            chooser.setDialogTitle("Save File");
            FileNameExtensionFilter filter = new FileNameExtensionFilter(null,"txt");
            chooser.setFileFilter(filter);
            chooser.setSelectedFile(new File(defaultFileName));
            int result = chooser.showDialog(this,"save");
            if (result == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fileStream = null;
                try{
                    if(prgstep==null) {
                        prgstep = new ProgressStep(Me, 400, 200,"载入游戏进度");
                        prgstep.setProgress("打开存档……",20);
                    }
                    fileStream = new FileOutputStream(chooser.getSelectedFile());
                    String newLine = System.getProperty("line.separator");
                    //获取棋局数据
                    String AllStr="";
                    List<String> mylist=chessboard.GetGameData();
                    for (int k = 0;k < 11;k++){     //不读入MAC
                        StringBuilder datas = new StringBuilder(mylist.get(k).toString());
                        for (int i = datas.length() - 1;i >= 0;i--){
                            char c = datas.charAt(i);
                            if (c == ']'||c == '['||c == ' '){
                                datas.replace(i,i+1,"");
                            }
                        }
                        fileStream.write(String.valueOf(datas).getBytes());
                        fileStream.write(newLine.getBytes());
                        AllStr+=datas;
                    }
                    //跳过第12行，计算保存MAC
                    prgstep.setProgress("保存棋局数据……",50);
                    String str=GetStringMac(AllStr);
                    fileStream.write(String.valueOf(str).getBytes());
                    fileStream.write(newLine.getBytes());
                    prgstep.setProgress("保存棋局校验数据……",60);
                    //写入步骤数量
                    str=mylist.get(12);
                    fileStream.write(String.valueOf(str).getBytes());
                    fileStream.write(newLine.getBytes());
                    AllStr=str;

                    for (int i = 13;i < mylist.size();i++){
                        //String str = String.valueOf(st.get(i));
                        str=mylist.get(i);
                        fileStream.write(str.getBytes());
                        fileStream.write(newLine.getBytes());
                        AllStr+=str;
                    }
                    prgstep.setProgress("保存走棋步骤……",80);
                    str=GetStringMac(AllStr);
                    fileStream.write(str.getBytes());
                    prgstep.setProgress("生产校验数据……",90);

                    fileStream.close();
                    prgstep.setProgress("保存记录完毕！",100);
                    //System.out.println("You chose to save to this file: " + chooser.getSelectedFile().getName());
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                prgstep=null;
            }
//            gameController.loadGameFromFile(path);
        });
    }

    //复盘Review
    public void addReviewButton(){
        JButton button = new JButton("Review");
        button.setLocation(HEIGHT - 65, 290);
        button.setSize(310, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        button.setForeground(new Color(47, 47, 89));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(253, 0, 152));
                button.setFont(new Font("华文彩云", Font.PLAIN, 30));
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(47, 47, 89));
                button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ButtonCommonProc())
                    return;
                Stack<String> slist=chessboard.getStepList();
                if(slist.size()==0){
                    JOptionPane.showMessageDialog(null, "没有可复盘的信息！", "Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
				//倒计时线程暂停
                setDownTimes(-2);
				//开始Review线程
                new ReviewThread(Me,slist).start();
				//做好线程标志，其他操作前都判该标志，为true一律不允许做
                ReviewThreadExist=true;
				//布置新棋局,供Review线程做回放
                NewGame();
                Me.repaint();

            }
        });
    }

    public void addRankButton(){
        JButton button = new JButton("Ranking List");
        button.setLocation(HEIGHT - 65, 340);
        button.setSize(310, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        button.setForeground(new Color(47, 47, 89));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(253, 0, 152));
                button.setFont(new Font("华文彩云", Font.PLAIN, 30));
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(47, 47, 89));
                button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ButtonCommonProc())
                    return;
                try {
                    RankList rk=new RankList(Me);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    //更换背景按钮显示及事件处理
    public void addChBGButton(){
        JButton button = new JButton("Change Skin");
        button.setLocation(HEIGHT - 65, 390);
        button.setSize(310, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        button.setForeground(new Color(47, 47, 89));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(253, 0, 152));
                button.setFont(new Font("华文彩云", Font.PLAIN, 30));
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(47, 47, 89));
                button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ButtonCommonProc())
                    return;
                //先保存棋局
                List<String> GameData= null;
				GameData = chessboard.GetGameData();
				
                String[] options={"A Picture1","B Picture2","C Picture3","D Picture4"};
                int n =  JOptionPane.showOptionDialog(null,"请选择新背景：","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,icon,options,options[0]);		//选择对话框*/
                switch (n){
                    case 0:
                    default:
                        BKimage=new ImageIcon(this.getClass().getResource("/images/bg1.jpg"));
                        break;
                    case 1:
                        BKimage=new ImageIcon(this.getClass().getResource("/images/bg2.jpg"));
                        break;
                    case 2:
                        BKimage=new ImageIcon(this.getClass().getResource("/images/bg3.jpg"));
                        break;
                    case 3:
                        BKimage=new ImageIcon(this.getClass().getResource("/images/bg4.jpg"));
                        break;
                }
				String OldChessPath=chessboard.getChessPath();
                //重新显示界面
                addImageByRepaint();
				chessboard.setChessPath(OldChessPath);
                //刷新棋局
                displayGame(GameData);
            }
        });
    }

    //更换棋盘
    public void addChChessBoardButton(){
        JButton button = new JButton("Change ChessBoard");
        button.setLocation(HEIGHT - 65, 440);
        button.setSize(310, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 14));
        add(button);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        button.setForeground(new Color(47, 47, 89));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(253, 0, 152));
                button.setFont(new Font("华文彩云", Font.PLAIN, 30));
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(47, 47, 89));
                button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color col0,col1;
                if(!ButtonCommonProc())
                    return;
                //先保存棋局
                List<String> GameData=chessboard.GetGameData();
                String[] options={"A:白--黑","B:白--蓝","C:白--绿","D:白--青","E:白--粉","F:白--淡灰","G:白--灰","H:白--深灰",
                        "I:淡灰--黑","J:淡灰--蓝","K:淡灰--绿","L:淡灰--青","M:淡灰--粉"};
                String info=(String)JOptionPane.showInputDialog(null,"请选择棋盘颜色方案","Dear Sir",
                        JOptionPane.INFORMATION_MESSAGE,icon,options,options[0]);
                switch(info.charAt(0)){
                    case 'A':
                    default:
                        col0=Color.WHITE;col1=Color.BLACK;
                        break;
                    case 'B':
                        col0=Color.WHITE;col1=Color.BLUE;
                        break;
                    case 'C':
                        col0=Color.WHITE;col1=Color.green;
                        break;
                    case 'D':
                        col0=Color.WHITE;col1=Color.CYAN;
                        break;
                    case 'E':
                        col0=Color.WHITE;col1=Color.PINK;
                        break;
                    case 'F':
                        col0=Color.WHITE;col1=Color.LIGHT_GRAY;
                        break;
                    case 'G':
                        col0=Color.WHITE;col1=Color.GRAY;
                        break;
                    case 'H':
                        col0=Color.WHITE;col1=Color.DARK_GRAY;
                        break;
                    case 'I':
                        col0=Color.LIGHT_GRAY;col1=Color.BLACK;
                        break;
                    case 'J':
                        col0=Color.LIGHT_GRAY;col1=Color.BLUE;
                        break;
                    case 'K':
                        col0=Color.LIGHT_GRAY;col1=Color.GREEN;
                        break;
                    case 'L':
                        col0=Color.LIGHT_GRAY;col1=Color.CYAN;
                        break;
                    case 'M':
                        col0=Color.LIGHT_GRAY;col1=Color.PINK;
                        break;
                }
                ChessComponent[][] CCponents=chessboard.getChessComponents();
                CCponents[0][0].setBackGroundColors(col0,col1);
                Me.repaint();
                //刷新棋局
                displayGame(GameData);
            }
        });
    }

    //更换棋子类型
    public void addChFormButton(){
        JButton button = new JButton("Change ChessStyle");
        button.setLocation(HEIGHT - 65, 490);
        button.setSize(310, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));

        add(button);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        button.setForeground(new Color(47, 47, 89));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(253, 0, 152));
                button.setFont(new Font("华文彩云", Font.PLAIN, 30));
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(47, 47, 89));
                button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ButtonCommonProc())
                    return;
                //先保存棋局
                List<String> GameData=chessboard.GetGameData();
                String[] options={"Orange-Blue","White-Black"};
                int n =  JOptionPane.showOptionDialog(null,"Choose a new type:","Tips",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,icon,options,options[0]);		//选择对话框*/
                switch (n){
                    case 0:
                    default:
                        chessboard.setChessPath("chess1");
                        break;
                    case 1:
                        chessboard.setChessPath("chess2");
                        break;
                }
                //刷新棋局
                displayGame(GameData);
                //触发界面paint，引起底层重新刷新显示
                Graphics2D g2 = (Graphics2D) getGraphics();
                repaint();
                g2.dispose();
            }
        });
    }

    //增加背景音乐控制功能
    public void addBGSoundButton(){
        JButton SoundBtn = new JButton("Play BG Sound");
        SoundBtn.setLocation(HEIGHT - 65, 540);
        SoundBtn.setSize(310, 40);
        SoundBtn.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(SoundBtn);

        SoundBtn.setContentAreaFilled(true);
        SoundBtn.setBorderPainted(false);
        SoundBtn.setFocusPainted(false);
        SoundBtn.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        SoundBtn.setForeground(new Color(47, 47, 89));

        SoundBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                SoundBtn.setForeground(new Color(253, 0, 152));
                SoundBtn.setFont(new Font("华文彩云", Font.PLAIN, 30));
            }

            public void mouseExited(MouseEvent e) {
                SoundBtn.setForeground(new Color(47, 47, 89));
                SoundBtn.setFont(new Font("华文琥珀", Font.PLAIN, 30));
            }
        });
        SoundBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ButtonCommonProc())
                    return;
                if(SoundBtn.getText().contains("Play")){
                    BackGroundSound.play();
                    SoundBtn.setText("Stop BG Sound");
                }
                else{
                    BackGroundSound.stop();
                    SoundBtn.setText("Play BG Sound");
                }
            }
        });
    }

    public void addExitButton(){
        JButton button = new JButton("Exit");
        button.setLocation(HEIGHT - 65, 590);
        button.setSize(310, 40);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
        button.setForeground(new Color(47, 47, 89));
        add(button);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(253, 0, 152));
                button.setFont(new Font("华文彩云", Font.PLAIN, 30));
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(47, 47, 89));
                button.setFont(new Font("华文琥珀", Font.PLAIN, 30));
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!ButtonCommonProc())
                    return;
                System.exit(0);
            }
        });
    }

    //按钮功能的操作
    public boolean ButtonCommonProc(){
        if(ReviewThreadExist){
            JOptionPane.showMessageDialog(this, "正在进行复盘操作，请不要打扰！",
                    "Error",JOptionPane.ERROR_MESSAGE);
            return(false);
        }
        BackGroundSound.play("button");
        chessboard.clickController.FrameDoButton();
        return(true);
    }

    public void ButtonCheckMate() {
        BackGroundSound.play("将军");
        //chessboard.clickController.FrameDoButton();
    }

    public void ButtonKill() {
        BackGroundSound.play("绝杀无解");
        //chessboard.clickController.FrameDoButton();
    }

    public void NewGame(){
        String[] cmd=  {"RNBQKBNR",
                "PPPPPPPP",
                "________",
                "________",
                "________",
                "________",
                "pppppppp",
                "rnbqkbnr",
                "w","1","w1,w2","MAC","0"};
        List<String> chBoard=new ArrayList<String>();
        for(int i=0;i<cmd.length;i++){
            chBoard.add(cmd[i]);
        }
        displayGame(chBoard);
    }

    //载入棋局，刷新界面及提示
    public void displayGame(List<String> list){
        //清空棋盘
        chessboard.initiateEmptyChessboard();
        //清除走棋记录
        chessboard.ClearSteps();
        //摆棋子
        chessboard.loadGame(list);
        //刷新信息
        RefreshStep();

    }
    //刷新显示回合及当前方
    public void RefreshStep(){
        roundLabel.setText("Round: "+chessboard.getRound());
        sideLabel.setText("Side: "+chessboard.getSide());
    }

    //倒计时线程，只要启动，线程会一直存在
    //DownTimes>=0倒计时时间，需要显示
    //DownTimes=-2-3，不显示内容（设置该值时，务必使用-2 -3可能会变化出来）
    public void addTimerEventHandler(int second){
        DownTimes = second;
        new Thread(){
            public void run() {
                while(DownTimes>=-3){
                    if(DownTimes == -2 || DownTimes == -3){
                        timeLabel.setText("");
                    }
                    else{
                        timeLabel.setText("Time: "+DownTimes+"s");
                    }
                    //当前时间
                    try {
                        Thread.sleep(1000);
                        //这个值，让线程暂停
                        if( (DownTimes!=-2)&&(DownTimes!=-3) )
                            DownTimes--;
                        //倒计时结束，需要交换当前方
                        if (DownTimes == -1){
                            //清除当前可能进行到一半的步骤
                            chessboard.clickController.FrameDoButton();
                            //先记录步骤，再换颜色
                            String s2=Integer.toString(chessboard.getRound() );
                            String s1;
                            if(chessboard.getCurrentPlayer()==ChessColor.BLACK)
                                s1="b";
                            else
                                s1="w";
                            chessboard.addStepList("______,"+s1+","+s2);
                            chessboard.swapColor();
                            DownTimes = SwapLoopTime;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    //将比赛结果更新到用户并保存起来
    //参数 wincolor 获胜方
    // 如平局 为ChessColor.NONE
    public void UpdateGameResultToPlayer(ChessColor wincolor) throws IOException {
        player[0].FinishGames++;
        player[1].FinishGames++;
        if(wincolor==ChessColor.NONE){
            player[0].TieGames++;
            player[1].TieGames++;
        }
        else{
            if(player[0].chessColor==wincolor){
                player[0].WinGames++;
            }
            else
                player[1].WinGames++;
        }
        player[0].UpdatePlayer();
        player[1].UpdatePlayer();
    }

    public String GetStringMac(String instr) throws Exception {
        String Key = "1234567890123456789012345678901234567890123456789012345678901234";

        byte[] keyHmacMD5 = Key.getBytes();
        String result = MACCoder.encodeHmacMD5(instr.getBytes(), keyHmacMD5);

        return (result);
    }

    //校验存盘数据及步骤数据，防篡改
    public boolean CheckData(List<String> ListData) throws Exception {
        int i;
        String sAll="";
        //校验棋盘 //8+当前方+回合数+选手方+校验
        for(i=0;i<11;i++){
            sAll+=ListData.get(i);
        }
        String Mac=GetStringMac(sAll);
        if(!Mac.equals(ListData.get(11))) {   //与记录的数据做对比
            JOptionPane.showMessageDialog(this, "存档数据校验不通过，怀疑数据被篡改，不予接受！",
                    "Error",JOptionPane.ERROR_MESSAGE);
            return(false);
        }
        //校验是否当前用户
        sAll=ListData.get(10);
        String[] ss=sAll.split(",");
        if((!ss[0].equals(player[0].getName()))||(!ss[1].equals(player[1].getName()))){
            sAll=String.format("当前选手是[%s]-[%s],存档中选手是[%s]-[%s],选手不一致，不予接受",
                    player[0].getName(),player[1].getName(),ss[0],ss[1]);
            JOptionPane.showMessageDialog(this, sAll,
                    "Error",JOptionPane.ERROR_MESSAGE);
            return(false);
        }
        //继续步骤校验
        sAll=ListData.get(12);
        int StepCount=Integer.parseInt(sAll);;
        for(i=13;i<13+StepCount;i++){
            sAll+=ListData.get(i);
        }
        Mac="";
        Mac=GetStringMac(sAll);
        if(!Mac.equals(ListData.get(13+StepCount))){
            JOptionPane.showMessageDialog(this, "步骤数据校验不通过，怀疑数据被篡改，不予接受！",
                    "Error",JOptionPane.ERROR_MESSAGE);
            return(false);
        }
        return(true);
    }
}


class ImagePanel extends JPanel {
    Dimension d;
    Image image;
    public ImagePanel(Dimension d, Image image) {
        super();
        this.d =d;
        this.image =image;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0, 0, d.width, d.height, this);
    }
}

//从网上找的做Mac校验的类
class MACCoder {
    /**
     * 产生HmacMD5摘要算法的密钥
     */
    public static byte[] initHmacMD5Key() throws NoSuchAlgorithmException, NoSuchAlgorithmException {
        // 初始化HmacMD5摘要算法的密钥产生器
        KeyGenerator generator = KeyGenerator.getInstance("HmacMD5");
        // 产生密钥
        SecretKey secretKey = generator.generateKey();
        // 获得密钥
        byte[] key = secretKey.getEncoded();
        return key;
    }

    /**
     * HmacMd5摘要算法
     * 对于给定生成的不同密钥，得到的摘要消息会不同，所以在实际应用中，要保存我们的密钥
     */
    public static String encodeHmacMD5(byte[] data, byte[] key) throws Exception {
        // 还原密钥
        SecretKey secretKey = new SecretKeySpec(key, "HmacMD5");
        // 实例化Mac
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        //初始化mac
        mac.init(secretKey);
        //执行消息摘要
        byte[] digest = mac.doFinal(data);
        return new HexBinaryAdapter().marshal(digest);//转为十六进制的字符串
    }

    /**
     * 产生HmacSHA1摘要算法的密钥
     */
    public static byte[] initHmacSHAKey() throws NoSuchAlgorithmException {
        // 初始化HmacMD5摘要算法的密钥产生器
        KeyGenerator generator = KeyGenerator.getInstance("HmacSHA1");
        // 产生密钥
        SecretKey secretKey = generator.generateKey();
        // 获得密钥
        byte[] key = secretKey.getEncoded();
        return key;
    }

    /**
     * HmacSHA1摘要算法
     * 对于给定生成的不同密钥，得到的摘要消息会不同，所以在实际应用中，要保存我们的密钥
     */
    public static String encodeHmacSHA(byte[] data, byte[] key) throws Exception {
        // 还原密钥
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA1");
        // 实例化Mac
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        //初始化mac
        mac.init(secretKey);
        //执行消息摘要
        byte[] digest = mac.doFinal(data);
        return new HexBinaryAdapter().marshal(digest);//转为十六进制的字符串
    }

    /**
     * 产生HmacSHA256摘要算法的密钥
     */
    public static byte[] initHmacSHA256Key() throws NoSuchAlgorithmException {
        // 初始化HmacMD5摘要算法的密钥产生器
        KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
        // 产生密钥
        SecretKey secretKey = generator.generateKey();
        // 获得密钥
        byte[] key = secretKey.getEncoded();
        return key;
    }

    /**
     * HmacSHA1摘要算法
     * 对于给定生成的不同密钥，得到的摘要消息会不同，所以在实际应用中，要保存我们的密钥
     */
    public static String encodeHmacSHA256(byte[] data, byte[] key) throws Exception {
        // 还原密钥
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA256");
        // 实例化Mac
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        //初始化mac
        mac.init(secretKey);
        //执行消息摘要
        byte[] digest = mac.doFinal(data);
        return new HexBinaryAdapter().marshal(digest);//转为十六进制的字符串
    }

    /**
     * 产生HmacSHA256摘要算法的密钥
     */
    public static byte[] initHmacSHA384Key() throws NoSuchAlgorithmException {
        // 初始化HmacMD5摘要算法的密钥产生器
        KeyGenerator generator = KeyGenerator.getInstance("HmacSHA384");
        // 产生密钥
        SecretKey secretKey = generator.generateKey();
        // 获得密钥
        byte[] key = secretKey.getEncoded();
        return key;
    }

    /**
     * HmacSHA1摘要算法
     * 对于给定生成的不同密钥，得到的摘要消息会不同，所以在实际应用中，要保存我们的密钥
     */
    public static String encodeHmacSHA384(byte[] data, byte[] key) throws Exception {
        // 还原密钥
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA384");
        // 实例化Mac
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        //初始化mac
        mac.init(secretKey);
        //执行消息摘要
        byte[] digest = mac.doFinal(data);
        return new HexBinaryAdapter().marshal(digest);//转为十六进制的字符串
    }

    /**
     * 产生HmacSHA256摘要算法的密钥
     */
    public static byte[] initHmacSHA512Key() throws NoSuchAlgorithmException {
        // 初始化HmacMD5摘要算法的密钥产生器
        KeyGenerator generator = KeyGenerator.getInstance("HmacSHA512");
        // 产生密钥
        SecretKey secretKey = generator.generateKey();
        // 获得密钥
        byte[] key = secretKey.getEncoded();
        return key;
    }

    /**
     * HmacSHA1摘要算法
     * 对于给定生成的不同密钥，得到的摘要消息会不同，所以在实际应用中，要保存我们的密钥
     */
    public static String encodeHmacSHA512(byte[] data, byte[] key) throws Exception {
        // 还原密钥
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA512");
        // 实例化Mac
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        //初始化mac
        mac.init(secretKey);
        //执行消息摘要
        byte[] digest = mac.doFinal(data);
        return new HexBinaryAdapter().marshal(digest);//转为十六进制的字符串
    }
}

class ReviewThread extends Thread{
    private ChessGameFrame faframe;
    private ArrayList<String> slist=new ArrayList<>();
	//构造回放线程，得到主窗口类和走棋步骤数据
    ReviewThread(ChessGameFrame frame,Stack<String> stack){
        this.faframe=frame;
        for(int i=0;i<stack.size();i++){
            slist.add(stack.get(i));
        }
    }
	//回放线程，完毕后退出
    public void run(){
        int count=slist.size();
        System.out.println("count="+count);
        int i=0;
        while(i<count){
            try {
                Thread.sleep(800);
//                System.out.printf("[%d] [%s]\n",i,slist.get(i));
                ReviewOneStep(slist.get(i));

                i++;
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        faframe.setReviewThreadExist(false);
//        System.out.println("复盘进程退出");
    }
	//回放一步操作
    private void ReviewOneStep(String s) throws InterruptedException, IOException { ;
        String[] ss=s.split(",");
        String s1 = ss[0].substring(1, 2);
		//超时换手的情况，只更新当前方和回合数
        if(s1.equals("_")){
            s1="";
        }
        else{
            int sx = Integer.parseInt(ss[0].substring(1, 2));
            int sy = Integer.parseInt(ss[0].substring(2, 3));
            int tx = Integer.parseInt(ss[0].substring(4, 5));
            int ty = Integer.parseInt(ss[0].substring(5, 6));
            ChessComponent first = faframe.chessboard.chessComponents[sx][sy];
            ChessComponent second = faframe.chessboard.chessComponents[tx][ty];
            //选择第一个棋子
            first.setSelected(true);
            first.repaint();
            faframe.chessboard.clickController.ShowAllCanMove(first, true);
            Thread.sleep(800);
			//交换位置
			first.setSelected(false);
            faframe.chessboard.clickController.ShowAllCanMove(first, false);
            faframe.chessboard.swapChessComponents(first, second);
            first = null;
        }
		//更新当前方和回合数
        faframe.chessboard.setcurPlayerRound(ss[1],ss[2]);
        faframe.chessboard.cgameFrame.RefreshStep();
    }
}
