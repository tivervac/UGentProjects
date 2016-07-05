package bozels.panels;

import actions.HerstartAction;
import java.awt.Dimension;
import javax.swing.*;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class EditorPanel extends JPanel {

    //Standaard grootte van een knop
    private static final int BUTTON_SIZE = 80;

    public EditorPanel(SpelModel model) {
        GroupLayout layout = new GroupLayout(this);

        
        JToggleButton pauze = new JToggleButton();
        model.setPauzeKnop(pauze);
        JButton herstarten = new JButton(new HerstartAction(model));
        pauze.setMinimumSize(new Dimension(BUTTON_SIZE,0));
        herstarten.setMinimumSize(new Dimension(BUTTON_SIZE,0));
        
        //Verbind pauze en herstart met de JTabbedPane adhv grouplayout
                JTabbedPane tabs = new TabbedPanel(model);
                layout.setVerticalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                .addComponent(pauze))
                .addGroup(layout.createParallelGroup()
                .addComponent(herstarten)))
                .addComponent(tabs))
                );
                layout.setHorizontalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                .addComponent(pauze)
                .addComponent(herstarten))
                .addGroup(layout.createParallelGroup()
                .addComponent(tabs))
                );
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
    }
}
