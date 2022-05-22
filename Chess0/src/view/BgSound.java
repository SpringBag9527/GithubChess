package view;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class BgSound {
        private static Clip bgm;//背景乐
        private static Clip hit;//音效
        private static AudioInputStream ais;
        private int WavIndex=1;
        BgSound(){}

        public void play(){
            try {
                bgm=AudioSystem.getClip();
                String fname="sounds/bgmusic/bg"+WavIndex+".wav";
                InputStream is=BgSound.class.getClassLoader().getResourceAsStream(fname);
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
        public void play(String name){
            try {
                hit=AudioSystem.getClip();
                String fname="sounds/acsound/"+name+".wav";
                InputStream is=BgSound.class.getClassLoader().getResourceAsStream(fname);
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
