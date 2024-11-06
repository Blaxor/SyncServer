import ro.deiutzentartainment.games.GameHandler;
import ro.deiutzentartainment.games.GameManager;
import ro.deiutzentartainment.games.data.Game;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialPage extends JFrame {

    private JPanel TabTop;

    public InitialPage(){

        Game game = GameManager.getInstance().getGame("test");


        setTitle("SyncGame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(1000,500);
        setVisible(true);


        TabTop = new JPanel();
        TabTop.setVisible(true);
        TabTop.setLayout(new FlowLayout());
        add(TabTop);


        TabTop.add(createMenuLabelAllGames());

        setVisible(true);



    }
    public static void main(String[] args){
        new InitialPage();

    }
    protected  static JScrollPane jScrollPane;
    public JScrollPane createMenuLabelAllGames(){
        GameManager gameManager = GameManager.getInstance();
        JPanel jPanel = new JPanel();
        /*jPanel.setMinimumSize(new Dimension(0,300));*/
        jPanel.updateUI();
        jPanel.setVisible(true);
        jPanel.setLayout(new FlowLayout());
        jScrollPane = new JScrollPane(jPanel);
        for (Game game : gameManager.getAllGames()) {
            jPanel.add(generateGamePanel(game,jScrollPane));
        }
        return jScrollPane;
    }

    public JPanel generateGamePanel(Game game,JScrollPane jScrollPane){
        JPanel principal = new JPanel();
        principal.setLayout(new GridLayout(2,1));
        //Setting the game's name.
        JButton name = new JButton();
        name.setBorder(new LineBorder(Color.BLACK));
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setText(game.getName());
        name.addActionListener(TabTopButtonHandler.showInfo(game,principal,jScrollPane));


        principal.add(name);

        //Setting the game imaginary icon?
        principal.setVisible(true);
        name.setVisible(true);

    return principal;


    }
}
