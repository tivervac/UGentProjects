package reader;

import decor.Bal;
import decor.Blok;
import decor.bozevogels.Bozel;
import decor.doelen.Doel;
import java.io.File;
import java.io.IOException;
import java.util.List;
import jbox.Wereld;
import modellen.SpelModel;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Titouan Vervack
 */
public class XMLParser {

    public XMLParser(File path, SpelModel model, int reset) {
        //Kijk of het spel herstart is of geopend is door de filechooser
        //Als het geopend is door de filechooser, reset = 1, maakt dan eerst alles leeg
        if (reset == 0) {
            Wereld wereld = new Wereld(model.getPanel(), model);
            model.setCurrentFile(path);
            try {
                Document document = new SAXBuilder().build(path);
                Element rootNode = document.getRootElement();
                List blokken = rootNode.getChildren("block");
                List ballen = rootNode.getChildren("ellipse");
                List bozels = rootNode.getChildren("bozel");
                List doelen = rootNode.getChildren("target");

                //Blokken
                for (int i = 0; i < blokken.size(); i++) {
                    Element el = (Element) blokken.get(i);
                    float x = Float.valueOf(el.getAttributeValue("x")).floatValue();
                    float y = Float.valueOf(el.getAttributeValue("y")).floatValue();
                    String material = el.getAttributeValue("material");
                    float angle = (float) Math.toRadians(Float.valueOf(el.getAttributeValue("angle")).floatValue());
                    float width = Float.valueOf(el.getAttributeValue("width")).floatValue();
                    float height = Float.valueOf(el.getAttributeValue("height")).floatValue();
                    Blok blok = new Blok(x, y, material, angle, width, height, model);
                }

                //Ballen
                for (int i = 0; i < ballen.size(); i++) {
                    Element el = (Element) ballen.get(i);
                    float x = Float.valueOf(el.getAttributeValue("x")).floatValue();
                    float y = Float.valueOf(el.getAttributeValue("y")).floatValue();
                    String material = el.getAttributeValue("material");
                    float angle = (float) Math.toRadians(Float.valueOf(el.getAttributeValue("angle")).floatValue());
                    float width = Float.valueOf(el.getAttributeValue("width")).floatValue();
                    float height = Float.valueOf(el.getAttributeValue("height")).floatValue();
                    Bal bal = new Bal(x, y, material, angle, width, height, model);
                }

                //Bozels
                for (int i = 0; i < bozels.size(); i++) {
                    Element el = (Element) bozels.get(i);
                    float x = Float.valueOf(el.getAttributeValue("x")).floatValue();
                    float y = Float.valueOf(el.getAttributeValue("y")).floatValue();
                    String kleur = el.getAttributeValue("type");
                    int id = Integer.valueOf(el.getAttributeValue("id")).intValue();
                    Bozel bozel = new Bozel(x, y, kleur, id, model);
                }

                //Targets
                for (int i = 0; i < doelen.size(); i++) {
                    Element el = (Element) doelen.get(i);
                    String type = el.getAttributeValue("type");
                    float x = Float.valueOf(el.getAttributeValue("x")).floatValue();
                    float y = Float.valueOf(el.getAttributeValue("y")).floatValue();
                    Doel doel = new Doel(x, y, type, model);
                }

            } catch (IOException ioexep) {
                System.out.println("IO @ Parser");
            } catch (JDOMException jdomexep) {
                System.out.println("JDOM @ Parser");
            }
            //Maak een nieuwe wereld en draad aan
            Thread thread = new Thread(wereld);
            model.setRun(true);
            thread.start();
        } else {
            model.setRun(false);
            model.setFirst(true);
            model.fireStateChanged();
            model.clearLists();
            new XMLParser(path, model, 0);
        }
    }
}
