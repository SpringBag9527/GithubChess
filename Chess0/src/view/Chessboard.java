package view;

//import com.sun.org.apache.xpath.internal.operations.String;

import controller.ClickController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Chessboard extends JComponent {

    /**
     * CHESSBOARD_SIZE： 棋盘是8 * 8的
     * <br>
     * BACKGROUND_COLORS: 棋盘的两种背景颜色
     * <br>
     * chessListener：棋盘监听棋子的行动
     * <br>
     * chessboard: 表示8 * 8的棋盘
     * <br>
     * currentColor: 当前行棋方
     */
    private final int CHESSBOARD_SIZE = 8;

    public final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    //private ChessColor currentColor = ChessColor.WHITE;
    private ChessColor currentPlayer = ChessColor.WHITE;
    private int currentRound = 1;      //当前回合数
    public final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    public ChessGameFrame cgameFrame;
    private String ChessPath = "chess1";
    private String userNameWhite = "";
    private String userNameBlack = "";
    private Stack<String> stepList = new Stack<>();

    public Stack<String> getStepList() {
        return stepList;
    }

    public String getUserNameWhite() {
        return userNameWhite;
    }

    public String getUserNameBlack() {
        return userNameBlack;
    }

    public void setChessPath(String chesspath) {
        this.ChessPath = chesspath;
    }

    public Chessboard(ChessGameFrame chessgameframe, int width, int height) {
        this.cgameFrame = chessgameframe;
        setLayout(null);
        setSize(width + 16, height + 16);
        CHESS_SIZE = width / 8;
        repaint();           //????
        //System.out.printf("chessboard size = %d,chess size = %d\n",width,CHESS_SIZE);

        initiateEmptyChessboard();//初始化空棋盘
    }

    public int getRound() {
        return (this.currentRound);
    }

    public String getSide() {
        return (currentPlayer.toString());
    }
    
    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE, '_', ChessPath));
            }
        }
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }

    private void initRookOnBoard(int row, int col, ChessColor color, char name) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, name, ChessPath);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initKnightOnBoard(int row, int col, ChessColor color, char name) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, name, ChessPath);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initBishopOnBoard(int row, int col, ChessColor color, char name) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, name, ChessPath);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initQueenOnBoard(int row, int col, ChessColor color, char name) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, name, ChessPath);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initKingOnBoard(int row, int col, ChessColor color, char name) {
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, name, ChessPath);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initPawnOnBoard(int row, int col, ChessColor color, char name) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE, name, ChessPath);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        addStepList(chess1, chess2);
        int i = 0;
        int j = 0;
        if (!(chess2 instanceof EmptySlotComponent)) {
            cgameFrame.BackGroundSound.play("kill");
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE, '_', ChessPath));
        }
        else
            cgameFrame.BackGroundSound.play("move");
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;
        if (CheckMate(chessComponents, chess1)) {
            i = 1;
        }
        if (CheckKilling(chessComponents, chess1)) {
            j = 1;
        }
        if (j == 1) {
            CheckMateKilling();
        } else if (i == 1) {
            CheckMateWaning();
        }
        chess2.repaint();
        chess1.repaint();
    }

    public void swapChessComponentsJudge(ChessComponent chess2) {
        if (CheckMateFan(chessComponents,chess2)){
            RegretStep(1);
            CheckMateWaning1();
        }
    }

    public boolean CheckMate(ChessComponent[][] chessComponent, ChessComponent chess2) {
        boolean kingDanger = false;
        int x = 0, y = 0;
        if (chess2.getChessColor() == ChessColor.WHITE) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && chessComponent[i][j] instanceof KingChessComponent) {
                        x = i;
                        y = j;
                    }
                }
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponent[i][j].getChessColor() == ChessColor.WHITE) {
                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[x][y].getChessboardPoint())) {
                            kingDanger = true;
                            break;
                        }
                    }
                }
            }
        } else if (chess2.getChessColor() == ChessColor.BLACK) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && chessComponent[i][j] instanceof KingChessComponent) {
                        x = i;
                        y = j;
                    }
                }
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponent[i][j].getChessColor() == ChessColor.BLACK) {
                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[x][y].getChessboardPoint())) {
                            kingDanger = true;
                            break;
                        }
                    }
                }
            }
        }
        return kingDanger;
    }

    public boolean CheckMateFan(ChessComponent[][] chessComponent, ChessComponent chess2) {
        boolean kingDanger = false;
        int x = 0, y = 0;
        if (chess2.getChessColor() == ChessColor.BLACK) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && chessComponent[i][j] instanceof KingChessComponent) {
                        x = i;
                        y = j;
                    }
                }
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponent[i][j].getChessColor() == ChessColor.WHITE) {
                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[x][y].getChessboardPoint())) {
                            kingDanger = true;
                            break;
                        }
                    }
                }
            }
        } else if (chess2.getChessColor() == ChessColor.WHITE) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && chessComponent[i][j] instanceof KingChessComponent) {
                        x = i;
                        y = j;
                    }
                }
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponent[i][j].getChessColor() == ChessColor.BLACK) {
                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[x][y].getChessboardPoint())) {
                            kingDanger = true;
                            break;
                        }
                    }
                }
            }
        }
        return kingDanger;
    }

    public boolean CheckKilling(ChessComponent[][] chessComponent, ChessComponent chess2) {
        return CheckKilling1(chessComponent, chess2) && CheckKilling2(chessComponent, chess2);
    }

    public boolean CheckKilling1(ChessComponent[][] chessComponent, ChessComponent chess2) {
        boolean Kill = false;
        int x = 0, y = 0;
        int pre = 0;
        if (CheckMate(chessComponent, chess2)) {
            if (chess2.getChessColor() == ChessColor.WHITE) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && chessComponent[i][j] instanceof KingChessComponent) {
                            x = i;
                            y = j;
                        }
                    }
                }
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chessComponent[x][y].canMoveTo(chessComponent, chessComponent[i][j].getChessboardPoint())) {
                            if (!(chessComponent[i][j].getChessColor() == chessComponent[x][y].getChessColor()) || chessComponent[i][j] instanceof EmptySlotComponent) {
                                pre++;
                            }
                        }
                    }
                }
                if (pre == 0) {
                    Kill = true;
                }
            } else if (chess2.getChessColor() == ChessColor.BLACK) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && chessComponent[i][j] instanceof KingChessComponent) {
                            x = i;
                            y = j;
                        }
                    }
                }
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chessComponent[x][y].canMoveTo(chessComponent, chessComponent[i][j].getChessboardPoint())) {
                            if (!(chessComponent[i][j].getChessColor() == chessComponent[x][y].getChessColor()) || chessComponent[i][j] instanceof EmptySlotComponent) {
                                pre++;
                            }
                        }
                    }
                }
                if (pre == 0) {
                    Kill = true;
                }
            }
        }
        return Kill;
    }

    public boolean CheckKilling2(ChessComponent[][] chessComponent, ChessComponent chess2) {
        Boolean King = true;
        int x = 0, y = 0;
        int q = 0, p = 0;
        int pre = 0;
        if (CheckMate(chessComponent, chess2)) {
            if (chess2.getChessColor() == ChessColor.WHITE) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && chessComponent[i][j] instanceof KingChessComponent) {
                            x = i;
                            y = j;
                        }
                    }
                }//扫描出黑王
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[x][y].getChessboardPoint())) {
                            q = i;
                            p = j;
                            pre++;
                        }
                    }
                }//找出将他的白棋子
                if (chessComponent[q][p] instanceof KnightChessComponent) {
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint())) {
                                King = false;
                            }
                        }
                    }//马解将军判断
                } else if (chessComponent[q][p] instanceof PawnChessComponent) {
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint())) {
                                King = false;
                            }
                        }
                    }//兵解将军判断
                } else if (chessComponent[q][p] instanceof BishopChessComponent) {
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint())) {
                                King = false;
                            } else if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && !(chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint()))) {
                                if (x - q > 0 && y - p > 0 && Math.abs(q - x) == Math.abs(p - y)) {
                                    int row0 = q + 1;
                                    int col0 = p + 1;
                                    while (row0 < x) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                            King = false;
                                        }
                                        row0++;
                                        col0++;
                                    }
                                } else if (y - p > 0 && x - q < 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                    int row0 = q - 1;
                                    int col0 = p + 1;
                                    while (col0 < y) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                            King = false;
                                        }
                                        row0--;
                                        col0++;
                                    }
                                } else if (y - p < 0 && x - q > 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                    int row0 = x - 1;
                                    int col0 = y + 1;
                                    while (row0 > q) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                            King = false;
                                        }
                                        row0--;
                                        col0++;
                                    }
                                } else if (y - p < 0 && x - q < 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                    int row0 = x + 1;
                                    int col0 = y + 1;
                                    while (row0 < q) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                            King = false;
                                        }
                                        row0++;
                                        col0++;
                                    }
                                }
                            }
                        }
                    }//象将军判断
                } else if (chessComponent[q][p] instanceof QueenChessComponent) {
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint())) {
                                King = false;
                            } else if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && !(chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint()))) {
                                if (q == x) {
                                    for (int m = Math.min(p, y) + 1; m < Math.max(p, y); m++) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][m].getChessboardPoint())) {
                                            King = false;
                                        }
                                    }
                                } else if (p == y) {
                                    for (int m = Math.min(q, x) + 1; m < Math.max(q, x); m++) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[m][p].getChessboardPoint())) {
                                            King = false;
                                        }
                                    }
                                } else if (x - q > 0 && y - p > 0 && Math.abs(q - x) == Math.abs(p - y)) {
                                    int row0 = q + 1;
                                    int col0 = p + 1;
                                    while (row0 < x) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                            King = false;
                                        }
                                        row0++;
                                        col0++;
                                    }
                                } else if (y - p > 0 && x - q < 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                    int row0 = q - 1;
                                    int col0 = p + 1;
                                    while (col0 < y) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                            King = false;
                                        }
                                        row0--;
                                        col0++;
                                    }
                                } else if (y - p < 0 && x - q > 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                    int row0 = x - 1;
                                    int col0 = y + 1;
                                    while (row0 > q) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                            King = false;
                                        }
                                        row0--;
                                        col0++;
                                    }
                                } else if (y - p < 0 && x - q < 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                    int row0 = x + 1;
                                    int col0 = y + 1;
                                    while (row0 < q) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                            King = false;
                                        }
                                        row0++;
                                        col0++;
                                    }
                                }
                            }
                        }
                    }//后将军判断
                } else if (chessComponent[q][p] instanceof RookChessComponent) {
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint())) {
                                King = false;
                            } else if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && !(chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint()))) {
                                if (q == x) {
                                    for (int m = Math.min(p, y) + 1; m < Math.max(p, y); m++) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][m].getChessboardPoint())) {
                                            King = false;
                                        }
                                    }
                                } else if (p == y) {
                                    for (int m = Math.min(q, x) + 1; m < Math.max(q, x); m++) {
                                        if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[m][p].getChessboardPoint())) {
                                            King = false;
                                        }
                                    }
                                }
                            }
                        }
                    }//车解将军判断
                }
                System.out.print(pre);
            } else if (chess2.getChessColor() == ChessColor.BLACK) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && chessComponent[i][j] instanceof KingChessComponent) {
                            x = i;
                            y = j;
                        }
                    }
                }//扫描出白王
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chessComponent[i][j].getChessColor() == ChessColor.BLACK && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[x][y].getChessboardPoint())) {
                            q = i;
                            p = j;
                            pre++;
                        }
                    }
                }//找出将他的黑棋子
                if (pre >= 2) {
                    King = true;
                } else if (pre == 0) {
                    King = false;
                } else {
                    if (chessComponent[q][p] instanceof KnightChessComponent) {
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint())) {
                                    King = false;
                                }
                            }
                        }//马将军判断
                    } else if (chessComponent[q][p] instanceof PawnChessComponent) {
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint())) {
                                    King = false;
                                }
                            }
                        }//兵将军判断
                    } else if (chessComponent[q][p] instanceof BishopChessComponent) {
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint())) {
                                    King = false;
                                } else if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && !(chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint()))) {
                                    if (x - q > 0 && y - p > 0 && Math.abs(q - x) == Math.abs(p - y)) {
                                        int row0 = q + 1;
                                        int col0 = p + 1;
                                        while (row0 < x) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                                King = false;
                                            }
                                            row0++;
                                            col0++;
                                        }
                                    } else if (y - p > 0 && x - q < 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                        int row0 = q - 1;
                                        int col0 = p + 1;
                                        while (col0 < y) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                                King = false;
                                            }
                                            row0--;
                                            col0++;
                                        }
                                    } else if (y - p < 0 && x - q > 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                        int row0 = x - 1;
                                        int col0 = y + 1;
                                        while (row0 > q) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                                King = false;
                                            }
                                            row0--;
                                            col0++;
                                        }
                                    } else if (y - p < 0 && x - q < 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                        int row0 = x + 1;
                                        int col0 = y + 1;
                                        while (row0 < q) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                                King = false;
                                            }
                                            row0++;
                                            col0++;
                                        }
                                    }
                                }
                            }
                        }//象将军判断
                    } else if (chessComponent[q][p] instanceof QueenChessComponent) {
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint())) {
                                    King = false;
                                } else if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && !(chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint()))) {
                                    if (q == x) {
                                        for (int m = Math.min(p, y) + 1; m < Math.max(p, y); m++) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][m].getChessboardPoint())) {
                                                King = false;
                                            }
                                        }
                                    } else if (p == y) {
                                        for (int m = Math.min(q, x) + 1; m < Math.max(q, x); m++) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[m][p].getChessboardPoint())) {
                                                King = false;
                                            }
                                        }
                                    } else if (x - q > 0 && y - p > 0 && Math.abs(q - x) == Math.abs(p - y)) {
                                        int row0 = q + 1;
                                        int col0 = p + 1;
                                        while (row0 < x) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                                King = false;
                                            }
                                            row0++;
                                            col0++;
                                        }
                                    } else if (y - p > 0 && x - q < 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                        int row0 = q - 1;
                                        int col0 = p + 1;
                                        while (col0 < y) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                                King = false;
                                            }
                                            row0--;
                                            col0++;
                                        }
                                    } else if (y - p < 0 && x - q > 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                        int row0 = x - 1;
                                        int col0 = y + 1;
                                        while (row0 > q) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                                King = false;
                                            }
                                            row0--;
                                            col0++;
                                        }
                                    } else if (y - p < 0 && x - q < 0 && Math.abs(y - p) == Math.abs(x - q)) {
                                        int row0 = x + 1;
                                        int col0 = y + 1;
                                        while (row0 < q) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[row0][col0].getChessboardPoint())) {
                                                King = false;
                                            }
                                            row0++;
                                            col0++;
                                        }
                                    }
                                }
                            }
                        }//后将军判断
                    } else if (chessComponent[q][p] instanceof RookChessComponent) {
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint())) {
                                    King = false;
                                } else if (chessComponent[i][j].getChessColor() == ChessColor.WHITE && !(chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][p].getChessboardPoint()))) {
                                    if (q == x) {
                                        for (int m = Math.min(p, y) + 1; m < Math.max(p, y); m++) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[q][m].getChessboardPoint())) {
                                                King = false;
                                            }
                                        }
                                    } else if (p == y) {
                                        for (int m = Math.min(q, x) + 1; m < Math.max(q, x); m++) {
                                            if (chessComponent[i][j].canMoveTo(chessComponent, chessComponent[m][p].getChessboardPoint())) {
                                                King = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }//车解将军判断
                    }
                }
            }
        }
        return King;
    }

    private void CheckMateWaning() {
        JOptionPane.showMessageDialog(this, "将军！");
    }

    private void CheckMateWaning1() {
        JOptionPane.showMessageDialog(this, "被将军啦傻瓜！谨慎考虑吧！！！");
    }

    private void CheckMateKilling() {
        JOptionPane.showMessageDialog(this, "绝杀！无解！！");
    }

    public void swapColor() {
        if (currentPlayer == ChessColor.BLACK) {
            currentPlayer = ChessColor.WHITE;
            cgameFrame.setDownTimes();
        } else if (currentPlayer == ChessColor.WHITE) {
            currentPlayer = ChessColor.BLACK;
            cgameFrame.setDownTimes();
        }
        if (currentPlayer == ChessColor.WHITE) {
            currentRound++;
        }
        cgameFrame.RefreshStep();
    }


    public ChessColor getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    //根据List<String>展开棋局
    public void loadGame(List<String> chessData) {
        int i;
        if (chessData.get(8).contains("b"))
            currentPlayer = ChessColor.BLACK;
        else if (chessData.get(8).contains("w"))
            currentPlayer = ChessColor.WHITE;
        currentRound = Integer.parseInt(chessData.get(9));
        for (i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (chessData.get(i).charAt(j)) {
                    case 'R':
                        initRookOnBoard(i, j, ChessColor.BLACK, 'R');
                        break;
                    case 'r':
                        initRookOnBoard(i, j, ChessColor.WHITE, 'r');
                        break;
                    case 'N':
                        initKnightOnBoard(i, j, ChessColor.BLACK, 'N');
                        break;
                    case 'n':
                        initKnightOnBoard(i, j, ChessColor.WHITE, 'n');
                        break;
                    case 'B':
                        initBishopOnBoard(i, j, ChessColor.BLACK, 'B');
                        break;
                    case 'b':
                        initBishopOnBoard(i, j, ChessColor.WHITE, 'b');
                        break;
                    case 'Q':
                        initQueenOnBoard(i, j, ChessColor.BLACK, 'Q');
                        break;
                    case 'q':
                        initQueenOnBoard(i, j, ChessColor.WHITE, 'q');
                        break;
                    case 'K':
                        initKingOnBoard(i, j, ChessColor.BLACK, 'K');
                        break;
                    case 'k':
                        initKingOnBoard(i, j, ChessColor.WHITE, 'k');
                        break;
                    case 'P':
                        initPawnOnBoard(i, j, ChessColor.BLACK, 'P');
                        break;
                    case 'p':
                        initPawnOnBoard(i, j, ChessColor.WHITE, 'p');
                        break;
                    case '_':
                        break;
                }
            }
        }
        //加载完毕棋局，再加载步骤
        //获取步骤数量

        int StepCount=Integer.parseInt(chessData.get(12));
        for(i=13;i<13+StepCount;i++){
            stepList.add(chessData.get(i));
        }
    }

    //从棋局中整理出List<String>
    public List<String> GetGameData() {
        List<String> GameDataList = new ArrayList<>();
        StringBuilder list;
        for (int i = 0; i < 8; i++) {
            list = new StringBuilder("");
            for (int j = 0; j < 8; j++) {
                list.append(chessComponents[i][j].GetChessName());
            }
            //System.out.println(list);
            GameDataList.add(list.toString());
        }
        //加入当前方
        if (currentPlayer == ChessColor.BLACK) {
            GameDataList.add("b");
        }
        else {
            GameDataList.add("w");
        }
        //加入回合数
        String s1 = Integer.toString(currentRound);
        GameDataList.add(s1);
        //加入黑白方
        s1=cgameFrame.player[0].getName() + "," + cgameFrame.player[1].getName();
        GameDataList.add(s1);
        //加入空的Mac校验位
        GameDataList.add("MAC");
        //加入Step步骤数
        int stepCount=stepList.size();
        GameDataList.add(Integer.toString(stepCount));
        for(int i=0;i<stepCount;i++)
        {
            GameDataList.add(stepList.get(i));
        }
        //注意，这里缺少最后一行走棋MAC

        return (GameDataList);
    }


    //步骤记录
    public void addStepList(ChessComponent chess1, ChessComponent chess2) {
//        //Stack<String> stepList = new Stack<>();
        String sourcePart = String.valueOf(chess1.GetChessName()) + chess1.getChessboardPoint().getX() + chess1.getChessboardPoint().getY();
        String targetPart = String.valueOf(chess2.GetChessName()) + chess2.getChessboardPoint().getX() + chess2.getChessboardPoint().getY();
        String s1;
        String s2=Integer.toString(currentRound);
        if (currentPlayer == ChessColor.BLACK) {
            s1="b";
        }
        else
            s1="w";

        stepList.push(sourcePart + targetPart+","+s1+","+s2);
    }


    public void addStepList(String s1){
        stepList.push(s1);
    }

    public int returnDownTimes() {
        return cgameFrame.getDownTimes();
    }


    public void setLocation(int i, int j) {

    }

    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE + 16, row * CHESS_SIZE + 16);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    //悔棋
    public void RegretStep(int num) {
        int i=0;
        //取出一步
        while(i<num) {
            //String step = stepList.get(stepList.size() - 1-i);
            String step=stepList.pop();
            String[] ss=step.split(",");
            String s1 = ss[0].substring(1, 2);
            String s2=ss[0].substring(3,4);
            if(s1.equals(s2)&&s1.equals("_")){
                int m=1;    //没有实际用途
            }
            else {
                int sx = Integer.parseInt(ss[0].substring(1, 2));
                int sy = Integer.parseInt(ss[0].substring(2, 3));
                int tx = Integer.parseInt(ss[0].substring(4, 5));
                int ty = Integer.parseInt(ss[0].substring(5, 6));
                ChessComponent first = chessComponents[sx][sy];
                ChessComponent second = chessComponents[tx][ty];
                RegretSwapChess(first, second);

                //有吃子，放子
                if (ss[0].charAt(3) != '_') {
                    switch (ss[0].charAt((3))) {
                        case 'R':
                            initRookOnBoard(tx, ty, ChessColor.BLACK, 'R');
                            break;
                        case 'r':
                            initRookOnBoard(tx, ty, ChessColor.WHITE, 'r');
                            break;
                        case 'N':
                            initKnightOnBoard(tx, ty, ChessColor.BLACK, 'N');
                            break;
                        case 'n':
                            initKnightOnBoard(tx, ty, ChessColor.WHITE, 'n');
                            break;
                        case 'B':
                            initBishopOnBoard(tx, ty, ChessColor.BLACK, 'B');
                            break;
                        case 'b':
                            initBishopOnBoard(tx, ty, ChessColor.WHITE, 'b');
                            break;
                        case 'Q':
                            initQueenOnBoard(tx, ty, ChessColor.BLACK, 'Q');
                            break;
                        case 'q':
                            initQueenOnBoard(tx, ty, ChessColor.WHITE, 'q');
                            break;
                        case 'K':
                            initKingOnBoard(tx, ty, ChessColor.BLACK, 'K');
                            break;
                        case 'k':
                            initKingOnBoard(tx, ty, ChessColor.WHITE, 'k');
                            break;
                        case 'P':
                            initPawnOnBoard(tx, ty, ChessColor.BLACK, 'P');
                            break;
                        case 'p':
                            initPawnOnBoard(tx, ty, ChessColor.WHITE, 'p');
                            break;
                    }
                }
                chessComponents[sx][sy].repaint();
                chessComponents[tx][ty].repaint();
            }
            setcurPlayerRound(ss[1],ss[2]);
            i++;
        }
        cgameFrame.setDownTimes();
        cgameFrame.RefreshStep();
    }

    public void setcurPlayerRound(String s1,String s2){
        if(s1.equals("b"))
            currentPlayer=ChessColor.BLACK;
        else
            currentPlayer=ChessColor.WHITE;
        currentRound=Integer.parseInt(s2);
    }

    public void RegretSwapChess(ChessComponent chess1, ChessComponent chess2) {
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;
    }

    //清除走棋记录
    public void ClearSteps(){
        stepList.clear();
    }

}
