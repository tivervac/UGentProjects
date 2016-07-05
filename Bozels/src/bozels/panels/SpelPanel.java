package bozels.panels;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jbox.ExplosionRayCastCallback;
import jbox.Lijntjes;
import modellen.SpelModel;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author Titouan Vervack
 */
public class SpelPanel extends JPanel implements ChangeListener, MouseListener, MouseMotionListener {

    private SpelModel model;
    private int loper = 1;
    private Body body;
    private static final int VERHOGING = 100;
    private float katapultX;
    private float katapultY;
    private boolean drawElastiek = true;
    private int powerup = 1;
    private ImageIcon achtergrond;
    private ExplosionRayCastCallback raycast;

    public SpelPanel(SpelModel model) {
        this.model = model;
        model.addChangeListener(this);
        setPreferredSize(new Dimension(1024, 450));
        model.setPanel(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        //Maak een achtergrond
        achtergrond = new ImageIcon(SpelPanel.class.getResource("./darksunshine.png"));
    }

    //Teken alles
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Teken telkens de achtergrond
        if (achtergrond != null) {
            g.drawImage(achtergrond.getImage(), 0, 0, getWidth(), getHeight(), null);
        }

        //Zet de standaard coordinaten van de katpult
        katapultX = model.getKatapultX() * 7f;
        katapultY = 450 - (model.getKatapultY() * 7f) - VERHOGING;
        //Stel de anti-aliasing in
        RenderingHints aA = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //Stel de huidige bozel in
        Body bozel = model.getBozels().get(loper);
        //Overloop alle bodies
        for (Iterator it = model.getBodies().keySet().iterator(); it.hasNext();) {
            Body blok = (Body) it.next();
            if (blok != null) {
                //Maak een nieuwe grapics en stel de kleur in
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(model.getMap().get((String) model.getBodies().get(blok)).getColor());
                //Kijk of slapende objecten een andere kleur moeten krijgen zoja, verander de kleur
                if (!blok.isAwake() && model.getAwake()) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                }
                //Sla de postitie van de huidige blok op
                Vec2 pos = blok.getPosition().mul(7);
                g2.addRenderingHints(aA);

                //Teken een cirkel
                if (blok.getFixtureList().getShape() instanceof CircleShape) {
                    CircleShape shape = (CircleShape) blok.getFixtureList().getShape();
                    //Vraag de straal op aan JBox
                    int r = (int) (shape.m_radius * 7);
                    //Als de huidige blok gelijk is aan de huidige bozel en de bozel mag op de katapult geplaatst worden
                    if (bozel.equals(blok) && model.getFirst()) {
                        //Verplaats de bozel naar de katapult en zeg dat de volgende dit niet mag doen
                        blok.m_xf.position.set(new Vec2(katapultX / 7, (model.getKatapultY() * 7 + VERHOGING) / 7));
                        model.setFirst(false);
                    }
                    g2.translate((int) (pos.x), (int) (450 - pos.y));
                    g2.fillOval(-r, -r, r * 2, r * 2);
                } else {
                    PolygonShape shape = (PolygonShape) blok.getFixtureList().getShape();
                    //Vraag de hoekpunten op aan JBox
                    //Teken een driehoek
                    if (shape.m_vertexCount != 4) {
                        float h = (float) (14 / Math.sqrt(4.0f / 3.0f));
                        //Als de huidige blok gelijk is aan de huidige bozel en de bozel mag op de katapult geplaatst worden
                        if (bozel.equals(blok) && model.getFirst()) {
                            //Verplaats de bozel naar de katapult en zeg dat de volgende dit niet mag doen
                            blok.m_xf.position.set(new Vec2(katapultX / 7, (model.getKatapultY() * 7 + VERHOGING) / 7));
                            model.setFirst(false);
                        }
                        int[] xs = {0, -7, 7};
                        int[] ys = {-Math.round(h * (2f / 3f)), (int) -Math.round(-h / 3), (int) -Math.round(-h / 3)};
                        g2.translate((int) pos.x, (int) (450 - pos.y));
                        g2.rotate(-blok.getAngle());
                        g2.drawPolygon(xs, ys, shape.m_vertexCount);
                        //Teken een rechthoek
                    } else {
                        //Vraag de breedte en de hoogte op aan JBox
                        float breedte = shape.m_vertices[2].x * 14;
                        float hoogte = shape.m_vertices[2].y * 14;
                        //Als de huidige blok gelijk is aan de huidige bozel en de bozel mag op de katapult geplaatst worden
                        if (bozel.equals(blok) && model.getFirst()) {
                            //Verplaats de bozel naar de katapult en zeg dat de volgende dit niet mag doen
                            blok.m_xf.position.set(new Vec2(katapultX / 7, (model.getKatapultY() * 7 + VERHOGING) / 7));
                            model.setFirst(false);
                        }
                        g2.translate((int) pos.x, (int) (450 - pos.y));
                        g2.rotate(-blok.getAngle());
                        g2.fillRect((int) -(breedte / 2), (int) -(hoogte / 2), (int) breedte, (int) hoogte);
                    }
                }

                //Teken indien nodig zwaartepunt
                if (model.getDrawZwaartepunt()) {
                    g2.setColor(Color.RED);
                    g2.fillOval(-2, -2, 4, 4);
                }

                g.setColor(Color.RED);
                //Teken indien nodig elastiek naar de bozel anders naar het middelpunt van de katapult
                if (drawElastiek) {
                    g.drawLine((int) katapultX, (int) katapultY, (int) bozel.getPosition().x * 7, (int) (450 - (bozel.getPosition().y * 7)));
                } else {
                    g.drawLine((int) katapultX, (int) katapultY, (int) katapultX, (int) katapultY);
                }

                //Teken indien nodig de snelheid, in een andere graphics want de vorige is al getranslate/rotate
                if (model.getDrawSnelheid() && blok.isAwake()) {
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.setColor(Color.MAGENTA);
                    g3.translate(pos.x, 450 - pos.y);
                    g3.drawLine(0, 0, (int) (blok.getLinearVelocity().x * 7), (int) -(blok.getLinearVelocity().y * 7));
                }

                //Teken indien nodig de rays
                if (model.getDrawRays() && raycast != null) {
                    for (Lijntjes r : raycast.getRays()) {
                        g.drawLine((int) r.getBegin().x * 7, (int) (450 - (r.getBegin().y * 7)), (int) r.getKracht().x * 7, (int) (450 - (r.getKracht().y * 7)));
                    }
                }
            }
        }

