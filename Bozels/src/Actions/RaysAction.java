package actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class RaysAction extends AbstractAction {

    private SpelModel model;

    public RaysAction(SpelModel model) {
        putValue(NAME, "Toon rays");
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox box = (JCheckBox)e.getSource();
        model.setDrawRays(box.isSelected());
    }
}
