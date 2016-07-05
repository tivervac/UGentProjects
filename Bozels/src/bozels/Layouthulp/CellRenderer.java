package bozels.layouthulp;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;

/**
 *
 * @author Titouan Vervack
 */
public class CellRenderer implements ListCellRenderer {
    
    public CellRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object c, int i, boolean isSelected, boolean focus) {
        if (c instanceof JPanel) {
            JComponent component = (JComponent) c;
            component.setForeground(Color.WHITE);
            component.setBackground(Color.WHITE);
            //Maak een kadertje rond het geselecteerde element
            component.setBorder(isSelected ? BorderFactory.createLineBorder(Color.BLACK) : null);
            return component;
        }
        return null;
    }
}
