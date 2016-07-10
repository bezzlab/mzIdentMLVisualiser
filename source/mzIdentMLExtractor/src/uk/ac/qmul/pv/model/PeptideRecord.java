/*
 * @(#) PeptideRecord    Version 1.0.0    02-09-2016
 *
 */

package uk.ac.qmul.pv.model;

/**
 * PeptideRecord is a class that represent model object of a peptide.
 * In other words, this contains all the fields that are required to 
 * display in visualisation. 
 * 
 * @author Suresh Hewapathirana
 */
public class PeptideRecord {
    
    private String ID;
    private String sequence;
    private int startPossion;
    private int endPossion;
    private String modifications;
    private String accession;
    private int noOfModifications = 0;
   
//    /**
//     * @return the rank
//     */
//    public int getRank() {
//        return rank;
//    }
//
//    /**
//     * @param rank the rank to set
//     */
//    public void setRank(int rank) {
//        this.rank = rank;
//    }
//    
//    /**
//     * @return the sequence
//     */
//    public String getSequence() {
//        return sequence;
//    }
//
//    /**
//     * @param sequence the sequence to set
//     */
//    public void setSequence(String sequence) {
//        this.sequence = sequence;
//    }
//
//    /**
//     * @return the calculatedMZ
//     */
//    public double getCalculatedMZ() {
//        return calculatedMZ;
//    }
//
//    /**
//     * @param calculatedMZ the calculatedMZ to set
//     */
//    public void setCalculatedMZ(double calculatedMZ) {
//        DecimalFormat df = new DecimalFormat("#.00");
//        this.calculatedMZ = Double.valueOf(df.format(calculatedMZ));
//    }
//
//    /**
//     * @return the expectedMZ
//     */
//    public double getExpectedMZ() {
//        return expectedMZ;
//    }
//
//    /**
//     * @param expectedMZ the expectedMZ to set
//     */
//    public void setExpectedMZ(double expectedMZ) {
//        DecimalFormat df = new DecimalFormat("#.00");
//        this.expectedMZ = Double.valueOf(df.format(expectedMZ));
//    }
//
//    /**
//     * @return the charge
//     */
//    public int getCharge() {
//        return charge;
//    }
//
//    /**
//     * @param charge the charge to set
//     */
//    public void setCharge(int charge) {
//        this.charge = charge;
//    }
//
//    /**
//     * @return the modifications
//     */
//    public String getModifications() {
//        return modifications;
//    }
//
//    /**
//     * @param modifications the modifications to set
//     */
//    public void setModifications(String modifications) {
//        this.modifications = modifications;
//    }
//
//    /**
//     * @return the msgfScore
//     */
//    public double getMsgfScore() {
//        return msgfScore;
//    }
//
//    /**
//     * @param msgfScore the msgfScore to set
//     */
//    public void setMsgfScore(double msgfScore) {
//        DecimalFormat df = new DecimalFormat("#.00");
//        this.msgfScore = Double.valueOf(df.format(msgfScore));
//    }
//    
//    /**
//     * @return the accession
//     */
//    public String getAccession() {
//        return accession;
//    }
//
//    /**
//     * @param accession the accession to set
//     */
//    public void setAccession(String accession) {
//        this.accession = accession;
//    }
//
//    /**
//     * @return the startPossion
//     */
//    public int getStartPossion() {
//        return startPossion;
//    }
//
//    /**
//     * @param startPossion the startPossion to set
//     */
//    public void setStartPossion(int startPossion) {
//        this.startPossion = startPossion;
//    }
//
//    /**
//     * @return the endPossion
//     */
//    public int getEndPossion() {
//        return endPossion;
//    }
//
//    /**
//     * @param endPossion the endPossion to set
//     */
//    public void setEndPossion(int endPossion) {
//        this.endPossion = endPossion;
//    }
//
//    /**
//     * @return the peptideEvidancePre
//     */
//    public String getPeptideEvidancePre() {
//        return peptideEvidancePre;
//    }
//
//    /**
//     * @param peptideEvidancePre the peptideEvidancePre to set
//     */
//    public void setPeptideEvidancePre(String peptideEvidancePre) {
//        this.peptideEvidancePre = peptideEvidancePre;
//    }
//
//    /**
//     * @return the peptideEvidancePost
//     */
//    public String getPeptideEvidancePost() {
//        return peptideEvidancePost;
//    }
//
//    /**
//     * @param peptideEvidancePost the peptideEvidancePost to set
//     */
//    public void setPeptideEvidancePost(String peptideEvidancePost) {
//        this.peptideEvidancePost = peptideEvidancePost;
//    }
//
//    /**
//     * @return the passThreshold
//     */
//    public boolean isPassThreshold() {
//        return passThreshold;
//    }
//
//    /**
//     * @param passThreshold the passThreshold to set
//     */
//    public void setPassThreshold(boolean passThreshold) {
//        this.passThreshold = passThreshold;
//    }
//
//    /**
//     * @return the decoy
//     */
//    public boolean isDecoy() {
//        return decoy;
//    }
//
//    /**
//     * @param decoy the decoy to set
//     */
//    public void setDecoy(boolean decoy) {
//        this.decoy = decoy;
//    }

    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the startPossion
     */
    public int getStartPossion() {
        return startPossion;
    }

    /**
     * @param startPossion the startPossion to set
     */
    public void setStartPossion(int startPossion) {
        this.startPossion = startPossion;
    }

    /**
     * @return the endPossion
     */
    public int getEndPossion() {
        return endPossion;
    }

    /**
     * @param endPossion the endPossion to set
     */
    public void setEndPossion(int endPossion) {
        this.endPossion = endPossion;
    }

    /**
     * @return the modifications
     */
    public String getModifications() {
        return modifications;
    }

    /**
     * @param modifications the modifications to set
     */
    public void setModifications(String modifications) {
        this.modifications = modifications;
    }

    /**
     * @return the accession
     */
    public String getAccession() {
        return accession;
    }

    /**
     * @param accession the accession to set
     */
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     * @return the noOfModifications
     */
    public int getNoOfModifications() {
        return noOfModifications;
    }

    /**
     * @param noOfModifications the noOfModifications to set
     */
    public void setNoOfModifications(int noOfModifications) {
        this.noOfModifications = noOfModifications;
    }
}
