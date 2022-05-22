package view;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

//自定义弹出窗口类

public class ProgressStep extends JDialog {
    private JProgressBar probar;
    public ArrayList<String> slist=new ArrayList<String>();

    public ProgressStep(JFrame frame,int width,int height,String title) {
        super(frame);//给弹窗指定父窗口this
        setTitle(title);
        //setModal(true);//!!! 设置为模态窗口,父窗口不能被点击
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);

        probar=new JProgressBar();
        probar.setLocation(20,20);
        probar.setSize(350,50);
        probar.setStringPainted(true);
        probar.setVisible(true);
        add(probar);
        new ProgressThread(probar, this).start();

        setVisible(true);
    }

    //给进度条设置值和显示内容 最终以此控制进度条结束关闭
    public void setProgress(String text,int val){
        //probar.setValue(val);
        //probar.setString(text);
        slist.add(text);
    }

    //确保点击右上角关闭钮不会被关闭
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        } else {
            super.processWindowEvent(e);
        }
    }

}

class ProgressThread extends Thread{
    private JProgressBar progressBar;
    private ProgressStep dlgFrame;

    ProgressThread(JProgressBar progressBar,ProgressStep dlgfrm){
        this.progressBar=progressBar;
        this.dlgFrame=dlgfrm;
    }

    public void run(){
        int val;
        ArrayList <String> slist;
        slist= dlgFrame.slist;

        while(true){
            try {
                val=progressBar.getValue();
                if(val>=100)
                    break;
                Thread.sleep(300);
                int count=slist.size();
                if(count>0)
                    val+=(100-val)/count;
                else
                    val=100;
                progressBar.setValue(val);
                progressBar.setString(slist.get(0));
                slist.remove(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        progressBar.setValue(100);
        progressBar.setString("完成！");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //结束对话框，退出
        dlgFrame.dispose();
    }
}