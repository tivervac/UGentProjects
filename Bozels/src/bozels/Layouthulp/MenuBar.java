package bozels.layouthulp;

import actions.HerstartAction;
import actions.OpenAction;
import java.awt.event.ActionEvent;
import javax.swing.*;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class MenuBar extends JMenuBar {

    public MenuBar(SpelModel model) {
        //Maak de menubaar aan en add de acties al
        JMenu bestand = new JMenu("Bestand");
        JMenuItem openItem = new JMenuItem(new OpenAction(model));
        JMenuItem exitItem = new JMenuItem(new AbstractAction("Afsluiten") {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu spel = new JMenu("Spel");
        JCheckBoxMenuItem pauzeItem = new JCheckBoxMenuItem();
        model.setPauzeBox(pauzeItem);
        JMenuItem herstartItem = new JMenuItem(new HerstartAction(model));

        bestand.add(openItem);
        bestand.add(exitItem);

        spel.add(pauzeItem);
        spel.add(herstartItem);

        add(bestand);
        add(spel);
    }
}
