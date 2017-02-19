package org.aikodi.lang.funky.behavior.description;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * A basic textual description of behavior.
 * 
 * @author Marko van Dooren
 */
public class TextualDescription extends ElementImpl implements Description {

   private String _text;

   /**
    * Create a new textual description of behavior from the given text.
    * 
    * @param text A text that describes the behavior.
    */
   @Requires({"text != null"})
   public TextualDescription(String text) {
      this._text = text;
   }

   /**
    * @return The text that describes the behavior.
    */
   @Ensures({"result != null"})
   public String text() {
      return _text;
   }

   /**
    * Set the textual description of behavior.
    * 
    * @param text The textual desription of behavior.
    */
   @Requires({"text != null"})
   @Ensures({"text().equals(text)"})
   public void setText(String text) {
      this._text = text;
   }
   
   @Override
   protected Element cloneSelf() {
      return new TextualDescription(text());
   }

    @Override
    public String toString(){
        return _text;
    }
}
