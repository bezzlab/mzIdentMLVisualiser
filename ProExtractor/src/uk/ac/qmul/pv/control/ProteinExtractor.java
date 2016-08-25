/*
 * @(#) ProteinExtractor    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.control;

import uk.ac.qmul.pv.util.ProteinAccessionParser;
import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import uk.ac.qmul.pv.model.ProteinRecord;
import uk.ac.qmul.pv.util.JavaToJSON;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinAmbiguityGroup;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;
import uk.ac.qmul.pv.db.DataAccess;
import uk.ac.qmul.pv.util.CoverageUtil;
import uk.ac.qmul.pv.model.Interval;
import uk.ac.qmul.pv.util.CV;

/**
 * This class handles all the data access methods related to proteins.
 *
 * @author Suresh Hewapathirana
 */
public class ProteinExtractor implements DataExtractor {

    private String inputFile;
    private String outputFile;

    DataAccess db;
    private Map<String, DBSequence> dbSequenceIdHashMap;
    private Map<String, ProteinAmbiguityGroup> pagHashMap;
    private Map<String, PeptideEvidence> peptideEvidenceMap;
    private HashMap<String, ArrayList<Interval>> dbSeqPeptideCoordsHashMap;
    private PeptideLocation peptideLocations;

    /**
     * Construction of ProteinExtractor Class.
     *
     * @param inputFile Full file path for input mzIdentML file(mzid file)
     * @param outputFile Full file path for output JSON files
     * @param dbSequenceIdHashMap Database sequence records
     * @param peptideEvidenceMap Peptide evidence records
     */
    public ProteinExtractor(String inputFile, 
            String outputFile, 
            Map<String, DBSequence> dbSequenceIdHashMap, 
            Map<String, PeptideEvidence> peptideEvidenceMap) {

        this.inputFile = inputFile;
        this.outputFile = outputFile;

        try {

            this.db = DataAccess.getInstance(this.inputFile);
            this.pagHashMap = db.getPagIdHashMap();
            this.dbSequenceIdHashMap = dbSequenceIdHashMap;
            this.peptideEvidenceMap = peptideEvidenceMap;
            
            // peptide locations in the protein
            this.peptideLocations = new PeptideLocation();
            this.dbSeqPeptideCoordsHashMap 
                    = peptideLocations.getPeptideCoordinatesHashMap(this.peptideEvidenceMap);

        } catch (Exception e) {
            System.err.println("Initialisation Error:" + e.getMessage());
        }

    }

    /**
     * This is the main controlling function to export protein data and 
     * convert data into JSON format and save in an outputs file.
     *
     */
    @Override
    public void export() {

        JsonArray proteinList = new JsonArray();
        ProteinRecord protein = new ProteinRecord();

        // There can be zero or one ProteinDetectionList
        // Therefore check for empty list of ProteinAmbiguityGroup
        // <xsd:element name="ProteinAmbiguityGroup" 
        // type="ProteinAmbiguityGroupType" minOccurs="0" maxOccurs="unbounded"/>
        if (!pagHashMap.isEmpty()) {
            
            // travel through each ProteinAmbiguityGroup
            Iterator<ProteinAmbiguityGroup> it = pagHashMap.values().iterator();
           
            while (it.hasNext()) {
                
                // get the current ProteinAmbiguityGroup
                ProteinAmbiguityGroup pag = it.next();
                
                // get the representative protein of the protein group
                ProteinDetectionHypothesis representativePHD = getRepresentativePDH(pag);
                if (representativePHD != null) {
                    
                    // extract these data from ProteinDetectionHypothesis:
                    // Accession, OS, Protein Name, #Distinct, score
                    protein = pdhToProtein(representativePHD);
                }
                
                // if still couldn't find the score value, try to find them in CV parameters of ProteinAmbiguityGroup.
                // For example Peptide shaker report score like this.
                if(protein.getPhdScore()== -1){
                    Iterator<CvParam> pegcvParam = pag.getCvParam().iterator();
                    while (pegcvParam.hasNext()) {
                
                        // get the current cv parameter
                        CvParam cvParam = pegcvParam.next();
                         if (cvParam.getAccession().equals(CV.PEPTIDESHAKER_SCORE)){
                             protein.setPhdScore(Double.parseDouble(cvParam.getValue()));
                         }
                    }
                }
                // if still cannot find score, make it 0
                if(protein.getPhdScore()== -1){
                    protein.setPhdScore(0.00);
                }
                // add protein into protein list as json object
                proteinList.add(JavaToJSON.proteinToJsonArray(protein));
            }
            // Convert Protein list to JSON file
            JavaToJSON.toJSON(proteinList, this.outputFile);
        }else{
            // add protein into protein list as json object
            proteinList.add(JavaToJSON.proteinToJsonArray(protein));
            // Convert Protein list to JSON file
            JavaToJSON.toJSON(proteinList, this.outputFile);
        }
    }

