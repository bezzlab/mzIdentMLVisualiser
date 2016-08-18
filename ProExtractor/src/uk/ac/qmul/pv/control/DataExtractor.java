/*
 * @(#) DataExtractor    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.control;

/**
 * This is an interface with one method forcing developers to implement a method 
 * called export in order to extract data from mzIdentML
 * 
 * @author sureshhewapathirana
 */
public interface DataExtractor {
    
    // should provide export method to extract data
    public void export();
    
}
