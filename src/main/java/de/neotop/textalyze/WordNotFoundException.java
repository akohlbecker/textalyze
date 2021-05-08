/**
* Copyright (C) 2021 Anderas Kohlbecker
* http://neotop.de
*
* The contents of this file are subject to the Mozilla Public License Version 2.0
* See LICENSE.TXT at the top of this package for the full license terms.
*/
package de.neotop.textalyze;

/**
 * @author a.kohlbecker
 */
public class WordNotFoundException extends Exception {

    private static final long serialVersionUID = -3170854434842694386L;

    /**
     * @param message
     */
    public WordNotFoundException(String word) {
        super("The word '" + word + "' is not found in the analyzed text");
    }

}