    /**
     * This method finds the representative protein of the protein group
     * @param pag Protein AmbiguityG roup 
     * @return Protein Detection Hypothesis - representative protein
     */
    private ProteinDetectionHypothesis getRepresentativePDH(ProteinAmbiguityGroup pag) {

        ProteinDetectionHypothesis representativePDH = null;
        boolean isRepresentativeFound = false;
        
        // travel though each protein in the protein group
        Iterator<ProteinDetectionHypothesis> it = pag.getProteinDetectionHypothesis().iterator();

        while (it.hasNext()) {
            
            // get the corrent protein
            ProteinDetectionHypothesis pdh = it.next();
            
            // assume no representative protein found
            isRepresentativeFound = false;
            
            // travel though each cv parameter
            Iterator<CvParam> itcvParam = pdh.getCvParam().iterator();
            while (itcvParam.hasNext()) {
                
                // get the current cv parameter
                CvParam cvParam = itcvParam.next();
                
                // check whether the current protein is a representative protein
                if (cvParam.getAccession().equals(CV.ANCHOR_PROTEIN_CV_TERM)
                        || cvParam.getAccession().equals(CV.GROUP_REP_PROTEIN_CV_TERM)) {
                    representativePDH = pdh;
                    isRepresentativeFound = true;
                    break;
                }
            }
        }
        // in the absence of a anchor protein, take the first one as the representative
        if (isRepresentativeFound == false) {
            representativePDH = pag.getProteinDetectionHypothesis().get(0);
        }
        return representativePDH;
    }

    /**
     * This method extract data from Protein Detection Hypothesis and temporary
     * saved in a protein object.
     *
     * @param pdh representative ProteinDetectionHypothesis for a PAG
     * @return protein Object that store information about the protein
     */
    private ProteinRecord pdhToProtein(ProteinDetectionHypothesis pdh) {

        ProteinRecord protein;
        ProteinAccessionParser accessionParser = new ProteinAccessionParser();
        
        // assume protein coverage has not reported in mzIdentML file
        boolean isCoverageAvailable = false; 

        // get mapping protein(DBSequence) associated with protein ambigiuty group
        DBSequence dbSeq = dbSequenceIdHashMap.get(pdh.getDBSequenceRef());

        // filtering accession, protein name and the species name
        protein = accessionParser.getAccessionInfo(dbSeq);

        // DbSequence unique ID
        protein.setId(dbSeq.getId());
        
        if(dbSeq.getSeq()!= null){
           protein.setIsSequenceAvailable(true);
        }
        
        // travel though each CV Parameter to find the score and #distinct peptides
        Iterator<CvParam> it = pdh.getCvParam().iterator();
        while (it.hasNext()) {
            
            // get the current CvParam
            CvParam cvParam = it.next();
            if (cvParam.getValue() != null) {
                if(cvParam.getAccession().equals(CV.DISTINCT_PEPTIDES)){
                    protein.setDistinctPeptides(Integer.parseInt(cvParam.getValue()));
                }
                if (cvParam.getName().toLowerCase().contains("score") || (cvParam.getAccession().equals(CV.SCAFFOLD_PROTEIN_PROBABILITY))){
                    protein.setPhdScore(Double.parseDouble(cvParam.getValue())); 
                }
                if(cvParam.getAccession().equals(CV.SEQUENCE_COVERAGE)){
                    protein.setCoverage(Double.parseDouble(cvParam.getValue()));
                    isCoverageAvailable = true;
                }
                // match with CV parameters
                switch (cvParam.getAccession()) {
                    case CV.DISTINCT_PEPTIDES:
                        protein.setDistinctPeptides(Integer.parseInt(cvParam.getValue()));
                    case CV.PDH_SCORE:
                        protein.setPhdScore(Double.parseDouble(cvParam.getValue()));
                        break;
                    case CV.MASCOT_SCORE:
                        protein.setPhdScore(Double.parseDouble(cvParam.getValue()));
                        break;
                     case CV.PEPTIDESHAKER_SCORE:
                        protein.setPhdScore(Double.parseDouble(cvParam.getValue()));
                        break;
                }
            }
        }
        // protein coverage
        if(isCoverageAvailable == false){
            protein.setCoverage(getProteinCoverage(pdh));
        }
        return protein;
    }