        if (body != null) {
            //Als dit niet de laatste bozel is
            if (model.getBozels().get(loper + 1) != null) {
                //Als de bozel in vlucht is
                if (!body.isAwake()) {
                    //Verwijder de bozel uit de nodige mappen en in de wereld en zet de powerup weer op 1
                    destroyBody(body);
                    //Stel de volgende bozel in
                    loper++;
                    body = model.getBozels().get(loper);
                    //Laat de volgende bozel klaarzetten en teken de elastiek naar de bozel
                    model.setFirst(true);
                    drawElastiek = true;
                }
                //Als dit de laatste bozel is
            } else {
                //Als de bozel in vlucht is
                if (!body.isAwake()) {
                    destroyBody(body);
                    loper = 0;
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (body != null) {
            //Als het het een witte/zwarte bozel is doe hem ontploffen op een muisklik
            if ("white".equals(model.getBodies().get(body)) || "black".equals(model.getBodies().get(body))) {
                Vec2 pos = body.getPosition();
                raycast = new ExplosionRayCastCallback(pos);
                for (int i = 0; i < 72; i++) {
                    try {
                        float hoek = (float) (Math.PI / 36 * i);
                        float x1 = (float) (Math.cos(hoek) * 10.0f) + pos.x;
                        float y1 = (float) (Math.sin(hoek) * 10.0f) + pos.y;
                        Vec2 positie2 = new Vec2(x1, y1);
                        model.getWorld().raycast(raycast, pos, positie2);
                        raycast.addRay();
                    } catch (NullPointerException ex) {
                    }
                }
                raycast.explode();
                model.setRaycast(raycast);
                
                //Verwijder de ontplofte bozel en zet de volgende klaar
                body.setAwake(true);
                if (model.getBozels().get(loper + 1) != null) {
                    model.setFirst(true);
                    drawElastiek = true;
                    destroyBody(body);
                    loper++;
                    body = model.getBozels().get(loper);
                } else {
                    destroyBody(body);
                    loper = 0;
                }
                //Verdrievoudig de kracht van de bozel indien hij geel is en doe -1 op powerup zodat hij het niet opnieuw kan gebruiken
            } else if ("yellow".equals(model.getBodies().get(body)) && powerup > 0) {
                body.setLinearVelocity(body.getLinearVelocity().mul(3f));
                powerup--;
                //Verwijder de gewone bozel, zodat je niet moet wachten tot hij slaapt, en zet de volgende klaar
            } else {
                destroyBody(body);
                if (model.getBozels().get(loper + 1) != null) {
                    loper++;
                    body = model.getBozels().get(loper);
                    model.setFirst(true);
                    drawElastiek = true;
                } else {
                    model.setFirst(true);
                    loper = 0;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        body = model.getBozels().get(loper);
        if (body != null) {
            //Als de bozel niet in vlucht is
            if (!body.isActive()) {
                float x = e.getX();
                float y = (450 - e.getY());
                if (!model.getBozels().isEmpty()) {
                    //Formules zoals ze in de pdf te vinden zijn
                    Vec2 r1 = new Vec2(x / 7, y / 7);
                    Vec2 r0 = new Vec2(model.getKatapultX(), model.getKatapultY() + (VERHOGING / 7));
                    Vec2 rd = r0.sub(r1);
                    Vec2 elastiek = rd.mul(Math.min(rd.normalize(), 7.0f));
                    Vec2 multi = rd.mul((float) elastiek.length());
                    Vec2 pos = r0.sub(multi);
                    verplaatsBody(body, pos.x * 7, pos.y * 7);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //Formules zoals ze in de pdf te vinden zijn
        if (!body.isActive()) {
            //Reken de kracht uit en voer hem uit op de bozel
            body.setActive(true);
            drawElastiek = false;
            float x = katapultX - e.getX();
            float y = katapultY - e.getY();
            Vec2 vec = new Vec2(x, y);
            float len = vec.length();
            Vec2 l = new Vec2(x * len, 450 - y * len);
            body.setLinearVelocity(l.mul(model.getLanceerkracht() / 10000000));
        }
    }

    //Verplaats de body
    public void verplaatsBody(Body b, float x, float y) {
        b.setTransform(new Vec2(x / 7, y / 7), b.getAngle());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //Als de bozel niet in vlucht is
        if (!body.isActive()) {
            float x = e.getX();
            float y = (450 - e.getY());
            if (!model.getBozels().isEmpty()) {
                //Formules zoals ze in de pdf te vinden zijn
                Vec2 r1 = new Vec2(x / 7, y / 7);
                Vec2 r0 = new Vec2(model.getKatapultX(), model.getKatapultY() + (VERHOGING / 7));
                Vec2 rd = r0.sub(r1);
                Vec2 elastiek = rd.mul(Math.min(rd.normalize(), 7.0f));
                Vec2 multi = rd.mul((float) elastiek.length());
                Vec2 pos = r0.sub(multi);
                verplaatsBody(body, pos.x * 7, pos.y * 7);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    //Zet de variabelen terug naar hun originele waarden wanneer het spel herstart wordt
    @Override
    public void stateChanged(ChangeEvent e) {
        //Om zeker te zijn dat er een herstart is geweest, dit wordt ook opgeroepen wanneer een ander element in de JList wordt geselecteerd
        if (model.getBozels().isEmpty()) {
            loper = 1;
            powerup = 1;
            drawElastiek = true;
            repaint();
        }
    }

    //Verwijder eeen bozel
    public void destroyBody(Body b) {
        model.getBodies().remove(b);
        model.getDestroy().push(b);
        model.getBozels().remove(loper);
        powerup = 1;
    }
}