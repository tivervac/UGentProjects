package actions;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author Titouan Vervack
 */
public class FieldListener extends KeyAdapter {
    
    private JTextField field;
    
    public FieldListener(JTextField field){
        this.field = field;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (field.hasFocus()){
            field.setBackground(Color.YELLOW);
        }   
    }
}
