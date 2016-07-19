/*
 * @(#) CV    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.util;

/**
 * This is a class which contains CV parameters used in the application All the
 * CVs have define as constants
 *
 * CV parameters are available at:
 *
 * @see https://raw.githubusercontent.com/HUPO-PSI/psi-ms-CV/master/psi-ms.obo
 *
 * @author sureshhewapathirana
 */
public class CV {

    // Used to identify the representative protein of each protein group 

//    [Term]
//    id: MS:1001591
//    name: anchor protein
//    def: "A representative protein selected from a set of sequence same-set or spectrum same-set proteins." [PSI:MS]
//    xref: value-type:xsd\:string "The allowed value-type for this CV term."
//    is_a: MS:1001101 ! protein group or subset relationship 
    public static final String ANCHOR_PROTEIN_CV_TERM = "MS:1001591";
    
//    [Term]
//    id: MS:1002403
//    name: group representative
//    def: "An arbitrary and optional flag applied to exactly one protein per group to indicate it can serve as the representative of the group, amongst leading proteins, in effect serving as a tiebreaker for approaches that require exactly one group representative." [PSI:PI]
//    is_a: MS:1001101 ! protein group or subset relationship
    public static final String GROUP_REP_PROTEIN_CV_TERM = "MS:1002403";

//        [Term]
//        id: MS:1001097
//        name: distinct peptide sequences
//        def: "This counts distinct sequences hitting the protein 
//         without regard to a minimal confidence threshold." [PSI:PI]
//        xref: value-type:xsd\:nonNegativeInteger "The allowed value-type for this CV term."
//        is_a: MS:1001116 ! single protein result details
    public static final String DISTINCT_PEPTIDES = "MS:1001097";

//        [Term]
//        id: MS:1002235
//        name: ProteoGrouper:PDH score
//        def: "A score assigned to a single protein accession (modelled as 
//         ProteinDetectionHypothesis in mzIdentML), based on summed peptide level scores." [PSI:PI]
//        xref: value-type:xsd\:double "The allowed value-type for this CV term."
//        is_a: MS:1001116 ! single protein result details
    public static final String PDH_SCORE = "MS:1002235";

//        [Term]
//        id: MS:1001171
//        name: Mascot:score
//        def: "The Mascot result 'Score'." [PSI:PI]
//        xref: value-type:xsd\:double "The allowed value-type for this CV term."
//        is_a: MS:1001143 ! PSM-level search engine specific statistic
    public static final String MASCOT_SCORE = "MS:1001171";

//        [Term]
//        id: MS:1002470
//        name: PeptideShaker protein group score
//        def: "The probability based PeptideShaker protein group score." [PSI:PI]
//        xref: value-type:xsd\:float "The allowed value-type for this CV term."
//        is_a: MS:1002363 ! search engine specific score for proteins
//        is_a: MS:1002368 ! search engine specific score for protein groups
//        relationship: has_order MS:1002108 ! higher score better
    public static final String PEPTIDESHAKER_SCORE = "MS:1002470";
}