    /**
     * This method calculates protein Coverage
     * @param repPdh Representative ProteinDetectionHypothesis
     * @return Protein Coverage in double 
     */
    private double getProteinCoverage(ProteinDetectionHypothesis repPdh) {

        int proteinLength = -1;
        int totalcoverage = 0;
        Double totalPeptideCoverage = 0.00;
        
        CoverageUtil coverage = new CoverageUtil();
        String proteinDbSeqRef = repPdh.getDBSequenceRef();

        // get database sequences
        DBSequence proteinDbSeq = dbSequenceIdHashMap.get(proteinDbSeqRef);
        if (proteinDbSeq == null) {
            return 0.00;
        }

        // if length is not available, but sequence is available, then
        // we can calculate the length from the sequence
        // otherwise directly take from the protein
        if (proteinDbSeq.getLength() == null) {
            if (proteinDbSeq.getSeq() != null) {
                proteinLength = proteinDbSeq.getSeq().length();
            }
        } else {
            proteinLength = proteinDbSeq.getLength();
        }

        // check whether peptide coordinates are available
        if (dbSeqPeptideCoordsHashMap != null && !dbSeqPeptideCoordsHashMap.isEmpty()) {

            // get all the ranges
            ArrayList<Interval> currentPeptideCoords = dbSeqPeptideCoordsHashMap.get(proteinDbSeqRef);
            // merge overapped ranges
            ArrayList<Interval> mergedCoords = coverage.merge(currentPeptideCoords);
            //System.out.println("mergedCoords length:" + mergedCoords.size());

            // get the total length of each non-overlaping ranges
            // used concurrent programming with parallel stream
            totalcoverage = mergedCoords
                    .parallelStream()
                    .map((interval) -> interval.getGap())
                    .reduce(totalcoverage, Integer::sum);
            
            // include both end to coverage
            // this means if a peptide start from 1 and end with 10
            // its length is 10-1 = 9
            // but for coverage it should be 10
            totalcoverage  += mergedCoords.size();

            try {
                if ((proteinLength != 0) && (proteinLength != -1)) {
                    totalPeptideCoverage = (totalcoverage / (double) proteinLength) * 100;
                } else {
                    return 0.00;
                }
            } catch (ArithmeticException e) {
                System.err.println("Protein Coverage calculation error: " + e.getMessage());
                return totalPeptideCoverage;
            } catch (Exception e) {
                System.err.println("Protein Coverage error: " + e.getMessage());
                return totalPeptideCoverage;
            }
        } else {
            // no peptide found
            return totalPeptideCoverage;
        }
        return totalPeptideCoverage;
    }
}
