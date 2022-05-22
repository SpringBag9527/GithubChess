package model;

import controller.ClickController;
import view.ChessboardPoint;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示棋盘上的空位置
 */
public class EmptySlotComponent extends ChessComponent {

    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location, ClickController listener, int size,char name,String chesspath) {
        super(chessboardPoint, location, ChessColor.NONE, listener, size,name,chesspath);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }

    @Override
    public boolean canReach(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }

    @Override
    public ArrayList<ChessboardPoint> getCanMoveToList(ChessComponent[][] chessComponents,ChessComponent chessComponent) {
        return new ArrayList<>();
    }

    @Override
    public void loadResource(String chesspath) throws IOException {
        //No resource!
    }

}
