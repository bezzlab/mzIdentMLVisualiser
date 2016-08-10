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
import uk.ac.qmul.pv.db.DataAccess;

/**
 * This is the controller class to handle the flow of execution
 * This library is should be executed in a command-line
 * 
 * @author Suresh Hewapathirana
 * 
 * USAGE: java -jar -Xmx<size> "mzIdentMLExtractor.jar" <inputFile> <OutputDirectory> <UniqueID> <boolean value for Multithreading>
 * 
 * EXAMPLE: java -jar -Xmx256m "/Users/sureshhewapathirana/Documents/Projects/ResearchProject/mzIdentMLVisualiser/source/mzIdentMLExtractor/dist/mzIdentMLExtractor.jar" "/Users/sureshhewapathirana/Downloads/galaxy/database/files/000/dataset_1.dat" "/Users/sureshhewapathirana/Downloads/galaxy/config/plugins/visualizations/protviewer/static/data/" "f2db41e1fa331b3e" "true"
 */
public class ProViewer {

    public static void main(String[] args) {
        
        String inputFile = "/Users/sureshhewapathirana/Desktop/Testdata/Galaxy5-[MSGF__MSMS_Search_on_data_33_and_data_3].mzid"; 
        String datasetId = "Galaxy5";
        String outputFile = "/Users/sureshhewapathirana/Desktop/1/";
        boolean isThreading = true;
        //Log log = new Log();

//        String inputFile = ""; 
//        String datasetId = "";
//        String outputFile = "";
//        boolean isThreading = true;

//         get parameters from the command line
//        if (args.length == 4) {
//            inputFile = args[0];
//            outputFile = args[1];
//            datasetId = args[2];
//            isThreading = Boolean.parseBoolean(args[3]);
//        } else {
//            System.err.println("You must pass four parameters!"
//                    + "\n 1.Input mzIdentML file"
//                    + "\n 2. Output directory for JSON files"
//                    + "\n 3. Unique dataset ID for output files"
//                    + "\n 4. Boolean value to enable/disable multithreading"
//                    + "\n\n USAGE: java -jar mzIdentMLExtractor.jar <InputFile> <OutputDir> <UniqueId> <Multithreading>");
//            System.exit(0); // immediate exit
//        }

        // appending datasetId(unique id) to output filenames
        outputFile = outputFile + datasetId;

        // for the debugging purpose only
        System.out.println("MzIdentML Extractor INFO: " 
                + inputFile + " "
                + outputFile + " "
                + datasetId + " "
                + isThreading);

        // initialised database access object
        DataAccess db = DataAccess.getInstance(inputFile);
        
        // get protein sequence and peptide evidance records from input file
        Map<String, DBSequence> dbSequenceIdHashMap = db.getDbSequenceIdHashMap();
        Map<String, PeptideEvidence> peptideEvidenceMap = db.getPeptideEvidenceIdHashMap();
        
        // instantiate extractor classes
        DataExtractor metadataHandler = new MetadataExtractor(inputFile, outputFile + "_metadata.json");
        DataExtractor proteinHandler = new ProteinExtractor(inputFile, outputFile + "_protein.json", dbSequenceIdHashMap, peptideEvidenceMap);
        DataExtractor peptideHandler = new PeptideExtractor(inputFile, outputFile + "_peptide.json", dbSequenceIdHashMap, peptideEvidenceMap);
        DataExtractor psmHandler = new PSMExtractor(inputFile, outputFile + "_psm.json");

        if (isThreading == true) {
           long startTotal = System.currentTimeMillis();

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
            
            proteinHandler.export();
            long endTotal = System.currentTimeMillis();
            System.out.println(LocalDateTime.now() + ": Protein : Finished!!!");
            long totaltime = TimeUnit.MILLISECONDS.toSeconds(endTotal - startTotal);
            System.out.println("total" + Long.toString(totaltime));
            // for testing purpose
            // log.WriteResults(inputFile, totaltime);
            System.out.println("Protein : Data extraction took " 
                    + totaltime + " milliseconds");
            

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
