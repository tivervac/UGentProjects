package bozels.panels;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class MainPanel extends JPanel implements ChangeListener {

    //Maak het bovenste en het onderste panel en add ze aan de contentPane
    public MainPanel(SpelModel model) {
        setLayout(new BorderLayout());
        
        SpelPanel spelPanel = new SpelPanel(model);
                
        EditorPanel editPanel = new EditorPanel(model);

        add(spelPanel, BorderLayout.CENTER);
        add(editPanel, BorderLayout.SOUTH);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        repaint();
    }
}
