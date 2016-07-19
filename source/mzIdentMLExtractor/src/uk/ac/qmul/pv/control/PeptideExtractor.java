/*
 * @(#) PeptideExtractor    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.control;

import uk.ac.qmul.pv.util.ProteinAccessionParser;
import com.google.gson.JsonArray;
import java.awt.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import uk.ac.qmul.pv.model.PeptideRecord;
import uk.ac.qmul.pv.util.JavaToJSON;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.Modification;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.SubstitutionModification;
import uk.ac.qmul.pv.db.DatabaseAccess;

/**
 * This class handles all the data access methods related to PSM. This class
 * supports threading functionality and it can be activated/deactivate in the
 * main controller class - ProteinViewer.
 *
 * @author Suresh Hewapathirana
 */
public class PeptideExtractor implements Runnable, DataExtractor {

    private String inputfile;
    private String outputfile;
    //private MzIdentMLUnmarshaller unmash;
    DatabaseAccess db;
    PeptideLocation pepLocation;
    HashMap<String, ArrayList<Point>> dbSeqCoordsHashMap;
    Map<String, DBSequence> dbSequenceIdHashMap;
    Map<String, PeptideEvidence> peptideEvidenceMap;

    public PeptideExtractor(String inputfile, String outputfile, Map<String, DBSequence> dbSequenceIdHashMap, Map<String, PeptideEvidence> peptideEvidenceMap) {
        this.inputfile = inputfile;
        this.outputfile = outputfile;

        try {
//            this.unmash = DatabaseAccess.getUnmarshaller(this.inputfile);
            db = DatabaseAccess.getInstance(inputfile);
            this.dbSequenceIdHashMap = dbSequenceIdHashMap;
            this.peptideEvidenceMap = peptideEvidenceMap;
        } catch (Exception e) {
            System.err.println("File Reading Error: " + e.getMessage());
        }
    }

    /**
     * This is the main function to export PSM data and convert data into JSON
     * format and save in an outputs file.
     */
    @Override
    public void export() {

        JsonArray peptideList = new JsonArray();
        dbSeqCoordsHashMap = new HashMap<>();

        Map<String, Peptide> peptideIdHashMap = db.getPeptideIdHashMap();
        ProteinAccessionParser accessionParser = new ProteinAccessionParser();

        Iterator it = peptideEvidenceMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            PeptideEvidence peptideEvidence = (PeptideEvidence) pair.getValue();

            PeptideRecord peptideRecord = new PeptideRecord();

            peptideRecord.setID(peptideEvidence.getId());
            if(peptideEvidence.getStart() != null){
                peptideRecord.setStartPossion(peptideEvidence.getStart());
            }
            if(peptideEvidence.getEnd() != null){
                peptideRecord.setEndPossion(peptideEvidence.getEnd());
            }

            DBSequence dbSeq = dbSequenceIdHashMap.get(peptideEvidence.getDBSequenceRef());
            if (dbSeq != null) {
                String accessionID = accessionParser.getAccessionID(dbSeq.getAccession());
                peptideRecord.setAccession(accessionID);
            }

            Peptide pep = peptideIdHashMap.get(peptideEvidence.getPeptideRef());
            if (pep != null) {
                peptideRecord.setSequence(pep.getPeptideSequence());
                String modifications = getAllModifications(pep);
                peptideRecord.setModifications(modifications);
                peptideRecord.setNoOfModifications(StringUtils.countMatches(modifications, ":"));
            }
            peptideList.add(JavaToJSON.peptideToJsonArray(peptideRecord));
            it.remove();
        }
        // Convert Protein list to JSON file
        JavaToJSON.toJSON(peptideList, this.outputfile);

    }

    /**
     * This method finds all the modifications for a given peptide.
     *
     * @param peptide The peptide which the modifications are interested in
     * @return each modification will be joined with a semi-colon and return as
     * a string
     * @see mzIdentML 1.1.1 specification page no 31
     */
    private String getAllModifications(Peptide peptide) {

        String modificationList = "";

        // A molecule modification specification. 
        // If n modifications have been found on a peptide, 
        // there should be n instances of Modification. 
        // If multiple modifications are provided as cvParams, 
        // it is assumed that the modification is ambiguous i.e. one modification or another. 
        // <xsd:element name="Modification" type="ModificationType" 
        // minOccurs="0" maxOccurs="unbounded"/>
        if (peptide.getModification() != null) {

            modificationList = peptide.getModification()
                    .parallelStream()
                    .map((modification) -> modificationsToString(modification) + ";")
                    .reduce(modificationList, String::concat);//                if (i > 0) {
//                    modificationList += ";"; //Add a separator between mods                                        
//                }
            // i++;
        }

        // Substitution Modification - 
        // A modification where one residue is substituted by another.
        // <xsd:element name="SubstitutionModification" 
        // type="SubstitutionModificationType" minOccurs="0" maxOccurs="unbounded"/>
        if (peptide.getSubstitutionModification() != null) {
            int i = 0;
            Iterator<SubstitutionModification> it = peptide.getSubstitutionModification().iterator();
            while (it.hasNext()) {
                SubstitutionModification subMod = it.next();
                if (i > 0 || !modificationList.equals("")) {
                    modificationList += ";"; //Add a separator between modifications                                        
                }
                modificationList += subModToString(subMod);
                i++;
            }
        }
        return modificationList;
    }

    /*
     * Method to convert an mzid Mod element into a string of type.
     * Eg:
     *  <Peptide id="TVDVGMGGVDLANLKACSGSGVSQELHIWGK_000000100000000000000000000000000">
     *      <PeptideSequence>TVDVGMGGVDLANLKACSGSGVSQELHIWGK</PeptideSequence>
     *          <Modification location="6" residues="M" monoisotopicMassDelta="15.994915">
     *              <cvParam accession="UNIMOD:35" name="Oxidation" cvRef="UNIMOD"/>
     *              <cvParam accession="MS:1001524" name="fragment neutral loss" cvRef="PSI-MS"
     *                  value="63.998285" unitAccession="UO:0000221" unitName="dalton" unitCvRef="UO"/>
     *          </Modification>
     *          ...
     *  </Peptide>
     */
    private String modificationsToString(Modification modification) {

        String modificationList = "";

        if (modification.getCvParam() != null) {

            String modificationName = "";
            Iterator<CvParam> it = modification.getCvParam().iterator(); 
            while (it.hasNext()) {
                CvParam cvParam = it.next();
                modificationName = cvParam.getName();
            }

            modificationList += modificationName;
        } else {

            if (modification.getMonoisotopicMassDelta() != null) {
                modificationList += modification.getMonoisotopicMassDelta();
            } else if (modification.getAvgMassDelta() != null) {
                modificationList += modification.getAvgMassDelta();
            }
        }

        // modification location
        if (modification.getLocation() != null) {
            modificationList += ":" + modification.getLocation();
        }
        return modificationList;

    }

    /*
     * Method to create and return a string representation of a substitution
     * modification
     */
    private String subModToString(SubstitutionModification subMod) {
        return subMod.getOriginalResidue() + " is replaced by "
                + subMod.getReplacementResidue() + " at :" + subMod.getLocation();

    }

    /**
     * Run PSM as a separate thread in parallel to protein
     */
    @Override
    public void run() {
        System.out.println(LocalDateTime.now() + ": Peptide : Started!!!");
        long start = System.currentTimeMillis();
        export();
        long end = System.currentTimeMillis();
        System.out.println(LocalDateTime.now() + ": Peptide : Finished!!!");
        System.out.println("Peptide : Data extraction took " + (end - start) + " milliseconds");

    }

}
