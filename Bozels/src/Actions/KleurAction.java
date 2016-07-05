package actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JList;
import modellen.ListsModel;

/**
 *
 * @author Titouan Vervack
 */
public class KleurAction extends AbstractAction {

    private Component c;
    private ListsModel model;

    //Verander de kleur van materialen en de knop
    public KleurAction(Component c, ListsModel model) {
        this.c = c;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton knop = (JButton) e.getSource();
        JColorChooser kies = new JColorChooser();
        Color myIconColor = kies.showDialog(c, "Kies een kleur", knop.getBackground());
        JList list = model.getList();
        if (myIconColor != null) {
            knop.setBackground(myIconColor);
            if (list.getSelectedIndex() >= 0) {
                //Verander het icoon van de JList 
                model.setIcon(list.getSelectedIndex(), myIconColor);
            }
        }
    }
}
