import view.ChessGameFrame;
import view.User_Login;

import javax.swing.*;
import java.awt.*;

public class Main  {
    public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
            User_Login logFrame=new User_Login(350,200,"白方选手登录：");
            logFrame.setVisible(true);
        });
    }
}


