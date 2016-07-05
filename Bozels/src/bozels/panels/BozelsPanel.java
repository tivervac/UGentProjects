package bozels.panels;

import bozels.layouthulp.MakeLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import modellen.ListsModel;

/**
 *
 * @author Titouan Vervack
 */
public class BozelsPanel extends JPanel {

    //Informatie nodig voor de JList
    private String[] materialen = {"Rood", "Blauw", "Wit", "Geel"};
    private String[] mats = {"red", "blue", "white","yellow"};
    private Color[] colors = {Color.RED, Color.BLUE, Color.LIGHT_GRAY, new Color(255,100,0)};

    public BozelsPanel(ListsModel model) {
        //Maak een panel voor de instellingen
        JPanel leftPanel = new JPanel();
        GroupLayout leftLayout = new GroupLayout(leftPanel);

        GroupLayout mainLayout = new GroupLayout(this);

        mainLayout.setAutoCreateGaps(true);
        mainLayout.setAutoCreateContainerGaps(true);

        model.setLists(materialen, mats, colors);
        MakeLayout makeLayout = new MakeLayout(model);
        //Maak de instellingen
        makeLayout.makeLayoutL(leftLayout, leftPanel);
        //Maak de list en zet een rand rond de instellingen
        JList list = model.getList();
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        //Voeg JList en instellingen samen
        mainLayout.setVerticalGroup(
                mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(list)
                .addComponent(leftPanel)));
        mainLayout.setHorizontalGroup(
                mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup()
                .addComponent(list))
                .addGroup(mainLayout.createParallelGroup()
                .addComponent(leftPanel)));

        setLayout(mainLayout);
    }
}