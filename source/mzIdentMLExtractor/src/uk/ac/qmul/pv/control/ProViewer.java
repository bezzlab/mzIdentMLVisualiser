/*
 * @(#) ProViewer    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.control;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.qmul.pv.db.DatabaseAccess;

/**
 * This is the controller class to handle Protein and PSM data
 *
 * @author Suresh Hewapathirana
 */
public class ProViewer {

    public static void main(String[] args) {

        String inputFile = "";
        String datasetId = "";
        String outputFile = "";
        boolean isThreading = true;

        // get parameters from the command line
        if (args.length == 4) {
            inputFile = args[0];
            outputFile = args[1];
            datasetId = args[2];
            isThreading = Boolean.parseBoolean(args[3]);
        } else {
            System.out.println("You must pass 4 parameters!");
            System.exit(0);
        }

        // appending datasetId to outpuf filenames
        outputFile = outputFile + datasetId;

        // for the testing purpose only
        System.out.println("databaseId :" + datasetId);
        System.out.println("isThreading:" + isThreading);
        System.out.println("Input File :" + inputFile);
        System.out.println("outputFile:" + outputFile);

        DatabaseAccess db = DatabaseAccess.getInstance(inputFile);

        Map<String, DBSequence> dbSequenceIdHashMap = db.getDbSequenceIdHashMap();
        Map<String, PeptideEvidence> peptideEvidenceMap = db.getPeptideEvidenceIdHashMap();

        DataExtractor metadataHandler = new MetadataExtractor(inputFile, outputFile + "_metadata.json");
        DataExtractor proteinHandler = new ProteinExtractor(inputFile, outputFile + "_protein.json", dbSequenceIdHashMap, peptideEvidenceMap);
        DataExtractor peptideHandler = new PeptideExtractor(inputFile, outputFile + "_peptide.json", dbSequenceIdHashMap, peptideEvidenceMap);
        DataExtractor psmHandler = new PSMExtractor(inputFile, outputFile + "_psm.json");

        if (isThreading == true) {

            System.out.println("Programme Started!");

            // Create and run a thread to handle peptides
            Thread peptideThread = new Thread((Runnable) peptideHandler);
            peptideThread.start();

            // Create and run a thread to handle metadata
            Thread metadataThread = new Thread((Runnable) metadataHandler);
            metadataThread.start();

            // Create and run a thread to handle metadata
            Thread psmThread = new Thread((Runnable) psmHandler);
            psmThread.start();

            System.out.println(LocalDateTime.now() + ": Protein : Started!!!");
            long start = System.currentTimeMillis();
            proteinHandler.export();
            long end = System.currentTimeMillis();
            System.out.println(LocalDateTime.now() + ": Protein : Finished!!!");
            System.out.println("Protein : Data extraction took " + TimeUnit.MILLISECONDS.toSeconds(end - start) + " milliseconds");

        } else {

            System.out.println("Programme Started!");
            long startTotal = System.currentTimeMillis();

            System.out.println(LocalDateTime.now() + ": Protein : Started!!!");
            long start = System.currentTimeMillis();
            proteinHandler.export();
            long end = System.currentTimeMillis();
            System.out.println(LocalDateTime.now() + ": Protein : Finished!!!");
            System.out.println("Protein : Data extraction took " + (end - start) + " milliseconds");

            System.out.println(LocalDateTime.now() + ": Peptide : Started!!!");
            start = System.currentTimeMillis();
            peptideHandler.export();
            end = System.currentTimeMillis();
            System.out.println(LocalDateTime.now() + ": Peptide : Finished!!!");
            System.out.println("Peptide : Data extraction took " + (end - start) + " milliseconds");

            System.out.println(LocalDateTime.now() + ": Metadata : Started!!!");
            start = System.currentTimeMillis();
            metadataHandler.export();
            end = System.currentTimeMillis();
            System.out.println(LocalDateTime.now() + ": Metadata : Finished!!!");
            System.out.println("Metadata : Data extraction took " + (end - start) + " milliseconds");

            System.out.println(LocalDateTime.now() + ": Peptide Spectrum Match : Started!!!");
            start = System.currentTimeMillis();
            psmHandler.export();
            end = System.currentTimeMillis();
            System.out.println(LocalDateTime.now() + ": Peptide Spectrum Match : Finished!!!");
            System.out.println("Peptide Spectrum Match : Data extraction took " + (end - start) + " milliseconds");

            long endTotal = System.currentTimeMillis();
            System.out.println("Programme Finished!");
            System.out.println("Data extraction took " + (endTotal - startTotal) + " milliseconds");
        }
    }

}
