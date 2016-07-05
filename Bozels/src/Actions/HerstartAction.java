package actions;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import modellen.SpelModel;
import reader.XMLParser;

/**
 *
 * @author Titouan Vervack
 */
public class HerstartAction extends AbstractAction {

    private SpelModel model;

    public HerstartAction(SpelModel model) {
        this.model = model;
        putValue(NAME, "Herstart");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (model.getCurrentFile() != null) {
            //Maak alle lijsten leeg en zet variabelen terug naar de originele waarde
            model.setRun(false);
            model.setFirst(true);
            model.fireStateChanged();
            model.clearLists();
            //Zonder sleep is er niet genoeg tijd om te herstarten
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(HerstartAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Laad het level opnieuw in
            new XMLParser(model.getCurrentFile(), model, 0);
        }
    }
}
