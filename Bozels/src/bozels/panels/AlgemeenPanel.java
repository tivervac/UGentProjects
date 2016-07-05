package bozels.panels;

import actions.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;
import javax.swing.*;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class AlgemeenPanel extends JPanel {

    public AlgemeenPanel(SpelModel smodel) {
        //Maak het model final zodat je eraan kan in de binnenklassen
        final SpelModel model = smodel;
        
        //Zet tekst en beheer de tekstvelden
        JLabel fz = new JLabel("Zwaartekracht:");
        JTextField fzField = new JTextField();
        fzField.setText(Float.toString(model.getFz()));
        fzField.addKeyListener(new FieldListener(fzField));
        fzField.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField comp = (JTextField) e.getSource();
                try {
                    String tekst = comp.getText();
                    //Als er een punt met een getal voor en na instaat en een eventuele -
                    //Verander dan de kleur naar wit, tenzij het niet kan gecast worden naar float
                    if(Pattern.matches("[-]*\\d+[.]\\d+", tekst)){
                        model.setFz((Float.valueOf(tekst).floatValue()));
                        comp.setBackground(Color.WHITE);
                        comp.setCaretPosition(comp.getText().length());
                    } else {
                        throw new Exception();
                    }
                    //Er was een fout bij het instellen van de tekst dus maak de kleur rood
                } catch (Exception exp2) {
                    comp.setBackground(Color.RED);
                }
            }
        });
        JCheckBox fzBox = new JCheckBox(new ZwaartepuntListener(model));

        JLabel tijdsstap = new JLabel("Tijdsstap:");
        JTextField tijdField = new JTextField();
        tijdField.setText(Float.toString(model.getTijdsstap()));
        tijdField.addKeyListener(new FieldListener(tijdField));
        tijdField.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField comp = (JTextField) e.getSource();
                try {
                    String tekst = comp.getText();
                    if(Pattern.matches("\\d+[.]\\d+", tekst)){
                        model.setTijdsstap((Float.valueOf(tekst).floatValue()));
                        comp.setBackground(Color.WHITE);
                        comp.setCaretPosition(comp.getText().length());
                    } else {
                        throw new Exception();
                    }
                } catch (Exception exp2) {
                    comp.setBackground(Color.RED);
                }
            }
        });
        JCheckBox tijdBox = new JCheckBox(new SnelheidListener(model));


        JLabel snelheid = new JLabel("Snelheid:");
        JTextField snelField = new JTextField();
        snelField.setText(Integer.toString(model.getSnelheid()));
        snelField.addKeyListener(new FieldListener(snelField));
        snelField.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField comp = (JTextField) e.getSource();
                try {
                    String tekst = comp.getText();
                    model.setSnelheid((Integer.valueOf(tekst).intValue()));
                    comp.setBackground(Color.WHITE);
                    comp.setCaretPosition(comp.getText().length());
                } catch (Exception exp2) {
                    comp.setBackground(Color.RED);
                }
            }
        });

        JCheckBox snelBox = new JCheckBox(new AwakeListener(model));

        JLabel lanceerkracht = new JLabel("Lanceerkracht:");
        JTextField lanceerField = new JTextField();
        lanceerField.setText(Float.toString(model.getLanceerkracht()));
        lanceerField.addKeyListener(new FieldListener(lanceerField));
        lanceerField.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField comp = (JTextField) e.getSource();
                try {
                    String tekst = comp.getText();
                    if(Pattern.matches("\\d+[.]\\d+", tekst)){
                        model.setLanceerkracht((Float.valueOf(tekst).floatValue()));
                        comp.setBackground(Color.WHITE);
                        comp.setCaretPosition(comp.getText().length());
                    } else {
                        throw new Exception();
                    }
                } catch (Exception exp2) {
                    comp.setBackground(Color.RED);
                }
            }
        });
        JCheckBox lanceerBox = new JCheckBox(new RaysAction(model));

        //Maak de grouplayout die het algemeen tab maakt
        GroupLayout layout = new GroupLayout(this);
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(fz)
                .addComponent(fzField)
                .addComponent(fzBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(tijdsstap)
                .addComponent(tijdField)
                .addComponent(tijdBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(snelheid)
                .addComponent(snelField)
                .addComponent(snelBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lanceerkracht)
                .addComponent(lanceerField)
                .addComponent(lanceerBox)));
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(fz)
                .addComponent(tijdsstap)
                .addComponent(snelheid)
                .addComponent(lanceerkracht))
                .addGroup(layout.createParallelGroup()
                .addComponent(fzField)
                .addComponent(tijdField)
                .addComponent(snelField)
                .addComponent(lanceerField))
                .addGroup(layout.createParallelGroup()
                .addComponent(fzBox)
                .addComponent(tijdBox)
                .addComponent(snelBox)
                .addComponent(lanceerBox)));
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
    }
}
