package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PawnChessComponent extends ChessComponent{
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image PAWN_WHITE;
    private static Image PAWN_BLACK;
    private static String oldchesspath;
    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image pawnImage;


    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource(String chesspath) throws IOException {
        if(!oldchesspath.equals(chesspath)){
            PAWN_WHITE = null;
            PAWN_BLACK = null;
            oldchesspath=chesspath;
        }
        if (PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("src/images/"+chesspath+"/pawn-white.png"));
        }

        if (PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("src/images/"+chesspath+"/pawn-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiatePawnImage(ChessColor color,String chesspath) {
        try {
            loadResource(chesspath);
            if (color == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            } else if (color == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size,char name,String chesspath) {
        super(chessboardPoint, location, color, listener, size,name,chesspath);
        if(oldchesspath==null)
            oldchesspath=chesspath;
        initiatePawnImage(color,chesspath);
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
        ChessColor a = getChessColor();
        if (a == ChessColor.WHITE){
            if (destination.getY() - source.getY() == 0){
                if (source.getX() == 6){
                    if (source.getX() - destination.getX() == 2){
                        return chessComponents[destination.getX() + 1][destination.getY()] instanceof EmptySlotComponent && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
                    } else if (source.getX() - destination.getX() == 1) {
                        return chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
                    }
                } else {
                    if (source.getX() - destination.getX() == 1) {
                        return chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
                    }
                }
            } else if (destination.getX() - source.getX() == -1 && Math.abs(destination.getY() - source.getY()) == 1){
                return !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
            }
        } else if (a == ChessColor.BLACK){
            if (destination.getY() - source.getY() == 0) {
                if (source.getX() == 1) {
                    if (source.getX() - destination.getX() == -2){
                        return chessComponents[destination.getX() - 1][destination.getY()] instanceof EmptySlotComponent && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
                    } else if (source.getX() - destination.getX() == -1) {
                        return chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
                    }
                } else {
                    if (source.getX() - destination.getX() == -1) {
                        return chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
                    }
                }
            } else if (destination.getX() - source.getX() == 1 && Math.abs(destination.getY() - source.getY()) == 1){
                return !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
            }
        }
        return false;
    }

    @Override
    public boolean canReach(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        ChessColor a = getChessColor();
        if (a == ChessColor.WHITE){
            if (destination.getY() - source.getY() == 0){
                if (source.getX() == 6){
                    if (source.getX() - destination.getX() == 2){
                        return !(chessComponents[destination.getX() + 1][destination.getY()] instanceof EmptySlotComponent) && !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
                    } else if (source.getX() - destination.getX() == 1) {
                        return !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
                    }
                } else {
                    if (source.getX() - destination.getX() == 1) {
                        return !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
                    }
                }
            } else if (destination.getX() - source.getX() == -1 && Math.abs(destination.getY() - source.getY()) == 1){
                return chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
            }
        } else if (a == ChessColor.BLACK){
            if (destination.getY() - source.getY() == 0) {
                if (source.getX() == 1) {
                    if (source.getX() - destination.getX() == -2){
                        return !(chessComponents[destination.getX() - 1][destination.getY()] instanceof EmptySlotComponent) && !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
                    } else if (source.getX() - destination.getX() == -1) {
                        return !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
                    }
                } else {
                    if (source.getX() - destination.getX() == -1) {
                        return !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent);
                    }
                }
            } else if (destination.getX() - source.getX() == 1 && Math.abs(destination.getY() - source.getY()) == 1){
                return chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent;
            }
        }
        return false;
    }

    public ArrayList<ChessboardPoint> getCanMoveToList(ChessComponent[][] chessComponents, ChessComponent chessComponent){
        ArrayList<ChessboardPoint> list = new ArrayList<>();
        ChessboardPoint getSource = chessComponent.getChessboardPoint();
        int x = getSource.getX();
        int y = getSource.getY();

        if (getChessColor() == ChessColor.BLACK && getSource.getX() == 1) {
            if (getSource.offset(1,0) != null){
                if (chessComponents[x + 1][y].GetChessName() == '_') {
                    list.add(getSource.offset(1, 0));
                    if (getSource.offset(2,0) != null){
                        if (chessComponents[x + 2][y].GetChessName() == '_') {
                            list.add(getSource.offset(2, 0));
                        }
                    }
                }
            }
            for (int i = -1; i < 2; i += 2) {
                if (getSource.offset(1,i) != null){
                    if (chessComponents[x + 1][y + i].GetChessName() != '_' && chessComponents[x + 1][y + i].getChessColor() != chessComponents[x][y].getChessColor()) {
                        list.add(getSource.offset(1, i));
                    }
                }
            }

        } else if (getChessColor() == ChessColor.BLACK && getSource.getX() != 1) {
            if (getSource.offset(1,0) != null){
                if (chessComponents[x + 1][y].GetChessName() == '_') {
                    list.add(getSource.offset(1, 0));
                }
            }
            for (int i = -1; i < 2; i += 2) {
                if (getSource.offset(1,i) != null){
                    if (chessComponents[x + 1][y + i].GetChessName() != '_' && chessComponents[x + 1][y + i].getChessColor() != chessComponents[x][y].getChessColor()) {
                        list.add(getSource.offset(1, i));
                    }
                }
            }
        } else if (getChessColor() == ChessColor.WHITE && getSource.getX() == 6) {
            if (getSource.offset(-1,0) != null){
                if (chessComponents[x - 1][y].GetChessName() == '_') {
                    list.add(getSource.offset(-1, 0));
                    if (getSource.offset(-2,0) != null){
                        if (chessComponents[x - 2][y].GetChessName() == '_') {
                            list.add(getSource.offset(-2, 0));
                        }
                    }
                }
            }
            for (int i = -1; i < 2; i += 2) {
                if (getSource.offset(-1,i) != null){
                    if (chessComponents[x - 1][y + i].GetChessName() != '_' && chessComponents[x - 1][y + i].getChessColor() != chessComponents[x][y].getChessColor()) {
                        list.add(getSource.offset(-1, i));
                    }
                }
            }
        }

        else {
            if (getSource.offset(-1,0) != null){
                if (chessComponents[x - 1][y].GetChessName() == '_') {
                    list.add(getSource.offset(-1, 0));
                }
            }
            for (int i = -1; i < 2; i += 2) {
                if (getSource.offset(-1,i) != null){
                    if (chessComponents[x - 1][y + i].GetChessName() != '_' && chessComponents[x - 1][y + i].getChessColor() != chessComponents[x][y].getChessColor()) {
                        list.add(getSource.offset(-1, i));
                    }
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
        g.drawImage(pawnImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
