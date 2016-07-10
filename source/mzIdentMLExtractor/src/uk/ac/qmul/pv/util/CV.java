/*
 * @(#) CV    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.util;

/**
 * This is a class which contains CV parameters used in the application
 * All the CVs have define as constants
 * 
 * CV parameters are available at:
 * @see https://raw.githubusercontent.com/HUPO-PSI/psi-ms-CV/master/psi-ms.obo
 * 
 * @author sureshhewapathirana
 */
public class CV {
    //[Term]
        //id: MS:1001097
        //name: distinct peptide sequences
        //def: "This counts distinct sequences hitting the protein 
        // without regard to a minimal confidence threshold." [PSI:PI]
        //xref: value-type:xsd\:nonNegativeInteger "The allowed value-type for this CV term."
        //is_a: MS:1001116 ! single protein result details
        public static final String DISTINCT_PEPTIDES = "MS:1001097";

        //[Term]
        //id: MS:1002235
        //name: ProteoGrouper:PDH score
        //def: "A score assigned to a single protein accession (modelled as 
        // ProteinDetectionHypothesis in mzIdentML), based on summed peptide level scores." [PSI:PI]
        //xref: value-type:xsd\:double "The allowed value-type for this CV term."
        //is_a: MS:1001116 ! single protein result details
        public static final String PDH_SCORE = "MS:1002235";
        
        public static final String MASCOT_SCORE = "MS:1001171";
}
