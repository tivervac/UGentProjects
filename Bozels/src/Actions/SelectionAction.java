package actions;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modellen.ListsModel;

/**
 *
 * @author Titouan Vervack
 */
public class SelectionAction implements ListSelectionListener {

    private ListsModel model;

    public SelectionAction(ListsModel model) {
        this.model = model;
    }

    //Roep een statechanged aan om de tekstvelden/knoppen te veranderen 
    //Wanneer een ander object wordt gekozen in de lijst
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();
        if ( list.getSelectedIndex() >= 0 ) {
            model.getSpelModel().fireStateChanged();
        }
    }
}
