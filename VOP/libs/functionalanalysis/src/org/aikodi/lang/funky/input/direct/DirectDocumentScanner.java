package org.aikodi.lang.funky.input.direct;

import org.aikodi.chameleon.workspace.DocumentScannerImpl;

/**
 * A scanner that does nothing by default. All document loades must be added
 * externally.
 * @author Marko van Dooren
 */
public class DirectDocumentScanner extends DocumentScannerImpl {

   @Override
   public String label() {
      return "Direct scanner";
   }

}