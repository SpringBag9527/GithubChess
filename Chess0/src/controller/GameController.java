package controller;

import view.Chessboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private Chessboard chessboard;
    public List<Integer> errorCode = new ArrayList<>();

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
        TimeEndSwapColor();
    }

    public void TimeEndSwapColor(){
        if (chessboard.returnDownTimes() == 0){
            chessboard.swapColor();
        }
    }

    public List<String> loadGameFromFile(String path) {
        try{
            List<String> chessData = new ArrayList<>();
            BufferedReader br = Files.newBufferedReader(Paths.get(path));
            for (int i = 0; i < 12;i++){  //8+当前方+回合数+选手方+校验
                chessData.add(br.readLine());
            }
        boolean legal = true;
        //竖直不为8
        if (chessData.size() < 8){
            legal = false;
            errorCode.add(101);
        }
        else if (chessData.get(8).length() == 8){
            legal = false;
            if (errorCode.size() == 0){
                errorCode.add(101);
            }
        }
        //水平不为8
        for (int i = 0;i < 8;i++){
            if (chessData.get(i).length() != 8){
                legal = false;
                if (errorCode.size() == 0){
                    errorCode.add(101);
                }
                break;
            }
        }
        chessboard.cgameFrame.prgstep.setProgress("存档格式校验……",40);
        //检测合法棋子
        for (int i = 0;i < 8;i++){
            for (int j = 0;j < 8;j++){
                char a = chessData.get(i).charAt(j);
                switch (a){
                    case 'K':
                        break;
                    case 'k':
                        break;
                    case 'Q':
                        break;
                    case 'q':
                        break;
                    case 'N':
                        break;
                    case 'n':
                        break;
                    case 'B':
                        break;
                    case 'b':
                        break;
                    case 'R':
                        break;
                    case 'r':
                        break;
                    case 'P':
                        break;
                    case 'p':
                        break;
                    case '_':
                        break;
                    default:
                        legal = false;
                        errorCode.add(102);
                        break;
                }
            }
        }
        chessboard.cgameFrame.prgstep.setProgress("棋子数据校验……",60);
        if (chessData.size() < 9){
            errorCode.add(103);
            legal = false;
        }
        else{
            if (!chessData.get(8).contains("w") && !chessData.get(8).contains("b")){
                if (!errorCode.contains(103)){
                    errorCode.add(103);
                }
                legal = false;
            }
        }

        //继续读入步骤
        String s=br.readLine();
        chessData.add(s);
        int count=Integer.parseInt(s);
        for (int i = 0; i < count+1;i++){
            chessData.add(br.readLine());
        }

        if (legal){
            return chessData;
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        chessboard.cgameFrame.prgstep.setProgress("存档数据校验完毕",80);
        return null;
    }

}
