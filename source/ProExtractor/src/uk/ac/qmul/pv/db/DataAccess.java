/*
 * @(#) DataAccess    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.db;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisSoftware;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.ebi.jmzidml.model.mzidml.Peptide;
import uk.ac.ebi.jmzidml.model.mzidml.PeptideEvidence;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinAmbiguityGroup;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionHypothesis;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationItem;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationProtocol;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationResult;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

/**
 *
 * This class provides collection of java objects parsed from
 * MzIdentMLUnmarshaller class of jmzIdentML library
 *
 * @author Suresh Hewapathirana
 */
public class DataAccess {

    private MzIdentMLUnmarshaller unmash = null;
    private static volatile DataAccess instance = null;
    private Map<String, PeptideEvidence> peptideEvidenceIdHashMap = null;
    private Map<String, Peptide> peptideIdHashMap = null;
    private Map<String, DBSequence> dbSequenceIdHashMap = null;
    private Map<String, ProteinAmbiguityGroup> pagIdHashMap = null;
    private Map<String, ProteinDetectionHypothesis> pdhIdHashMap = null;
    private Map<String, SpectrumIdentificationResult> siiIdToSirHashMap = null;
    private Map<String, SpectrumIdentificationItem> siiHashMap = null;
    private Map<String, AnalysisSoftware> analysisSoftwareHashMap = null;

    private static SpectrumIdentificationProtocol sip = null;

    private DataAccess(String inputfile) {
        File file = new File(inputfile);

        if (file.exists()) {

            // If no unmarshaller object found, create it;
            // otherwise return existing object.
            if (unmash == null) {
                System.out.println(LocalDateTime.now() + ": File Unmarshalling: Started!!!");
                long start = System.currentTimeMillis();
                unmash = new MzIdentMLUnmarshaller(file);
                long end = System.currentTimeMillis();
            System.out.println(LocalDateTime.now() + ": File Unmarshalling : Finished!!!");
            System.out.println("File Unmarshalling took " 
                    + TimeUnit.MILLISECONDS.toSeconds(end - start) + " milliseconds");
            }
        } else {
            System.out.println("Sorry, Cannot find the input file in :" + inputfile);
            System.exit(2); /* Exit with error code */

        }
    }

