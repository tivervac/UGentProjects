package actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JToggleButton;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class PauzeAction extends AbstractAction {

    private SpelModel model;

    public PauzeAction(SpelModel model) {
        putValue(NAME, "Pauze");
        this.model = model;
    }

    //Kijk naar de staat van de andere herstartknop en pas de knop eraan aan
    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBoxMenuItem box = model.getPauzeBox();
        JToggleButton knop = model.getPauzeKnop();
            if (e.getSource() instanceof JToggleButton) {
                    model.setPaused(!knop.isSelected());
                    box.setState(knop.isSelected());
            } else {
                    knop.setSelected(box.getState());
                    model.setPaused(!box.getState());
            }
    }
}