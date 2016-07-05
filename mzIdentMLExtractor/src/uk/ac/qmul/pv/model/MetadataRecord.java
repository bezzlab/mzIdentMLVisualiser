/*
 * @(#) MetadataRecord    Version 1.0.0    02-09-2016
 *
 */

package uk.ac.qmul.pv.model;

/**
 * MetadataRecord is a class that represent model object of a metadata.
 * (Metadata is data that describes about data.)
 * In other words, this contains all the fields that are required to
 * display in visualisation.
 *
 * @author Suresh Hewapathirana
 */
public class MetadataRecord {

    private String searchType;
    private String softwareList;
    private String enzymes;
    private String fixedModifications = "No modifications";
    private String variableModifications = "No modifications";
    private int numberofPeptides = 0;
    private int numberofProteind = 0;
    private double decoyPercentage = 0.00;

    /**
     * @return the searchType
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * @param searchType the searchType to set
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * @return the softwareList
     */
    public String getSoftwareList() {
        return softwareList;
    }

    /**
     * @param softwareList the softwareList to set
     */
    public void setSoftwareList(String softwareList) {
        this.softwareList = softwareList;
    }

    /**
     * @return the enzymes
     */
    public String getEnzymes() {
        return enzymes;
    }

    /**
     * @param enzymes the enzymes to set
     */
    public void setEnzymes(String enzymes) {
        this.enzymes = enzymes;
    }

    /**
     * @return the fixedModifications
     */
    public String getFixedModifications() {
        return fixedModifications;
    }

    /**
     * @param fixedModifications the fixedModifications to set
     */
    public void setFixedModifications(String fixedModifications) {
        this.fixedModifications = fixedModifications;
    }

    /**
     * @return the variableModifications
     */
    public String getVariableModifications() {
        return variableModifications;
    }

    /**
     * @param variableModifications the variableModifications to set
     */
    public void setVariableModifications(String variableModifications) {
        this.variableModifications = variableModifications;
    }

    /**
     * @return the numberofPeptides
     */
    public int getNumberofPeptides() {
        return numberofPeptides;
    }

    /**
     * @param numberofPeptides the numberofPeptides to set
     */
    public void setNumberofPeptides(int numberofPeptides) {
        this.numberofPeptides = numberofPeptides;
    }

    /**
     * @return the numberofProteind
     */
    public int getNumberofProteind() {
        return numberofProteind;
    }

    /**
     * @param numberofProteind the numberofProteind to set
     */
    public void setNumberofProteind(int numberofProteind) {
        this.numberofProteind = numberofProteind;
    }

    /**
     * @return the decoyPercentage
     */
    public double getDecoyPercentage() {
        return decoyPercentage;
    }

    /**
     * @param decoyPercentage the decoyPercentage to set
     */
    public void setDecoyPercentage(double decoyPercentage) {
        this.decoyPercentage = decoyPercentage;
    }
}
