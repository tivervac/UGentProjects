package actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class SnelheidListener extends AbstractAction {

    private SpelModel model;
    
    public SnelheidListener(SpelModel model) {
        this.model = model;    
        putValue(NAME, "Toon snelheid");
    }

    //Kijk of de snelheid moet worden getekend
    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox box = (JCheckBox) e.getSource();
        model.setDrawSnelheid(box.isSelected());
    }
}
