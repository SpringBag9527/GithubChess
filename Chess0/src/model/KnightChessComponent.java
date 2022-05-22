package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KnightChessComponent extends ChessComponent{
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image KNIGHT_WHITE;
    private static Image KNIGHT_BLACK;
    private static String oldchesspath;
    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image knightImage;


    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource(String chesspath) throws IOException {
        if(!oldchesspath.equals(chesspath)){
            KNIGHT_WHITE = null;
            KNIGHT_BLACK = null;
            oldchesspath=chesspath;
        }
        if (KNIGHT_WHITE == null) {
            KNIGHT_WHITE = ImageIO.read(new File("src/images/"+chesspath+"/knight-white.png"));
        }

        if (KNIGHT_BLACK == null) {
            KNIGHT_BLACK = ImageIO.read(new File("src/images/"+chesspath+"/knight-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateKnightImage(ChessColor color,String chesspath) {
        try {
            loadResource(chesspath);
            if (color == ChessColor.WHITE) {
                knightImage = KNIGHT_WHITE;
            } else if (color == ChessColor.BLACK) {
                knightImage = KNIGHT_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KnightChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size,char name,String chesspath) {
        super(chessboardPoint, location, color, listener, size,name,chesspath);
        if(oldchesspath==null)
            oldchesspath=chesspath;
        initiateKnightImage(color,chesspath);
    }

    /**
     * 车棋子的移动规则
     *
     * @param chessComponents 棋盘
     * @param destination     目标位置，如(0, 0), (0, 7)等等
     * @return 车棋子移动的合法性
     */

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (Math.abs(destination.getX() - source.getX()) == 1 && Math.abs(destination.getY() - source.getY()) == 2){
            return true;
        } else return Math.abs(destination.getX() - source.getX()) == 2 && Math.abs(destination.getY() - source.getY()) == 1;
    }

    @Override
    public boolean canReach(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }

    public ArrayList<ChessboardPoint> getCanMoveToList(ChessComponent[][] chessComponents, ChessComponent chessComponent){
        ArrayList<ChessboardPoint> list = new ArrayList<>();
        ChessboardPoint getSource = chessComponent.getChessboardPoint();
        int x = getSource.getX();
        int y = getSource.getY();

        for (int i = -2;i < 0;i++){
            if (getSource.offset(i,-(3+i)) != null){
                if (chessComponents[x+i][y-(3+i)].getChessColor() != chessComponents[x][y].getChessColor()){
                    list.add(getSource.offset(i,-(3+i)));
                }
            }
            if (getSource.offset(i,(3+i)) != null){
                if (chessComponents[x+i][y+(3+i)].getChessColor() != chessComponents[x][y].getChessColor()){
                    list.add(getSource.offset(i,(3+i)));
                }
            }
        }
        for (int i = 1; i < 3;i++){
            if (getSource.offset(i,-(3-i)) != null){
                if (chessComponents[x+i][y-(3-i)].getChessColor() != chessComponents[x][y].getChessColor()){
                    list.add(getSource.offset(i,-(3-i)));
                }
            }
            if (getSource.offset(i,(3-i)) != null){
                if (chessComponents[x+i][y+(3-i)].getChessColor() != chessComponents[x][y].getChessColor()){
                    list.add(getSource.offset(i,(3-i)));
                }
            }
        }
        return list;
    }


    public List<ChessboardPoint> canMoveTo(ChessComponent[][] chessComponents,ChessComponent chessComponent){
        ArrayList<ChessboardPoint> list = new ArrayList<>();
        ChessboardPoint getSource = chessComponent.getChessboardPoint();
        int x = getSource.getX();
        int y = getSource.getY();
        for (int i = -2;i < 0;i++){
            if (getSource.offset(i,-(3+i)) != null){
                if (chessComponents[x+i][y-(3+i)].getChessColor() != chessComponents[x][y].getChessColor()){
                    list.add(getSource.offset(i,-(3+i)));
                }
            }
            if (getSource.offset(i,(3+i)) != null){
                if (chessComponents[x+i][y+(3+i)].getChessColor() != chessComponents[x][y].getChessColor()){
                    list.add(getSource.offset(i,(3+i)));
                }
            }
        }
        for (int i = 1; i < 3;i++){
            if (getSource.offset(i,-(3-i)) != null){
                if (chessComponents[x+i][y-(3-i)].getChessColor() != chessComponents[x][y].getChessColor()){
                    list.add(getSource.offset(i,-(3-i)));
                }
            }
            if (getSource.offset(i,(3-i)) != null){
                if (chessComponents[x+i][y+(3-i)].getChessColor() != chessComponents[x][y].getChessColor()){
                    list.add(getSource.offset(i,(3-i)));
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
        g.drawImage(knightImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
