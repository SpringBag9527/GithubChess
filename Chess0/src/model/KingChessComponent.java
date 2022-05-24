package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KingChessComponent extends ChessComponent{
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image KING_WHITE;
    private static Image KING_BLACK;
    private static String oldchesspath;
    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image kingImage;


    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource(String chesspath) throws IOException {
        if(!oldchesspath.equals(chesspath)){
            KING_WHITE = null;
            KING_BLACK = null;
            oldchesspath=chesspath;
        }
        if (KING_WHITE == null) {
            KING_WHITE = ImageIO.read(new File("src/images/"+ chesspath +"/king-white.png"));
        }
        if (KING_BLACK == null) {
            KING_BLACK = ImageIO.read(new File("src/images/"+ chesspath +"/king-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateKingImage(ChessColor color,String chesspath) {
        try {
            loadResource(chesspath);
            if (color == ChessColor.WHITE) {
                kingImage = KING_WHITE;
            } else if (color == ChessColor.BLACK) {
                kingImage = KING_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size,char name,String chesspath) {
        super(chessboardPoint, location, color, listener, size,name,chesspath);
        if(oldchesspath==null)
            oldchesspath=chesspath;
        initiateKingImage(color,chesspath);
    }

    /**
     * 车棋子的移动规则
     *
     * @param chessComponents 棋盘
     * @param destination     目标位置，如(0, 0), (0, 7)等等
     * @return 车棋子移动的合法性
     */
    public int KingChangeRook = 0;
    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        ChessColor color = getChessColor();
        Boolean a = false;
        if (Math.abs(destination.getX() - source.getX()) <= 1 && Math.abs(destination.getY() - source.getY()) <= 1){
            a = !CheckMate(chessComponents, chessComponents[destination.getX()][destination.getY()], color);
        } else
        if (chessComponents[source.getX()][source.getY()] instanceof KingChessComponent && ((KingChessComponent) chessComponents[source.getX()][source.getY()]).KingChangeRook == 0){
            if (chessComponents[source.getX()][0] instanceof RookChessComponent && ((RookChessComponent) chessComponents[source.getX()][0]).RookChangeKing == 0){
                if (destination.getY() - source.getY() == -2 && Math.abs(destination.getX() - source.getX()) == 0){
                    a = !CheckMate(chessComponents, chessComponents[source.getX()][source.getY()], color) &&
                            !CheckMate(chessComponents, chessComponents[destination.getX()][destination.getY()], color) &&
                            !CheckMate(chessComponents, chessComponents[destination.getX()][destination.getY() + 1], color) &&
                            chessComponents[destination.getX()][destination.getY() + 1] instanceof EmptySlotComponent;
                }
            }
            if (chessComponents[source.getX()][7] instanceof RookChessComponent && ((RookChessComponent) chessComponents[source.getX()][7]).RookChangeKing == 0){
                if (destination.getY() - source.getY() == 2 && Math.abs(destination.getX() - source.getX()) == 0){
                    a = !CheckMate(chessComponents, chessComponents[source.getX()][source.getY()], color) &&
                            !CheckMate(chessComponents, chessComponents[destination.getX()][destination.getY()], color) &&
                            !CheckMate(chessComponents, chessComponents[destination.getX()][destination.getY() - 1], color) &&
                            chessComponents[destination.getX()][destination.getY() - 1] instanceof EmptySlotComponent;
                }
            }
        }

        return a;
    }

    @Override
    public boolean canReach(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }
    public boolean CheckMate(ChessComponent[][] chessComponent , ChessComponent destination , ChessColor color){
        boolean kingDanger = false;
        int x = 0, y = 0;
        int pre = 0;
        if (color == ChessColor.BLACK){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && chessComponent[i][j] instanceof KingChessComponent) {
                        x = i;
                        y = j;
                    }
                }
            }
            for (int i = 0 ; i < 8 ; i++){
                for (int j = 0 ; j < 8 ; j++){
                    if (chessComponent[i][j].getChessColor() == ChessColor.WHITE){
                        if (chessComponent[i][j] instanceof PawnChessComponent && chessComponent[i][j].canMoveTo(chessComponent,chessComponent[x][y].getChessboardPoint())){
                            kingDanger = false;
                        } else if (chessComponent[i][j] instanceof PawnChessComponent && !(chessComponent[i][j].canMoveTo(chessComponent,chessComponent[x][y].getChessboardPoint()))){
                            if (chessComponent[i][j].canMoveTo(chessComponent,destination.getChessboardPoint())){
                                if (destination.getChessColor() == ChessColor.WHITE){
                                    kingDanger = true;
                                    pre = pre + 1;
                                    break;
                                } else if (destination instanceof EmptySlotComponent){
                                    kingDanger = false;
                                } else {
                                    kingDanger = true;
                                    pre = pre + 1;
                                    break;
                                }
                            } else {
                                if (chessComponent[i][j].canReach(chessComponent,destination.getChessboardPoint())) {
                                    if (destination instanceof EmptySlotComponent) {
                                        kingDanger = true;
                                        pre = pre + 1;
                                        break;
                                    } else {
                                        kingDanger = false;
                                    }
                                } else {
                                    kingDanger = false;
                                }
                            }
                        } else if (chessComponent[i][j].canMoveTo(chessComponent,destination.getChessboardPoint())){
                            pre = pre + 1;
                            break;
                        }
                    }
                }
            }
        } else if (color == ChessColor.WHITE){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && chessComponent[i][j] instanceof KingChessComponent) {
                        x = i;
                        y = j;
                    }
                }
            }
            for (int i = 0 ; i < 8 ; i++){
                for (int j = 0 ; j < 8 ; j++){
                    if (chessComponent[i][j].getChessColor() == ChessColor.BLACK){
                        if (chessComponent[i][j] instanceof PawnChessComponent && chessComponent[i][j].canMoveTo(chessComponent,chessComponent[x][y].getChessboardPoint())){
                            kingDanger = false;
                        } else if (chessComponent[i][j] instanceof PawnChessComponent && !(chessComponent[i][j].canMoveTo(chessComponent,chessComponent[x][y].getChessboardPoint()))){
                            if (chessComponent[i][j].canMoveTo(chessComponent,destination.getChessboardPoint())){
                                if (destination.getChessColor() == ChessColor.BLACK){
                                    kingDanger = true;
                                    pre = pre + 1;
                                    break;
                                } else if (destination instanceof EmptySlotComponent){
                                    kingDanger = false;
                                } else {
                                    kingDanger = true;
                                    pre = pre + 1;
                                    break;
                                }
                            } else {
                                if (chessComponent[i][j].canReach(chessComponent,destination.getChessboardPoint())) {
                                    if (destination instanceof EmptySlotComponent) {
                                        kingDanger = true;
                                        pre = pre + 1;
                                        break;
                                    } else {
                                        kingDanger = false;
                                    }
                                } else {
                                    kingDanger = false;
                                }
                            }
                        } else if (chessComponent[i][j].canMoveTo(chessComponent,destination.getChessboardPoint())){
                            pre = pre + 1;
                            break;
                        }
                    }
                }
            }
        }
        return pre > 0;
    }

    public ArrayList<ChessboardPoint> getCanMoveToList(ChessComponent[][] chessComponents,ChessComponent chessComponent) {
        ArrayList<ChessboardPoint> list = new ArrayList<>();
        ChessboardPoint getSource = chessComponent.getChessboardPoint();
        int x = getSource.getX();
        int y = getSource.getY();

        if (getSource.offset(0,-1) != null){
            if (chessComponents[x][y-1].getChessColor() != chessComponents[x][y].getChessColor()){
                list.add(getSource.offset(0,-1));
            }
        }
        if (getSource.offset(0,1) != null){
            if (chessComponents[x][y+1].getChessColor() != chessComponents[x][y].getChessColor()){
                list.add(getSource.offset(0,1));
            }
        }
        for (int j = -1;j < 2;j++){
            if (getSource.offset(-1,j) != null){
                if (chessComponents[x-1][y+j].getChessColor() != chessComponents[x][y].getChessColor()){
                    list.add(getSource.offset(-1,j));
                }
            }
        }
        for (int j = -1;j < 2;j++){
            if (getSource.offset(1,j) != null){
                if (chessComponents[x+1][y+j].getChessColor() != chessComponents[x][y].getChessColor()){
                    list.add(getSource.offset(1,j));
                }
            }
        }
        return list;
    }

    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(kingImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
