package modellen;

import bozels.layouthulp.CreateIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author Titouan Vervack
 */
public class ListsModel extends DefaultListModel {

    private String[] materialen = {};
    private String[] mats = {};
    private Color[] colors = {};
    private JList list;
    private Object[] objects = {};
    private SpelModel model;

    public ListsModel(SpelModel model) {
        this.model = model;
    }

    //Zet lijsten als nodig
    public void setLists(String[] materialen, String[] mats, Color[] colors) {
        if (!Arrays.equals(materialen, this.materialen)) {
            this.materialen = materialen;
        }
        if (!Arrays.equals(mats, this.mats)) {
            this.mats = mats;
        }
        if (!Arrays.equals(colors, this.colors)) {
            this.colors = colors;
        }
    }

    public String[] getMats() {
        return mats;
    }

    public Color[] getIconList() {
        return colors;
    }

    public String[] getStringList() {
        return materialen;
    }

    public void setList(JList list) {
        this.list = list;
    }

    public JList getList() {
        return list;
    }

    //Verander het icoon in de JList
    public void setIcon(int index, Color c) {
        if (!colors[index].equals(c)) {
            colors[index] = c;
            JLabel label = new JLabel(materialen[index], new CreateIcon(colors[index]), JLabel.LEFT);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(label, BorderLayout.WEST);
            objects[index] = panel;
            //Verander de kleur van het materiaal in het spel
            model.getMap().get(mats[index]).setColor(colors[index]);
            fireContentsChanged(this, index, index);
        }
    }

    public Color getColor(int i) {
        return colors[i];
    }

    public void setObjects(Object[] objects) {
        if (objects != this.objects) {
            this.objects = objects;
        }
    }

    public Object[] getObjects() {
        return objects;
    }

    public SpelModel getSpelModel() {
        return model;
    }
}
