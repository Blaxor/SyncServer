import ro.deiutzentartainment.games.data.Game;
import ro.deiutzentartainment.games.data.SavePathGame;

import javax.swing.*;

public class InitialPage extends JFrame {

    private JPanel GamePanel1;
    private JPanel GameView;
    private JScrollPane GameViewScroll;
    private JLabel GameName;
    private JLabel IconGame;

    public InitialPage(){

        Game game = SavePathGame.TEST_GAME.generateGame();


        setTitle("SyncGame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
        GameView = new JPanel();
        add(GameView);
        GameViewScroll = new JScrollPane();
        GameViewScroll.setVisible(true);
        GameView.add(GameViewScroll);
        GamePanel1 = new JPanel();
        GamePanel1.setVisible(true);

        GameViewScroll.add(GamePanel1);
        GameName = new JLabel();
        GameName.setVisible(true);
        GameName.setText(game.getName());
        GamePanel1.add(GameName);









    }
    public static void main(String[] args){
        new InitialPage();

    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
