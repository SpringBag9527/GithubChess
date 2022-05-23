package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

//选手排行榜
public class RankList extends JDialog {
    private Dimension frameSize;
    private ImageIcon BKimage;
    private final int WIDTH=650;
    private final int HEIGHT=400;
    private JList listshow;
    ArrayList<ChessPlayer> playlist=new ArrayList<>();
    private ChessGameFrame cgameFrame;

    public RankList(ChessGameFrame cgame) throws IOException {
        this.cgameFrame=cgame;
        setTitle("参赛选手排行榜");
        setSize(WIDTH,HEIGHT);
        setResizable(false);
        setModal(true);
        frameSize=new Dimension(WIDTH,HEIGHT);
        BKimage=new ImageIcon(this.getClass().getResource("/images/bg2.jpg"));
        //设置背景图片
        ImagePanel imagePanel= new ImagePanel(frameSize, BKimage.getImage());
        setContentPane(imagePanel);
        setLocationRelativeTo(null);
        setLayout(null);

        //获取所有player
        ChessPlayer pl=new ChessPlayer("aa");
        playlist=pl.GetAllPlayer();
        String[] sOrderInfo=viewPlayer(SortBy.FinishGames);

        JLabel jla=new JLabel("排名 选手    比赛场次 获胜场次   获胜率 %");
        jla.setLocation(10,10);
        jla.setSize(460,30);
        jla.setBackground(Color.lightGray);
        jla.setOpaque(true);

        listshow=new JList(sOrderInfo);
        listshow.setVisibleRowCount(10);
        JScrollPane jsp1=new JScrollPane(listshow);
        jsp1.setSize(460,310);
        jsp1.setLocation(10,40);

        AddButtons();

        add(jsp1);
        add(jla);
        setVisible(true);
    }
    public void AddButtons(){
        JButton button=new JButton("比赛场次");
        button.setLocation(480,10);
        button.setSize(150,40);
        add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cgameFrame.BackGroundSound.play("button");
                String[] sOrderInfo=sOrderInfo=viewPlayer(SortBy.FinishGames);
                listshow.setListData(sOrderInfo);
            }
        });

        JButton button2=new JButton("获胜场次");
        button2.setLocation(480,70);
        button2.setSize(150,40);
        add(button2);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cgameFrame.BackGroundSound.play("button");
                String[] sOrderInfo=sOrderInfo=viewPlayer(SortBy.WinGames);
                listshow.setListData(sOrderInfo);
            }
        });

        JButton button3=new JButton("获胜概率");
        button3.setLocation(480,130);
        button3.setSize(150,40);
        add(button3);
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cgameFrame.BackGroundSound.play("button");
                String[] sOrderInfo=sOrderInfo=viewPlayer(SortBy.WinRate);
                listshow.setListData(sOrderInfo);
            }
        });

        //退出按钮
        JButton button4=new JButton("退出");
        button4.setLocation(480,300);
        button4.setSize(150,40);
        add(button4);
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cgameFrame.BackGroundSound.play("button");
                dispose();
            }
        });
    }
    //确保点击右上角关闭钮不会被关闭
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        } else {
            super.processWindowEvent(e);
        }
    }
    //按照规范方式排序
    public String[] viewPlayer(SortBy sortMethod){
        int     playernum=playlist.size();
        String[] sret=new String[playernum];
        float[] tmplist=new float[playernum];
        int[]   orderlist=new int[playernum];
        int     ordersize=0;    //已拍好的顺序数
        int     i;

        switch(sortMethod){
            case FinishGames:
                for(i=0;i<playernum;i++)
                    tmplist[i]=playlist.get(i).getFinishGames();
                orderlist=GetSort(tmplist);
                break;
            case WinGames:
                for(i=0;i<playernum;i++)
                    tmplist[i]=playlist.get(i).getWinGames();
                orderlist=GetSort(tmplist);
                break;
            case WinRate:
                for(i=0;i<playernum;i++)
                    tmplist[i]=playlist.get(i).getWinRate();
                orderlist=GetSort(tmplist);
                break;
        }
        //组织输出数据
        for(i=0;i<playernum;i++){
            int pos=orderlist[i];
            ChessPlayer thePlayer=playlist.get(pos);
            String stmp=String.format("%04d %-6s %8d %8d %8.2f",i+1,thePlayer.getName(),thePlayer.getFinishGames(),
                    thePlayer.getWinGames(),thePlayer.getWinRate()*100);
            sret[i]=new String(stmp);

        }
        return(sret);
    }
	//获得排序指向数组
    public int[] GetSort(float[] fvalue){
        int count=fvalue.length;
        int[] order=new int[count];
        int i;
        int ordernum=0;
        while(ordernum<count) {
            float tmpf=(float)(-100);
            int pos=-1;
            for(i=0;i<count;i++){
                if(InArrayList(i,order,ordernum))
                    continue;
                if(fvalue[i]>tmpf){
                    tmpf=fvalue[i];
                    pos=i;
                }
            }
            order[ordernum]=pos;
            ordernum++;
        }
        return(order);
    }
	//是否在列表中
    public boolean InArrayList(int val,int[] iList,int lListSize){
        int i;
        for(i=0;i<lListSize;i++){
            if(iList[i]==val)
                break;
        }
        if(i>=lListSize)
            return(false);
        else
            return(true);
    }
}

enum SortBy {
    FinishGames,WinGames,WinRate
}
