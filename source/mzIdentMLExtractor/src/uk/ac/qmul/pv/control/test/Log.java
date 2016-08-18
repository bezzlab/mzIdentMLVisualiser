/*
 * @(#) Log    Version 1.0.0    02-09-2016
 *
 */

package uk.ac.qmul.pv.control.test;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * This is a simple utility class to log down the execution time eclipse 
 * for all the testing datasets along with their filename
 * 
 * @author sureshhewapathirana
 */
public class Log {

    /**
     * 
     * @param filename
     * @param totalTime Execution time in milliseconds
     */
    public void WriteResults(String filename, long totalTime) {

        try {
            String outputFile = "/Users/sureshhewapathirana/Documents/Projects/ResearchProject/mzIdentMLVisualiser/test/output.csv";
            FileWriter fw = new FileWriter(outputFile, true); //the true will append the new data
            fw.write(filename + "," + totalTime + "\n");//appends the string to the file
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
}
