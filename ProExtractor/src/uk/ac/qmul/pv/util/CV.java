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
 * @see https://raw.githubusercontent.com/HUPO-PSI/psi-ms-CV/master/psi-ms.obo
 *
 * @author Suresh Hewapathirana
 */
public class CV {

//  [Term]
//  id: MS:1001591
//  name: anchor protein
//  def: "A representative protein selected from a set of sequence same-set or spectrum same-set proteins." [PSI:MS]
//  xref: value-type:xsd\:string "The allowed value-type for this CV term."
//  is_a: MS:1001101 ! protein group or subset relationship 
    public static final String ANCHOR_PROTEIN_CV_TERM = "MS:1001591";
    
//  [Term]
//  id: MS:1002403
//  name: group representative
//  def: "An arbitrary and optional flag applied to exactly one protein per group to indicate it can serve as the representative of the group, amongst leading proteins, in effect serving as a tiebreaker for approaches that require exactly one group representative." [PSI:PI]
//  is_a: MS:1001101 ! protein group or subset relationship
    public static final String GROUP_REP_PROTEIN_CV_TERM = "MS:1002403";

//  [Term]
//  id: MS:1001097
//  name: distinct peptide sequences
//  def: "This counts distinct sequences hitting the protein 
//  without regard to a minimal confidence threshold." [PSI:PI]
//  xref: value-type:xsd\:nonNegativeInteger "The allowed value-type for this CV term."
//  is_a: MS:1001116 ! single protein result details
    public static final String DISTINCT_PEPTIDES = "MS:1001097";

//  [Term]
//  id: MS:1002235
//  name: ProteoGrouper:PDH score
//  def: "A score assigned to a single protein accession (modelled as 
//  ProteinDetectionHypothesis in mzIdentML), based on summed peptide level scores." [PSI:PI]
//  xref: value-type:xsd\:double "The allowed value-type for this CV term."
//  is_a: MS:1001116 ! single protein result details
    public static final String PDH_SCORE = "MS:1002235";

//  [Term]
//  id: MS:1001171
//  name: Mascot:score
//  def: "The Mascot result 'Score'." [PSI:PI]
//  xref: value-type:xsd\:double "The allowed value-type for this CV term."
//  is_a: MS:1001143 ! PSM-level search engine specific statistic
    public static final String MASCOT_SCORE = "MS:1001171";

//  [Term]
//  id: MS:1002470
//  name: PeptideShaker protein group score
//  def: "The probability based PeptideShaker protein group score." [PSI:PI]
//  xref: value-type:xsd\:float "The allowed value-type for this CV term."
//  is_a: MS:1002363 ! search engine specific score for proteins
//  is_a: MS:1002368 ! search engine specific score for protein groups
//  relationship: has_order MS:1002108 ! higher score better
    public static final String PEPTIDESHAKER_SCORE = "MS:1002470";
    
//  [Term]
//  id: MS:1001579
//  name: Scaffold:Protein Probability
//  def: "Scaffold protein probability score." [PSI:PI]
//  xref: value-type:xsd\:double "The allowed value-type for this CV term."
//  is_a: MS:1002363 ! search engine specific score for proteins        
    public static final String  SCAFFOLD_PROTEIN_PROBABILITY = "MS:1001579";
    
//  [Term]
//  id: MS:1001088
//  name: protein description
//  def: "The protein description line from the sequence entry in the source database FASTA file." [PSI:PI]
//  xref: value-type:xsd\:string "The allowed value-type for this CV term."
//  is_a: MS:1001085 ! protein-level identification attribute
//  is_a: MS:1001342 ! database sequence details
    public static final String  PROTEIN_DESCRIPTION = "MS:1001088";
    
//  [Term]
//  id: MS:1001093
//  name: sequence coverage
//  def: "The percent coverage for the protein based upon the matched peptide sequences (can be calculated)." [PSI:PI]
//  xref: value-type:xsd\:decimal "The allowed value-type for this CV term."
//  is_a: MS:1001085 ! protein-level identification attribute
    public static final String  SEQUENCE_COVERAGE = "MS:1001093";
    
//  [Term]
//  id: MS:1001189
//  name: modification specificity peptide N-term
//  def: "As parameter for search engine: apply the modification only at the N-terminus of a peptide." [PSI:PI]
//  is_a: MS:1001056 ! modification specificity rule
    public static final String N_TERM_PEPTIDE_MOD = "MS:1001189";
    
//  [Term]
//  id: MS:1001190
//  name: modification specificity peptide C-term
//  def: "As parameter for search engine: apply the modification only at the C-terminus of a peptide." [PSI:PI]
//  is_a: MS:1001056 ! modification specificity rule
    public static final String C_TERM_PEPTIDE_MOD = "MS:1001190";

//  [Term]
//  id: MS:1002057
//  name: modification specificity protein N-term
//  def: "As parameter for search engine: apply the modification only at the N-terminus of a protein." [PSI:PI]
//  is_a: MS:1001056 ! modification specificity rule
    public static final String N_TERM_PROTEIN_MOD = "MS:1002057";
          
//  [Term]
//  id: MS:1002058
//  name: modification specificity protein C-term
//  def: "As parameter for search engine: apply the modification only at the C-terminus of a protein." [PSI:PI]
//  is_a: MS:1001056 ! modification specificity rule
    public static final String C_TERM_PROTEIN_MOD = "MS:1002058";
}
