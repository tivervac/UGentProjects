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
public final class MaterialenPanel extends JPanel {

    //Informatie nodig voor de JList
    private String[] materialen = {"Vast", "Beton", "Hout", "Metaal", "Ijs"};
    private String[] mats = {"solid", "stone", "wood","metal","ice"};
    private Color[] colors = {Color.BLACK, Color.DARK_GRAY, new Color(123, 49, 11), Color.CYAN, Color.BLUE};

    public MaterialenPanel(ListsModel model) {
        JPanel rightPanel = new JPanel();
        GroupLayout rightLayout = new GroupLayout(rightPanel);

        JPanel leftPanel = new JPanel();
        GroupLayout leftLayout = new GroupLayout(leftPanel);

        GroupLayout mainLayout = new GroupLayout(this);

        mainLayout.setAutoCreateGaps(true);
        mainLayout.setAutoCreateContainerGaps(true);

        model.setLists(materialen, mats, colors);

        MakeLayout makeLayout = new MakeLayout(model);
        //Maak de linkerkant van de instellingen   
        makeLayout.makeLayoutL(leftLayout, leftPanel);
        //Maak de rechterkant van de instellingen
        makeLayout.makeLayoutR(rightLayout, rightPanel);

        //Maak de list en zet een rand rond de instellingen
        JList list = model.getList();
        //Maak een panel voor de instellingen  
        JPanel instellingen = new JPanel();
        GroupLayout instellingenLayout = new GroupLayout(instellingen);

        //Voeg JList en instellingen samen
        instellingenLayout.setVerticalGroup(
                instellingenLayout.createSequentialGroup()
                .addGroup(instellingenLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(leftPanel)
                .addComponent(rightPanel)));
        instellingenLayout.setHorizontalGroup(
                instellingenLayout.createSequentialGroup()
                .addGroup(instellingenLayout.createParallelGroup()
                .addComponent(leftPanel))
                .addGroup(instellingenLayout.createParallelGroup()
                .addComponent(rightPanel)));
        instellingen.setLayout(instellingenLayout);
        instellingen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mainLayout.setVerticalGroup(
                mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(list)
                .addComponent(instellingen)));
        mainLayout.setHorizontalGroup(
                mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup()
                .addComponent(list))
                .addGroup(mainLayout.createParallelGroup()
                .addComponent(instellingen)));
        setLayout(mainLayout);
    }
}