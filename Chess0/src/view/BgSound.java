package view;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BgSound {
        private static Clip bgm;//背景乐
        private static Clip hit;//音效
        private static AudioInputStream ais;
        private int WavIndex=1;     //背景音乐序号，最大到5
        //背景音乐播放
        public void play(){
            try {
                bgm=AudioSystem.getClip();
                String filename="sounds/music/bg"+WavIndex+".wav";

                InputStream is_0=getClass().getClassLoader().getResourceAsStream(filename);
                BufferedInputStream is = new BufferedInputStream(is_0);
                //getclassLoader得到当前类的加载器.getResourceAsStream加载资源，只能加载wav的音乐格式
                if (is != null) {
                    ais=AudioSystem.getAudioInputStream(is);//获取输入流
                }
                bgm.open(ais);
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
            bgm.start();//开始播放
            bgm.loop(Clip.LOOP_CONTINUOUSLY);//循环播放
        }
        //声音效果播放，需要送入约定声音文件名 如按钮音"Button" 移动音"Move" 击杀音"Kill"
        public void play(String name){
            try {
                hit=AudioSystem.getClip();
                String filename="sounds/sound/"+name+".wav";
                InputStream is_0=getClass().getClassLoader().getResourceAsStream(filename);
                BufferedInputStream is = new BufferedInputStream(is_0);
                //getclassLoader得到当前类的加载器.getResourceAsStream加载资源，只能加载wav的音乐格式
                if (is != null) {
                    ais=AudioSystem.getAudioInputStream(is);//获取输入流
                }
                hit.open(ais);
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
            hit.start();//开始播放
        }
        //停止背景音乐播放
        public void stop()
        {
            if(ais!=null) {
                bgm.close();
                //实现换曲子的功能
                WavIndex++;
                if(WavIndex>5)
                    WavIndex=1;
            }
        }
}