    public static DataAccess getInstance(String inputfile) {
        try {
            if (instance == null) {
                synchronized (DataAccess.class) {
                    if (instance == null) {
                        instance = new DataAccess(inputfile);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("mzIdentML file access error : " + e.getMessage());
        }

        return instance;
    }

    /**
     * This function extracts <PeptideEvidence> tag data and copy to a hash map.
     *
     * @param unmash
     * @return Map<String, PeptideEvidence> type object - list of
     * PeptideEvidence as key:value where key : PeptideEvidence ID and value:
     * PeptideEvidence object
     */
    public Map<String, PeptideEvidence> getPeptideEvidenceIdHashMap() {
        if (peptideEvidenceIdHashMap == null) {
            peptideEvidenceIdHashMap = new HashMap<>();
            Iterator<PeptideEvidence> iterPeptideEvidence
                    = this.unmash.unmarshalCollectionFromXpath(MzIdentMLElement.PeptideEvidence);
            while (iterPeptideEvidence.hasNext()) {
                PeptideEvidence peptideEvidence = iterPeptideEvidence.next();
                peptideEvidenceIdHashMap.put(peptideEvidence.getId(), peptideEvidence);
            }
        }
        return peptideEvidenceIdHashMap;
    }

    /**
     * This function extracts <Peptide> tag data and copy to a hash map.
     *
     * @param unmash
     * @return Map<String, Peptide> type object - list of Peptide as key:value
     * where key : Peptide ID and value: Peptide object
     */
    public Map<String, Peptide> getPeptideIdHashMap() {
        if (peptideIdHashMap == null) {
            peptideIdHashMap = new HashMap<>();
            Iterator<Peptide> iterPeptide
                    = unmash.unmarshalCollectionFromXpath(MzIdentMLElement.Peptide);
            while (iterPeptide.hasNext()) {
                Peptide peptide = iterPeptide.next();
                peptideIdHashMap.put(peptide.getId(), peptide);
            }
        }
        return peptideIdHashMap;
    }

    /**
     * This function extracts <DBSequence> tag data and copy to a hash map.
     *
     * @return Map<String, DBSequence> type object - list of DBSequence as
     * key:value where key : DBSequence ID and value: DBSequence object
     */
    public Map<String, DBSequence> getDbSequenceIdHashMap() {
        if (dbSequenceIdHashMap == null) {
            dbSequenceIdHashMap = new HashMap<>();
            Iterator<DBSequence> iterDBSequence
                    = unmash.unmarshalCollectionFromXpath(MzIdentMLElement.DBSequence);

            while (iterDBSequence.hasNext()) {
                DBSequence dbSequence = iterDBSequence.next();
                dbSequenceIdHashMap.put(dbSequence.getId(), dbSequence);
            }
        }
        return dbSequenceIdHashMap;
    }

    /**
     * This function extracts <ProteinAmbiguityGroup> tag data and copy to a
     * hash map.
     *
     * @param unmash
     * @return Map<String, ProteinAmbiguityGroup> type object - list of
     * ProteinAmbiguityGroup as key:value where key : ProteinAmbiguityGroup ID
     * and value: ProteinAmbiguityGroup object
     */
    public Map<String, ProteinAmbiguityGroup> getPagIdHashMap() {
        if (pagIdHashMap == null) {
            pagIdHashMap = new HashMap<>();
            Iterator<ProteinAmbiguityGroup> iterDBSequence
                    = unmash.unmarshalCollectionFromXpath(MzIdentMLElement.ProteinAmbiguityGroup);

            while (iterDBSequence.hasNext()) {
                ProteinAmbiguityGroup pag = iterDBSequence.next();
                pagIdHashMap.put(pag.getId(), pag);
            }
        }
        return pagIdHashMap;
    }

    /**
     * This function extracts <ProteinDetectionHypothesis> tag data and copy to
     * a hash map.
     *
     * @param unmash
     * @return Map<String, ProteinDetectionHypothesis> type object - list of
     * ProteinDetectionHypothesis as key:value where key :
     * ProteinDetectionHypothesis ID and value: ProteinDetectionHypothesis
     * object
     */
    public Map<String, ProteinDetectionHypothesis> getPdhIdHashMap() {
        if (pdhIdHashMap == null) {
            pdhIdHashMap = new HashMap<>();
            Iterator<ProteinDetectionHypothesis> iterpdh
                    = unmash.unmarshalCollectionFromXpath(MzIdentMLElement.ProteinDetectionHypothesis);

            while (iterpdh.hasNext()) {
                ProteinDetectionHypothesis pdh = iterpdh.next();
                pdhIdHashMap.put(pdh.getId(), pdh);
            }
        }
        return pdhIdHashMap;
    }

    /**
     * This function extracts <SpectrumIdentificationResult> tag data and copy
     * to a hash map.
     *
     * @param unmash
     * @return Map<String, SpectrumIdentificationResult> type object - list of
     * SpectrumIdentificationResult as key:value where key :
     * SpectrumIdentificationResult ID and value: SpectrumIdentificationResult
     * object
     */
    public Map<String, SpectrumIdentificationResult> getSiiIdToSirHashMap() {
        if (siiIdToSirHashMap == null) {
            siiIdToSirHashMap = new HashMap<>();
            Iterator<SpectrumIdentificationResult> iterSIR
                    = unmash.unmarshalCollectionFromXpath(MzIdentMLElement.SpectrumIdentificationResult);

            while (iterSIR.hasNext()) {
                SpectrumIdentificationResult sir = iterSIR.next();
                siiIdToSirHashMap.put(sir.getId(), sir);
            }
        }
        return siiIdToSirHashMap;
    }

    public Map<String, SpectrumIdentificationItem> getSiiHashMap() {
        if (siiHashMap == null) {
            siiHashMap = new HashMap<>();
            Iterator<SpectrumIdentificationItem> iterSIR
                    = unmash.unmarshalCollectionFromXpath(MzIdentMLElement.SpectrumIdentificationItem);

            while (iterSIR.hasNext()) {
                SpectrumIdentificationItem sii = iterSIR.next();
                siiHashMap.put(sii.getId(), sii);
            }
        }
        return siiHashMap;
    }

    public Map<String, AnalysisSoftware> getAnalysisSoftwareHashMap() {
        if (analysisSoftwareHashMap == null) {
            analysisSoftwareHashMap = new HashMap<>();
            Iterator<AnalysisSoftware> iterSIR
                    = unmash.unmarshalCollectionFromXpath(MzIdentMLElement.AnalysisSoftware);

            while (iterSIR.hasNext()) {
                AnalysisSoftware software = iterSIR.next();
                analysisSoftwareHashMap.put(software.getId(), software);
            }
        }
        return analysisSoftwareHashMap;
    }

    public SpectrumIdentificationProtocol getSpectrumIdentificationProtocol() {
        if (sip == null) {
            sip = unmash.unmarshal(SpectrumIdentificationProtocol.class);
        }
        return sip;
    }
}
