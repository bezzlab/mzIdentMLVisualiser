/*
 * @(#) JavaToJSON    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.FileWriter;
import java.io.IOException;
import uk.ac.qmul.pv.model.MetadataRecord;
import uk.ac.qmul.pv.model.PSMRecord;
import uk.ac.qmul.pv.model.PeptideRecord;
import uk.ac.qmul.pv.model.ProteinRecord;

/**
 *
 * @author Suresh Hewapathirana
 */
public class JavaToJSON {

    /**
     * This is converts list of proteins into JSON format. This output file is
     * formated into array list structure.
     *
     * @param proteinlist collection of proteins
     * @param outputFile full tile path to output JSON file
     */
    public static void toJSON(JsonArray proteinlist, String outputFile) {

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("data", proteinlist);

        try ( //write converted json data to a file
                FileWriter writer = new FileWriter(outputFile)) {
            writer.write(gson.toJson(jsonObject));

        } catch (IOException e) {
            System.out.println("File Input Output Error(JSON): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void metadataToJSON(MetadataRecord metadata, String outputFile) {

        Gson gson = new Gson();

        try ( //write converted json data to a file
                FileWriter writer = new FileWriter(outputFile)) {
            writer.write(gson.toJson(metadata));

        } catch (IOException e) {
            System.out.println("File Input Output Error(JSON): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static JsonArray proteinToJsonArray(ProteinRecord prot) {

        
        JsonArray protein = new JsonArray();
        protein.add(new JsonPrimitive(prot.getId()));
//        protein.add(new JsonPrimitive("<a href=http://www.uniprot.org/uniprot/"
//                + prot.getAccessionCode() + ">" + prot.getAccessionCode() + "</a>"));
        protein.add(new JsonPrimitive(prot.getAccessionCode()));
        protein.add(new JsonPrimitive(prot.getProteinName()));
        protein.add(new JsonPrimitive(prot.getSpeciesName()));
        protein.add(new JsonPrimitive(prot.getDistinctPeptides()));
        protein.add(new JsonPrimitive(prot.getPhdScore()));
        protein.add(new JsonPrimitive(prot.getCoverage()));
        return protein;
    }

    public static JsonArray peptideToJsonArray(PeptideRecord pep) {

        JsonArray peptide = new JsonArray();

        peptide.add(new JsonPrimitive(pep.getID()));
        peptide.add(new JsonPrimitive(pep.getSequence()));
        peptide.add(new JsonPrimitive(pep.getStartPossion()));
        peptide.add(new JsonPrimitive(pep.getEndPossion()));
        peptide.add(new JsonPrimitive(pep.getAccession()));
        peptide.add(new JsonPrimitive(pep.getModifications()));
        peptide.add(new JsonPrimitive(pep.getNoOfModifications()));
        
        return peptide;
    }
    
      public static JsonArray psmToJsonArray(PSMRecord psmRecord) {

        JsonArray psm = new JsonArray();

        psm.add(new JsonPrimitive(psmRecord.getId()));
//        psm.add(new JsonPrimitive(psmRecord.getRank()));
        psm.add(new JsonPrimitive(psmRecord.getChargeState()));
          psm.add(new JsonPrimitive(psmRecord.getCalculatedMassToCharge()));
        psm.add(new JsonPrimitive(psmRecord.getExperimentalMassToCharge()));
        
        return psm;
    }
      
      public static void sequenceToJSON(String sequence, String outputFile) {

        Gson gson = new Gson();

        try ( //write converted json data to a file
                FileWriter writer = new FileWriter(outputFile)) {
            writer.write(gson.toJson(sequence));

        } catch (IOException e) {
            System.out.println("File Input Output Error(JSON): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
