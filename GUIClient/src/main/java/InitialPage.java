import ro.deiutzentartainment.games.data.SavePathGame;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

import java.util.ArrayList;
import java.util.List;

public class InitialPage extends JFrame {
    private JList list1;

    public InitialPage(){

        setTitle("SyncGame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300,200);
        setLocationRelativeTo(null);
        setVisible(true);

        List<String> string = new ArrayList<String>();
        for (SavePathGame value : SavePathGame.values()) {
            string.add(value.name + "        " + value.defaultPath);
        }
        list1 = new JList<>(string.toArray());

        this.add(list1);
        list1.show();




    }
    public static void main(String[] args){
        new InitialPage();

    }


}
