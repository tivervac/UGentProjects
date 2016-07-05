package bozels.panels;

import javax.swing.JTabbedPane;
import modellen.ListsModel;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class TabbedPanel extends JTabbedPane {

    public TabbedPanel(SpelModel model) {
        //Maak en add alle tabs
        addTab("Algemeen", new AlgemeenPanel(model));
        ListsModel mModel = new ListsModel(model);
        addTab("Materialen", new MaterialenPanel(mModel));
        ListsModel bModel = new ListsModel(model);
        addTab("Bozels", new BozelsPanel(bModel));
        ListsModel dModel = new ListsModel(model);
        addTab("Doelen", new DoelenPanel(dModel));
    }
}
