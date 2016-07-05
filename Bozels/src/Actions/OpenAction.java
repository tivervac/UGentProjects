package actions;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import modellen.SpelModel;
import reader.XMLParser;

/**
 *
 * @author Titouan Vervack
 */
public class OpenAction extends AbstractAction {

    private File first = new File("leeg.xml");
    private static final String PAD = "./etc/levels/";
    private SpelModel model;

    //Lees het level in en selecteer standaard leeg.xml
    public OpenAction(SpelModel model) {
        this.model = model;
        putValue(NAME, "Open level");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser(new java.io.File(PAD));
        fc.setSelectedFile(first);
        fc.setDialogTitle("Level laden");
        int knop = fc.showOpenDialog(null);
        if (knop == JFileChooser.APPROVE_OPTION) {
            new XMLParser(fc.getSelectedFile(), model, 1);
        }
    }
}
