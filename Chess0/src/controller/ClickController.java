package controller;


import model.ChessComponent;
import model.KingChessComponent;
import model.PawnChessComponent;
import model.RookChessComponent;
import view.Chessboard;
import view.ChessboardPoint;

import java.util.ArrayList;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    //主窗口做按钮操作，如果底层操作到一半，需要自行取消
    public void FrameDoButton(){
        if(first!=null){
            first.setSelected(false);
            ShowAllCanMove(first,false);
            ChessComponent recordFirst = first;
            first = null;
            recordFirst.repaint();
        }
    }

    public void onClick(ChessComponent chessComponent) {
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessboard.cgameFrame.BackGroundSound.play("chess");
                chessComponent.setSelected(true);
                ShowAllCanMove(chessComponent,true);
                first = chessComponent;
                first.repaint();
            }
        }
        else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                ShowAllCanMove(first,false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
            } else if (handleSecond(chessComponent)) {
                //repaint in swap chess method.
                ShowAllCanMove(first,false);
                chessboard.swapChessComponents(first, chessComponent);
                chessboard.swapColor();
                chessboard.swapChessComponentsJudge(first);
                first.setSelected(false);
                first = null;
                chessboard.cgameFrame.RefreshStep();
            }
        }
    }
//public void onClick(ChessComponent chessComponent) {
//    if (first == null) {
//        if (handleFirst(chessComponent)) {
//                chessboard.cgameFrame.BackGroundSound.play("chess");
//                chessComponent.setSelected(true);
//                ShowAllCanMove(chessComponent,true);
//                first = chessComponent;
//                first.repaint();
//            }
//    } else {
//        if (first == chessComponent) { // 再次点击取消选取
//            chessComponent.setSelected(false);
//            ShowAllCanMove(first,false);
//            ChessComponent recordFirst = first;
//            first = null;
//            recordFirst.repaint();
//        } else if (handleSecond(chessComponent)) {
//            //repaint in swap chess method.
//            ShowAllCanMove(first,false);
//                if (chessboard.swapChessComponentsJudge(first, chessComponent)) {
//                    chessboard.swapChessComponents(first, chessComponent);
//                    chessboard.swapColor();
//                    first.setSelected(false);
//                    first = null;
//                    System.out.print("1");
//                }
//                else {
//                    first.setSelected(false);
//                    first = null;
//                    System.out.print("2");
//                }
//            chessboard.cgameFrame.RefreshStep();
//        }
//    }
//}
    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentPlayer();
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        return chessComponent.getChessColor() != chessboard.getCurrentPlayer() &&
                first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint());
    }

    //显示某个棋子的所有可移动点  flag=true 显示  flag=false 取消显示
    public void ShowAllCanMove(ChessComponent chessComponent,boolean flag){
        ArrayList<ChessboardPoint> list = chessComponent.getCanMoveToList(chessboard.getChessComponents(),chessComponent);
        ChessComponent[][] cp = chessboard.getChessComponents();
        for (int i = 0;i < list.size();i ++){
            int x = list.get(i).getX();
            int y = list.get(i).getY();
            cp[x][y].setCanMove(flag);
            cp[x][y].repaint();
        }
    }
}
