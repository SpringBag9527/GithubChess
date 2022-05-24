package controller;


import model.*;
import view.Chessboard;
import view.ChessboardPoint;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    //主窗口做按钮操作，如果底层操作到一半，需要自行取消
    public void FrameDoButton() {
        if (first != null) {
            first.setSelected(false);
            ShowAllCanMove(first, false);
            ChessComponent recordFirst = first;
            first = null;
            recordFirst.repaint();
        }
    }

    public void onClick(ChessComponent chessComponent) throws IOException {
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessboard.cgameFrame.BackGroundSound.play("chess");
                chessComponent.setSelected(true);
                ShowAllCanMove(chessComponent, true);
                first = chessComponent;
                first.repaint();
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                ShowAllCanMove(first, false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
            } else if (handleSecond(chessComponent)) {
                //repaint in swap chess method.
                ShowAllCanMove(first, false);
                if (first instanceof PawnChessComponent) {
                    //判断兵是不是走了第一步
                    if (Math.abs(first.getChessboardPoint().getX() - chessComponent.getChessboardPoint().getX()) == 2) {
                        ((PawnChessComponent) first).judge = ((PawnChessComponent) first).judge + 1;
                    } else {
                        ((PawnChessComponent) first).judge = ((PawnChessComponent) first).judge + 2;
                    }
                    ((PawnChessComponent) first).judgeRound = chessboard.getRound();
                    if (first.getChessColor() == ChessColor.BLACK) {
                        if (chessComponent.getChessboardPoint().getX() == 7) {
                            chessboard.swapPawnChangeComponents(first,chessComponent);
                            chessboard.swapColor();
                            chessboard.swapChessComponentsJudge(first);
                            first.setSelected(false);
                            first = null;
                            if (chessboard.getCurrentPlayer().getColor() == Color.BLACK){
                                chessboard.plus(chessboard.getRound());
                            } else if (chessboard.getCurrentPlayer().getColor() == Color.WHITE){
                                chessboard.plus(chessboard.getRound() - 1);
                            }
                            chessboard.cgameFrame.RefreshStep();
                        } else if (first.getChessboardPoint().getX() == 4 && chessComponent.getChessboardPoint().getX() == 4 && Math.abs(first.getChessboardPoint().getY() - chessComponent.getChessboardPoint().getY()) == 1) {
                            ((PawnChessComponent) first).judgePath = ((PawnChessComponent) first).judgePath + 1;
                            chessboard.swapPawnComponents(first, chessComponent);
                            chessboard.swapColor();
                            chessboard.swapChessComponentsJudge(first);
                            first.setSelected(false);
                            first = null;
                            if (chessboard.getCurrentPlayer().getColor() == Color.BLACK){
                                chessboard.plus(chessboard.getRound());
                            } else if (chessboard.getCurrentPlayer().getColor() == Color.WHITE){
                                chessboard.plus(chessboard.getRound() - 1);
                            }
                            chessboard.cgameFrame.RefreshStep();
                        } else {
                            chessboard.swapChessComponents(first, chessComponent);
                            chessboard.swapColor();
                            chessboard.swapChessComponentsJudge(first);
                            first.setSelected(false);
                            first = null;
                            if (chessboard.getCurrentPlayer().getColor() == Color.BLACK){
                                chessboard.plus(chessboard.getRound());
                            } else if (chessboard.getCurrentPlayer().getColor() == Color.WHITE){
                                chessboard.plus(chessboard.getRound() - 1);
                            }
                            chessboard.cgameFrame.RefreshStep();
                        }
                    } else if (first.getChessColor() == ChessColor.WHITE) {
                        if (chessComponent.getChessboardPoint().getX() == 0) {
                            chessboard.swapPawnChangeComponents(first,chessComponent);
                            chessboard.swapColor();
                            chessboard.swapChessComponentsJudge(first);
                            first.setSelected(false);
                            first = null;
                            if (chessboard.getCurrentPlayer().getColor() == Color.BLACK){
                                chessboard.plus(chessboard.getRound());
                            } else if (chessboard.getCurrentPlayer().getColor() == Color.WHITE){
                                chessboard.plus(chessboard.getRound() - 1);
                            }
                            chessboard.cgameFrame.RefreshStep();
                        } else if (first.getChessboardPoint().getX() == 3 && chessComponent.getChessboardPoint().getX() == 3 && Math.abs(first.getChessboardPoint().getY() - chessComponent.getChessboardPoint().getY()) == 1) {
                            ((PawnChessComponent) first).judgePath = ((PawnChessComponent) first).judgePath + 1;
                            chessboard.swapPawnComponents(first, chessComponent);
                            chessboard.swapColor();
                            chessboard.swapChessComponentsJudge(first);
                            first.setSelected(false);
                            first = null;
                            if (chessboard.getCurrentPlayer().getColor() == Color.BLACK){
                                chessboard.plus(chessboard.getRound());
                            } else if (chessboard.getCurrentPlayer().getColor() == Color.WHITE){
                                chessboard.plus(chessboard.getRound() - 1);
                            }
                            chessboard.cgameFrame.RefreshStep();
                        } else {
                            chessboard.swapChessComponents(first, chessComponent);
                            chessboard.swapColor();
                            chessboard.swapChessComponentsJudge(first);
                            first.setSelected(false);
                            first = null;
                            if (chessboard.getCurrentPlayer().getColor() == Color.BLACK){
                                chessboard.plus(chessboard.getRound());
                            } else if (chessboard.getCurrentPlayer().getColor() == Color.WHITE){
                                chessboard.plus(chessboard.getRound() - 1);
                            }
                            chessboard.cgameFrame.RefreshStep();
                        }
                    }
                } //兵类，并写上吃过路兵和触底升变
                else if (first instanceof KingChessComponent) {
                    ((KingChessComponent) first).KingChangeRook = ((KingChessComponent) first).KingChangeRook + 1;
                    if (first.getChessboardPoint().getY() - chessComponent.getChessboardPoint().getY() == 2) {
                        chessboard.swapKingChangeRookComponentsLeft(first, chessComponent);
                    } else if (first.getChessboardPoint().getY() - chessComponent.getChessboardPoint().getY() == -2) {
                        chessboard.swapKingChangeRookComponentsRight(first, chessComponent);
                    } else {
                        chessboard.swapChessComponents(first, chessComponent);
                    }
                    chessboard.swapColor();
                    chessboard.swapChessComponentsJudge(first);
                    first.setSelected(false);
                    first = null;
                    if (chessboard.getCurrentPlayer().getColor() == Color.BLACK){
                        chessboard.plus(chessboard.getRound());
                    }
                    else if (chessboard.getCurrentPlayer().getColor() == Color.WHITE){
                        chessboard.plus(chessboard.getRound() - 1);
                    }
                    chessboard.cgameFrame.RefreshStep();
                } //将类，王车换位
                else if (first instanceof RookChessComponent) {
                    ((RookChessComponent) first).RookChangeKing = ((RookChessComponent) first).RookChangeKing + 1;
                    if (((RookChessComponent) first).Change == 1){
                        ((RookChessComponent) first).judge = ((RookChessComponent) first).judge + 1;
                    }
                    chessboard.swapChessComponents(first, chessComponent);
                    chessboard.swapColor();
                    chessboard.swapChessComponentsJudge(first);
                    first.setSelected(false);
                    first = null;
                    if (chessboard.getCurrentPlayer().getColor() == Color.BLACK){
                        chessboard.plus(chessboard.getRound());
                    } else if (chessboard.getCurrentPlayer().getColor() == Color.WHITE){
                        chessboard.plus(chessboard.getRound() - 1);
                    }
                    chessboard.cgameFrame.RefreshStep();
                } //车类，王车换位，不用写其他的
                else {
                    if (first instanceof QueenChessComponent){
                        if (((QueenChessComponent) first).Change == 1){
                            ((QueenChessComponent) first).judge = ((QueenChessComponent) first).judge + 1;
                        }
                    } else if (first instanceof KnightChessComponent){
                        if (((KnightChessComponent) first).Change == 1){
                            ((KnightChessComponent) first).judge = ((KnightChessComponent) first).judge + 1;
                        }
                    } else if (first instanceof BishopChessComponent){
                        if (((BishopChessComponent) first).Change == 1){
                            ((BishopChessComponent) first).judge = ((BishopChessComponent) first).judge + 1;
                        }
                    }
                    chessboard.swapChessComponents(first, chessComponent);
                    chessboard.swapColor();
                    chessboard.swapChessComponentsJudge(first);
                    first.setSelected(false);
                    first = null;
                    if (chessboard.getCurrentPlayer().getColor() == Color.BLACK){
                        chessboard.plus(chessboard.getRound());
                    } else if (chessboard.getCurrentPlayer().getColor() == Color.WHITE){
                        chessboard.plus(chessboard.getRound() - 1);
                    }
                    chessboard.cgameFrame.RefreshStep();
                }//其他
            }
        }
    }

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
    public void ShowAllCanMove(ChessComponent chessComponent, boolean flag) {
        ArrayList<ChessboardPoint> list = chessComponent.getCanMoveToList(chessboard.getChessComponents(), chessComponent);
        ChessComponent[][] cp = chessboard.getChessComponents();
        for (int i = 0; i < list.size(); i++) {
            int x = list.get(i).getX();
            int y = list.get(i).getY();
            cp[x][y].setCanMove(flag);
            cp[x][y].repaint();
        }
    }
}
