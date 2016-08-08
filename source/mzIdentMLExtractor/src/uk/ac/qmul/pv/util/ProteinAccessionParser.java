/*
 * @(#) ProteinAccessionParser    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.util;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.DBSequence;
import uk.ac.qmul.pv.model.ProteinRecord;

/**
 *
 * These function were taken from: Chan, A.S.L. (2015) New Tool for Visualising
 * Proteomics Results. Unpublished MSc thesis. Queen Mary - University of
 * London.
 *
 * Modified by Suresh Hewapathirana
 *
 * @author Suresh Hewapathirana
 */
public class ProteinAccessionParser {

    /**
     * This methods extracts accession, protein name and the species name from
     * protein records by using regular expressions.
     *
     * @param proteinDbSeq DBSequence records representing a protein sequence
     * @return protein The object that store information about the protein
     */
    public ProteinRecord getAccessionInfo(DBSequence proteinDbSeq) {

        ProteinRecord protein = new ProteinRecord();
        String speciesName = "NA";
        

        // 1) Set accession
        String proteinDbAccession = proteinDbSeq.getAccession();
        protein.setAccessionCode(getAccessionID(proteinDbAccession));

        // 2) Species Name and 3) Protein Name
        // Get the list of CvParam which are associated with DBSequence
        // <xsd:group ref="ParamGroup" minOccurs="0" maxOccurs="unbounded">
        // <xsd:documentation> Additional descriptors for the sequence, such as taxon, description line etc
        List<CvParam> DbSeqCvParamList = proteinDbSeq.getCvParam();
        Iterator<CvParam> it = DbSeqCvParamList.iterator();
        while (it.hasNext()) {
            CvParam DbSeqCvParam = it.next();
            if (DbSeqCvParam.getAccession().equals(CV.PROTEIN_DESCRIPTION)) {

                String proteinNameFull = DbSeqCvParam.getValue();

                // Regex to extract the accession code, species name and protein name from CvParam
                // This is not always available, filler values e.g. "Not Available"
                // or short forms extracted from DB Sequence will be put if this is not available             
                // Full name of species (short form extracted from DB Sequence)
                Pattern patternSpe = Pattern.compile("OS=(.*?)GN");
                Matcher matcherSpe = patternSpe.matcher(proteinNameFull);
                if (matcherSpe.find()) {
                    speciesName = matcherSpe.group(1);  // species full name
                } else {
                    // The code for the species is extracted from the Db Accession 
                    // Because sometimes the full species name (in the CvParam) is not available
                    // In these cases only the 5-alphanumeric character code for species is shown
                    Pattern patternSpeShort = Pattern.compile(".*_(.*)$");
                    Matcher matcherSpeShort = patternSpeShort.matcher(proteinDbAccession);

                    if (matcherSpeShort.find()) {
                        speciesName = matcherSpeShort.group(1); // species short name
                    }
                }

                // Full name of protein (replace default "Not Available" value)               
                Pattern patternName = Pattern.compile("\\s(.*?)OS=");
                Matcher matcherName = patternName.matcher(proteinNameFull);
                if (matcherName.find()) {
                    protein.setProteinName(matcherName.group(1));
                }else{
                    // shorten protein description
                    if(proteinNameFull.length()>50){
                        proteinNameFull = proteinNameFull.substring(0, 50) + "...";
                    }
                    protein.setProteinName(proteinNameFull);
                }
            }
        }

        protein.setSpeciesName(speciesName);

        return protein;
    }

    /**
     * This method extracts protein accession ID associated with DBSequence. A
     * regular expression has been provided by Uniprot to extract accession ID.
     *
     * @param proteinDbAccession DBSequence accession to extract Uniprot ID
     * @return uniprotAccessionID
     * @see http://web.expasy.org/docs/userman.html
     */
    public String getAccessionID(String proteinDbAccession) {

        final String UNIPROT_ACCESSION_REGX = "[OPQ][0-9][A-Z0-9]{3}[0-9]|[A-NR-Z][0-9]([A-Z][A-Z0-9]{2}[0-9]){1,2}";
        String uniprotAccessionID = "NA";

        if (proteinDbAccession != null) {
            Pattern patternAcc = Pattern.compile(UNIPROT_ACCESSION_REGX);
            Matcher matcherAcc = patternAcc.matcher((proteinDbAccession));
            if (matcherAcc.find()) {
                uniprotAccessionID = matcherAcc.group(0);
            }
        }
        if (uniprotAccessionID.equals("NA")) {
            uniprotAccessionID = proteinDbAccession;
        }
        return uniprotAccessionID;
    }

}
