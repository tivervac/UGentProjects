package bozels;

import bozels.layouthulp.MenuBar;
import bozels.panels.MainPanel;
import java.awt.EventQueue;
import javax.swing.JFrame;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class Bozels {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame window = new JFrame("Bozels");
                SpelModel model = new SpelModel();
                //Nodig voor popups wanneer je verliest/wint
                model.setWindow(window);
                window.setJMenuBar(new MenuBar(model));
                window.setContentPane(new MainPanel(model));
                window.pack();
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setVisible(true);
            }
        });
    }
}