package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BishopChessComponent extends ChessComponent{
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image BISHOP_WHITE;
    private static Image BISHOP_BLACK;
    private static String oldchesspath;
    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image bishopImage;


    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource(String chesspath) throws IOException {
        if(!oldchesspath.equals(chesspath)){
            BISHOP_WHITE = null;
            BISHOP_BLACK = null;
            oldchesspath=chesspath;
        }
        if (BISHOP_WHITE == null) {
            BISHOP_WHITE = ImageIO.read(new File("src/images/"+chesspath+"/bishop-white.png"));
        }

        if (BISHOP_BLACK == null) {
            BISHOP_BLACK = ImageIO.read(new File("src/images/"+chesspath+"/bishop-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateBishopImage(ChessColor color,String chesspath) {
        try {
            loadResource(chesspath);
            if (color == ChessColor.WHITE) {
                bishopImage = BISHOP_WHITE;
            } else if (color == ChessColor.BLACK) {
                bishopImage = BISHOP_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size,char name,String chesspath) {
        super(chessboardPoint, location, color, listener, size,name,chesspath);
        if(oldchesspath==null)
            oldchesspath=chesspath;
        initiateBishopImage(color,chesspath);
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
            if (destination.getY() - source.getY() > 0 && destination.getX() - source.getX() > 0 && Math.abs(destination.getY() - source.getY()) == Math.abs(destination.getX() - source.getX())){
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

    @Override
        public ArrayList<ChessboardPoint> getCanMoveToList(ChessComponent[][] chessComponents,ChessComponent chessComponent){
            ArrayList<ChessboardPoint> list = new ArrayList<>();
            int x = chessComponent.getChessboardPoint().getX();
            int y = chessComponent.getChessboardPoint().getY();
            ChessboardPoint getSource = chessComponent.getChessboardPoint();
        for (int i = -1;i > -7;i--){
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
        for (int i = -1;i >= -7;i--){
            if (getSource.offset(i,-i) != null){
                if (chessComponents[x+i][y-i].GetChessName() == '_'){
                    list.add(getSource.offset(i,-i));
                }
                else{
                    if (chessComponents[x+i][y-i].getChessColor() != chessComponents[x][y].getChessColor()){
                        list.add(getSource.offset(i,-i));
                    }
                    break;
                }
            }
        }
        for (int i = 1;i <= 7;i++){
            if (getSource.offset(i,-i) != null){
                if (chessComponents[x+i][y-i].GetChessName() == '_'){
                    list.add(getSource.offset(i,-i));
                }
                else{
                    if (chessComponents[x+i][y-i].getChessColor() != chessComponents[x][y].getChessColor()){
                        list.add(getSource.offset(i,-i));
                    }
                    break;
                }
            }
        }
        for (int i = 1;i <= 7;i++){
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
        g.drawImage(bishopImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
