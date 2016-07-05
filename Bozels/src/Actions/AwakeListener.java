package actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class AwakeListener extends AbstractAction {

    private SpelModel model;
    //Kijkt of slapenede objecten moeten getekend worden
    public AwakeListener(SpelModel model) {
        this.model = model;
        putValue(NAME, "Markeer slapende objecten");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox box = (JCheckBox) e.getSource();
        model.setAwake(box.isSelected());
        model.fireStateChanged();
    }
}
