import ro.deiutzentartainment.games.data.Game;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final  class TabTopButtonHandler {

    static boolean isAnyOpen= false;

    private static JPanel currentPanel;




    public static ActionListener showInfo(Game game,JPanel parent,JScrollPane jScrollPane){

        return actionEvent -> {
            if(isAnyOpen){

                currentPanel.getParent().remove(currentPanel);
                currentPanel.removeAll();
                isAnyOpen =false;
                if(parent == currentPanel.getParent()){
                    currentPanel.updateUI();
                    jScrollPane.updateUI();
                    jScrollPane.revalidate();

                    return;
                }
                jScrollPane.updateUI();
                jScrollPane.revalidate();
                currentPanel.updateUI();
            }

            JPanel jPanel = new JPanel();

            jPanel.setLayout(new FlowLayout());
            jPanel.setVisible(true);

            JLabel gameSaveLocationName = new JLabel();
            gameSaveLocationName.setText("<html>"+game.getPrettyWritten().replaceAll("\\n","<br>")+"</html>");
            gameSaveLocationName.setVisible(true);
            jPanel.add(gameSaveLocationName);
            parent.add(jPanel);
            parent.updateUI();
            jScrollPane.updateUI();

            currentPanel = jPanel;
            isAnyOpen =true;
            jScrollPane.updateUI();
            jScrollPane.revalidate();
        };

    }



}
