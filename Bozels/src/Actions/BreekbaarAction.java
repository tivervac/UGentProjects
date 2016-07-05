package actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import modellen.ListsModel;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class BreekbaarAction extends AbstractAction {

    private ListsModel model;
    //Kijkt of objecten breekbaar mogen zijn
    public BreekbaarAction(ListsModel model) {
        putValue(NAME, "Breekbaar");
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SpelModel spelModel = model.getSpelModel();
        JCheckBox box = (JCheckBox) e.getSource();
        //Pas het veld breekbaar in Objecten
        spelModel.getMap().get(model.getMats()[model.getList().getSelectedIndex()]).setBreekbaar(box.isSelected());
    }
}
