package actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class ZwaartepuntListener extends AbstractAction {

    private SpelModel model;
    
    public ZwaartepuntListener(SpelModel model) {
        this.model = model;
        putValue(NAME, "Toon zwaartepunt");
    }

    //Kijk of het zwaartepunt moet getekend worden
    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox box = (JCheckBox) e.getSource();
        model.setDrawZwaartepunt(box.isSelected());
    }
}
