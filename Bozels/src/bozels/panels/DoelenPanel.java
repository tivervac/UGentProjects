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
public class DoelenPanel extends JPanel {

    //Informatie nodig voor de JList
    private String[] materialen = {"Klein", "Groot"};
    private String[] mats = {"small","big"};
    private Color[] icons = {Color.GREEN, Color.PINK};
    
    public DoelenPanel(ListsModel model) {        
        GroupLayout mainLayout = new GroupLayout(this);
        //Maak een panel voor de instellingen    
        JPanel tabs = new JPanel();
        GroupLayout layout = new GroupLayout(tabs);
        
        JPanel leftPanel = new JPanel();
        GroupLayout leftLayout = new GroupLayout(leftPanel);
        
        mainLayout.setAutoCreateGaps(true);
        mainLayout.setAutoCreateContainerGaps(true);
        setLayout(mainLayout);

        model.setLists(materialen, mats, icons);
        
        MakeLayout makeLayout = new MakeLayout(model);
        //Maak de linkerkant van de instellingen
        makeLayout.makeLayoutL(leftLayout, leftPanel);
        //Maak de rechterkant van de instellingen
        makeLayout.makeLayoutR(layout, tabs);
        //Maak de list en zet een rand rond de instellingen
        JList list = model.getList();
        
        //Maak een panel met de 2 panels en de JList in
        JPanel instellingen = new JPanel();
        GroupLayout instellingenLayout = new GroupLayout(instellingen);
        
        //Voeg JList en instellingen samen
        instellingenLayout.setVerticalGroup(
                instellingenLayout.createSequentialGroup()
                .addGroup(instellingenLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(leftPanel)
                .addComponent(tabs))
        );
        
        instellingenLayout.setHorizontalGroup(
                instellingenLayout.createSequentialGroup()
                .addGroup(instellingenLayout.createParallelGroup()
                .addComponent(leftPanel))
                .addGroup(instellingenLayout.createParallelGroup()
                .addComponent(tabs))
        );
        
        instellingen.setLayout(instellingenLayout);
        instellingen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        mainLayout.setVerticalGroup(
                mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(list)
                .addComponent(instellingen))
                );
         mainLayout.setHorizontalGroup(
                mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup()
                .addComponent(list))
                .addGroup(mainLayout.createParallelGroup()
                .addComponent(instellingen))
                );
                  
        setLayout(mainLayout);
    }
}
