package bozels.layouthulp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import modellen.ListsModel;

/**
 *
 * @author Titouan Vervack
 */
public class MakeList extends JList {

    private Object[] panels;
    private static final Dimension MIN = new Dimension(75, 128);
    
    //Maak een JList aan
    public MakeList(ListsModel model) {
        setCellRenderer(new CellRenderer());

        String[] materialen = model.getStringList();
        Color[] colors = model.getIconList();
        panels = new Object[materialen.length];

        //Zet de objecten 
        for (int i = 0; i < materialen.length; i++) {
            JLabel label = new JLabel(materialen[i], new CreateIcon(colors[i]), JLabel.LEFT);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(label, BorderLayout.WEST);
            panels[i] = panel;
        }

        model.setObjects(panels);

        setListData(model.getObjects());

        //Gebruik een luisteraar om wanneer nodig de lijst te repainten
        ListDataListener listener = new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent e) {
                //Niet nodig;
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                //Niet nodig;
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                repaint();
            }
        };

        model.addListDataListener(listener);

        //Instellingen van de JList en teken ook een rand
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setMinimumSize(MIN);

        setBorder(BorderFactory.createLoweredBevelBorder());
    }
}
