/*
 * @(#) PeptideLocation    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.qmul.pv.model.Interval;

/**
 * This class was written by
 *
 * Chan, A.S.L. (2015) New Tool for Visualising Proteomics Results. Unpublished
 * MSc thesis. Queen Mary - University of London.
 *
 * Optimized by introducing parallel streaming
 *
 * @author Modified by Suresh Hewapathirana
 */
public class PeptideLocation {

    /**
     *
     * @param iterPeptideEvidence PeptideEvidence iteration object
     * @link uk.ac.qmul.pv.control.ProteinExtractor#ProteinExtractor
     * @return peptide start and end locations for each protein
     */
    HashMap<String, ArrayList<Interval>> getPeptideCoordinatesHashMap(Map<String, PeptideEvidence> iterPeptideEvidence) {

        Map<String, PeptideEvidence> iterPepEvid = iterPeptideEvidence;

        HashMap<String, ArrayList<Interval>> dbSeqCoordsHashMap = new HashMap<>();

        try {
            // travel though each peptideEvidece, but with a parallel processing
            iterPepEvid.values()
                    .stream()
                    .forEach((peptideEvidence) -> {
                        // get the corresponding database sequence id
                        String dbSeqRef = peptideEvidence.getDBSequenceRef();
                        // get the range of the peptide
                        Interval peptideCoordinates = new Interval(peptideEvidence.getStart(), peptideEvidence.getEnd());
                      
                            if (dbSeqCoordsHashMap.containsKey(dbSeqRef)) {
                                ArrayList peptideCoordinatesList = dbSeqCoordsHashMap.get(dbSeqRef);
                                peptideCoordinatesList.add(peptideCoordinates);
                                dbSeqCoordsHashMap.put(dbSeqRef, peptideCoordinatesList);
                            } else {
                                ArrayList peptideCoordinatesList = new ArrayList<>();
                                peptideCoordinatesList.add(peptideCoordinates);
                                dbSeqCoordsHashMap.put(dbSeqRef, peptideCoordinatesList);
                            }
                       
                    });

        } catch (Exception e) {
            System.err.println("Peptide location retrieval error: " + e.getMessage());
        }

        return dbSeqCoordsHashMap;
    }
}
