package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueenChessComponent extends ChessComponent{
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image QUEEN_WHITE;
    private static Image QUEEN_BLACK;
    private static String oldchesspath;
    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image queenImage;


    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource(String chesspath) throws IOException {
        if(!oldchesspath.equals(chesspath)){
            QUEEN_WHITE = null;
            QUEEN_BLACK = null;
            oldchesspath=chesspath;
        }
        if (QUEEN_WHITE == null) {
            QUEEN_WHITE = ImageIO.read(new File("src/images/"+chesspath+"/queen-white.png"));
        }

        if (QUEEN_BLACK == null) {
            QUEEN_BLACK = ImageIO.read(new File("src/images/"+chesspath+"/queen-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateQueenImage(ChessColor color,String chesspath) {
        try {
            loadResource(chesspath);
            if (color == ChessColor.WHITE) {
                queenImage = QUEEN_WHITE;
            } else if (color == ChessColor.BLACK) {
                queenImage = QUEEN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public QueenChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size,char name,String chesspath) {
        super(chessboardPoint, location, color, listener, size,name,chesspath);
        if(oldchesspath==null)
            oldchesspath=chesspath;
        initiateQueenImage(color,chesspath);
    }

    /**
     * 车棋子的移动规则
     *
     * @param chessComponents 棋盘
     * @param destination     目标位置，如(0, 0), (0, 7)等等
     * @return 车棋子移动的合法性
     */
    public int judge = 0;
    public int Change = 0;
    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        ChessColor color = getChessColor();
        if (source.getX() == destination.getX() && source.getY() == destination.getY()){ // Not on the same row or the same column.
            return false;
        } else if (source.getX() == destination.getX()) {
            int row = source.getX();
            for (int col = Math.min(source.getY(), destination.getY()) + 1;
                 col < Math.max(source.getY(), destination.getY()); col++) {
                if (chessComponents[row][col] instanceof EmptySlotComponent || chessComponents[row][col] instanceof KingChessComponent) {
                    if (chessComponents[row][col] instanceof KingChessComponent && color == chessComponents[row][col].getChessColor()){
                        return false;
                    }
                } else {
                    return false;
                }
            }return true;
        } else if (source.getY() == destination.getY()) {
            int col = source.getY();
            for (int row = Math.min(source.getX(), destination.getX()) + 1;
                 row < Math.max(source.getX(), destination.getX()); row++) {
                if (chessComponents[row][col] instanceof EmptySlotComponent || chessComponents[row][col] instanceof KingChessComponent) {
                    if (chessComponents[row][col] instanceof KingChessComponent && color == chessComponents[row][col].getChessColor()){
                        return false;
                    }
                }else{
                    return false;
                }
            }return true;
        }  else if (destination.getY() - source.getY() > 0 && destination.getX() - source.getX() > 0 && Math.abs(destination.getY() - source.getY()) == Math.abs(destination.getX() - source.getX())){
            int row0 = source.getX() + 1;
            int col0 = source.getY() + 1;
            while (row0 < destination.getX()) {
                if ((chessComponents[row0][col0] instanceof EmptySlotComponent || chessComponents[row0][col0] instanceof KingChessComponent)) {
                    if (chessComponents[row0][col0] instanceof KingChessComponent && color == chessComponents[row0][col0].getChessColor()){
                        return false;
                    }
                } else {
                    return false;
                }
                row0++;
                col0++;
            }
            return true;
        } else if (destination.getY() - source.getY() > 0 && destination.getX() - source.getX() < 0 && Math.abs(destination.getY() - source.getY()) == Math.abs(destination.getX() - source.getX())){
            int row0 = source.getX() - 1;
            int col0 = source.getY() + 1;
            while (col0 < destination.getY()) {
                if ((chessComponents[row0][col0] instanceof EmptySlotComponent || chessComponents[row0][col0] instanceof KingChessComponent)) {
                    if (chessComponents[row0][col0] instanceof KingChessComponent && color == chessComponents[row0][col0].getChessColor()){
                        return false;
                    }
                } else {
                    return false;
                }
                row0--;
                col0++;
            }return true;
        } else if (destination.getY() - source.getY() < 0 && destination.getX() - source.getX() > 0 && Math.abs(destination.getY() - source.getY()) == Math.abs(destination.getX() - source.getX())){
            int row0 = destination.getX() - 1;
            int col0 = destination.getY() + 1;
            while (row0 > source.getX()) {
                if ((chessComponents[row0][col0] instanceof EmptySlotComponent || chessComponents[row0][col0] instanceof KingChessComponent)) {
                    if (chessComponents[row0][col0] instanceof KingChessComponent && color == chessComponents[row0][col0].getChessColor()){
                        return false;
                    }
                } else {
                    return false;
                }
                row0--;
                col0++;
            }return true;
        } else if (destination.getY() - source.getY() < 0 && destination.getX() - source.getX() < 0 && Math.abs(destination.getY() - source.getY()) == Math.abs(destination.getX() - source.getX())){
            int row0 = destination.getX() + 1;
            int col0 = destination.getY() + 1;
            while (row0 < source.getX()) {
                if ((chessComponents[row0][col0] instanceof EmptySlotComponent || chessComponents[row0][col0] instanceof KingChessComponent)) {
                    if (chessComponents[row0][col0] instanceof KingChessComponent && color == chessComponents[row0][col0].getChessColor()){
                        return false;
                    }
                } else {
                    return false;
                }
                row0++;
                col0++;
            }return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canReach(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }

    public ArrayList<ChessboardPoint> getCanMoveToList(ChessComponent[][] chessComponents,ChessComponent chessComponent){
        ArrayList<ChessboardPoint> list = new ArrayList<>();
        ChessboardPoint getSource = chessComponent.getChessboardPoint();
        int x = getSource.getX();
        int y = getSource.getY();
        //左上
        for (int i = -1;i >= -7;i--){
            if (getSource.offset(i,i) != null){
                if (chessComponents[x+i][y+i].GetChessName() == '_'){
                    list.add(getSource.offset(i,i));
                }
                else{
                    if (chessComponents[x+i][y+i].getChessColor() != chessComponents[x][y].getChessColor()){
                        list.add(getSource.offset(i,i));
                    }
                    break;
                }
            }
        }
        //上
        for (int i = -1;i >= -7;i--){
            if (getSource.offset(i,0) != null){
                if (chessComponents[x+i][y].GetChessName() == '_'){
                    list.add(getSource.offset(i,0));
                }
                else{
                    if (chessComponents[x+i][y].getChessColor() != chessComponents[x][y].getChessColor()){
                        list.add(getSource.offset(i,0));
                    }
                    break;
                }
            }
        }
        //右上
        for (int i = -1;i >= -7;i--) {
            if (getSource.offset(i,-i) != null){
                if (chessComponents[x + i][y - i].GetChessName() == '_') {
                    list.add(getSource.offset(i, -i));
                } else {
                    if (chessComponents[x + i][y - i].getChessColor() != chessComponents[x][y].getChessColor()) {
                        list.add(getSource.offset(i, -i));
                    }
                    break;
                }
            }
        }
        //左下
        for (int i = -1;i >= -7;i--) {
            if (getSource.offset(-i,i) != null){
                if (chessComponents[x - i][y + i].GetChessName() == '_') {
                    list.add(getSource.offset(-i, i));
                } else {
                    if (chessComponents[x - i][y + i].getChessColor() != chessComponents[x][y].getChessColor()) {
                        list.add(getSource.offset(-i, i));
                    }
                    break;
                }
            }
        }
        //下
        for (int i = -1;i >= -7;i--) {
            if (getSource.offset(-i,0) != null){
                if (chessComponents[x - i][y].GetChessName() == '_') {
                    list.add(getSource.offset(-i, 0));
                } else {
                    if (chessComponents[x - i][y].getChessColor() != chessComponents[x][y].getChessColor()) {
                        list.add(getSource.offset(-i, 0));
                    }
                    break;
                }
            }
        }
        //右下
        for (int i = -1;i >= -7;i--) {
            if (getSource.offset(-i,-i) != null){
                if (chessComponents[x - i][y - i].GetChessName() == '_') {
                    list.add(getSource.offset(-i, -i));
                } else {
                    if (chessComponents[x - i][y - i].getChessColor() != chessComponents[x][y].getChessColor()) {
                        list.add(getSource.offset(-i, -i));
                    }
                    break;
                }
            }
        }
        //左
        for (int i = -1;i >= -7;i--) {
            if (getSource.offset(0,i) != null){
                if (chessComponents[x][y + i].GetChessName() == '_') {
                    list.add(getSource.offset(0, i));
                } else {
                    if (chessComponents[x][y + i].getChessColor() != chessComponents[x][y].getChessColor()) {
                        list.add(getSource.offset(0, i));
                    }
                    break;
                }
            }
        }
        //右
        for (int i = -1;i >= -7;i--) {
            if (getSource.offset(0,-i) != null){
                if (chessComponents[x][y - i].GetChessName() == '_') {
                    list.add(getSource.offset(0, -i));
                } else {
                    if (chessComponents[x][y - i].getChessColor() != chessComponents[x][y].getChessColor()) {
                        list.add(getSource.offset(0, -i));
                    }
                    break;
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
        g.drawImage(queenImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
