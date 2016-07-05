package bozels.layouthulp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 *
 * @author Titouan Vervack
 */
public class CreateIcon implements Icon {

    private static final int SIZE = 15;
    private Color color;
    
    //Maakt een icon voor in de JList
    public CreateIcon(Color color) {
        this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x, y, SIZE, SIZE);
    }

    @Override
    public int getIconWidth() {
        return SIZE;
    }

    @Override
    public int getIconHeight() {
        return SIZE;
    }
    
    public Color getColor(){
        return color;
    }
}