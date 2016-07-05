package bozels.layouthulp;

import actions.BreekbaarAction;
import actions.FieldListener;
import actions.KleurAction;
import actions.SelectionAction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import modellen.ListsModel;
import modellen.SpelModel;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author Titouan Vervack
 */
public class MakeLayout implements ChangeListener {

    private ListsModel model;
    private static final Dimension KNOP_GROOTTE = new Dimension(40, 20);
    private SpelModel spelModel;
    private String[] mats;
    private JList list;
    private JTextField dichtField;
    private JTextField restField;
    private JTextField wrijvingField;
    private JTextField krachtField;
    private JTextField sterkteField;
    private JButton kleurKnop;
    private JCheckBox breekbaar;

    public MakeLayout(ListsModel model) {
        this.model = model;
        spelModel = model.getSpelModel();
        mats = model.getMats();
        model.getSpelModel().addChangeListener(this);
    }

    //Maak de linkerkant van de instellingen
    public void makeLayoutL(GroupLayout leftLayout, JPanel leftPanel) {
        //Maak de JList
        list = new MakeList(model);
        model.setList(list);

        //Maak de componenten
        JLabel dichtheid = new JLabel("Dichtheid:");
        dichtField = new JTextField();

        JLabel restitutie = new JLabel("Restitutie:");
        restField = new JTextField();

        JLabel wrijving = new JLabel("Wrijving:");
        wrijvingField = new JTextField();

        JLabel kleur = new JLabel("Kleur:");
        kleurKnop = new JButton(new KleurAction(leftPanel, model));
        kleurKnop.setMinimumSize(KNOP_GROOTTE);
        list.addListSelectionListener(new SelectionAction(model));
        list.setSelectedIndex(0);
        
        //Beheer tekst
        dichtField.addKeyListener(new FieldListener(dichtField));
        dichtField.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField comp = (JTextField) e.getSource();
                try {
                    String tekst = comp.getText();
                    if (Pattern.matches("\\d+[.]\\d+", tekst)) {
                        spelModel.getMap().get(mats[list.getSelectedIndex()]).setDichtheid(Float.valueOf(comp.getText()).floatValue());
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
        
        restField.addKeyListener(new FieldListener(restField));
        restField.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField comp = (JTextField) e.getSource();
                try {
                    String tekst = comp.getText();
                    if (Pattern.matches("\\d+[.]\\d+", tekst)) {
                        for (Iterator it = spelModel.getBodies().keySet().iterator(); it.hasNext();) {
                            Body blok = (Body) it.next();
                            blok.setAwake(true);
                        }
                        spelModel.getMap().get(mats[list.getSelectedIndex()]).setRestitutie(Float.valueOf(comp.getText()).floatValue());
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
        
        wrijvingField.addKeyListener(new FieldListener(wrijvingField));
        wrijvingField.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField comp = (JTextField) e.getSource();
                try {
                    String tekst = comp.getText();
                    if (Pattern.matches("\\d+[.]\\d+", tekst)) {
                        spelModel.getMap().get(mats[list.getSelectedIndex()]).setWrijving(Float.valueOf(comp.getText()).floatValue());
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
        
        //Stel de layout in voor de linkerkant van de instellingen en stop ze in een panel
        leftLayout.setAutoCreateGaps(true);
        leftLayout.setAutoCreateContainerGaps(true);

        leftLayout.setVerticalGroup(
                leftLayout.createSequentialGroup()
                .addGroup(leftLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(dichtheid).addComponent(dichtField))
                .addGroup(leftLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(restitutie)
                .addComponent(restField)).addGroup(leftLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(wrijving).addComponent(wrijvingField))
                .addGroup(leftLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(kleur)
                .addComponent(kleurKnop)));

        leftLayout.setHorizontalGroup(
                leftLayout.createSequentialGroup()
                .addGroup(leftLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(dichtheid)
                .addComponent(restitutie)
                .addComponent(wrijving)
                .addComponent(kleur))
                .addGroup(leftLayout.createParallelGroup()
                .addComponent(dichtField)
                .addComponent(restField)
                .addComponent(wrijvingField)
                .addComponent(kleurKnop)));

        leftPanel.setLayout(leftLayout);
    }

    //Maakt de rechterkant van de instellingen
    public void makeLayoutR(GroupLayout rightLayout, JPanel rightPanel) {
        //Maak de componenten aan
        breekbaar = new JCheckBox(new BreekbaarAction(model));
        breekbaar.setSelected(spelModel.getMap().get(model.getMats()[model.getList().getSelectedIndex()]).getBreekbaar());

        JLabel krachtdrempel = new JLabel("Krachtdrempel:");
        krachtField = new JTextField();

        JLabel sterkte = new JLabel("Sterkte:");
        sterkteField = new JTextField();

        //Beheer tekst
        krachtField.addKeyListener(new FieldListener(krachtField));
        krachtField.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField comp = (JTextField) e.getSource();
                try {
                    String tekst = comp.getText();
                    if (Pattern.matches("\\d+[.]\\d+", tekst)) {
                        spelModel.getMap().get(mats[list.getSelectedIndex()]).setKrachtdrempel(Float.valueOf(comp.getText()).floatValue());
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
        sterkteField.addKeyListener(new FieldListener(sterkteField));
        sterkteField.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField comp = (JTextField) e.getSource();
                try {
                    String tekst = comp.getText();
                    if (Pattern.matches("\\d+[.]\\d+", tekst)) {
                        spelModel.getMap().get(mats[list.getSelectedIndex()]).setSterkte(Float.valueOf(comp.getText()).floatValue());
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

        //Stel de layout in voor de rechterkant van de instellingen en stop ze in een panel
        rightLayout.setAutoCreateGaps(true);
        rightLayout.setAutoCreateContainerGaps(true);

        rightLayout.setVerticalGroup(
                rightLayout.createSequentialGroup()
                .addGroup(rightLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(breekbaar)).addGroup(rightLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(krachtdrempel)
                .addComponent(krachtField))
                .addGroup(rightLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(sterkte)
                .addComponent(sterkteField)));
        rightLayout.setHorizontalGroup(
                rightLayout.createSequentialGroup()
                .addGroup(rightLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(krachtdrempel).addComponent(sterkte))
                .addGroup(rightLayout.createParallelGroup()
                .addComponent(breekbaar)
                .addComponent(krachtField)
                .addComponent(sterkteField)));
        rightPanel.setLayout(rightLayout);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        //Verander de tekst, kleur van de kleurknop en de status van de checbox wanneer een andere element in de JList geselecteerd wordt
        dichtField.setText(Float.toString(spelModel.getMap().get(mats[list.getSelectedIndex()]).getDichtheid()));
        restField.setText(Float.toString(spelModel.getMap().get(mats[list.getSelectedIndex()]).getRestitutie()));
        wrijvingField.setText(Float.toString(spelModel.getMap().get(mats[list.getSelectedIndex()]).getWrijving()));
        kleurKnop.setBackground(model.getColor(list.getSelectedIndex()));
        //Try-catch omdat niet elke paneel een rechterkant heeft
        try {
            breekbaar.setSelected(spelModel.getMap().get(model.getMats()[model.getList().getSelectedIndex()]).getBreekbaar());
            krachtField.setText(Float.toString(spelModel.getMap().get(mats[list.getSelectedIndex()]).getKrachtdrempel()));
            sterkteField.setText(Float.toString(spelModel.getMap().get(mats[list.getSelectedIndex()]).getSterkte()));
        } catch (Exception exp) {
            //Paneel heeft geen rechterkant
        }
    }
}