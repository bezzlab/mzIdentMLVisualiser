/*
 * @(#) ProteinRecord    Version 1.0.0    02-09-2016
 *
 */
package uk.ac.qmul.pv.model;

import java.text.DecimalFormat;

/**
 *
 * ProteinRecord is a class that represent model object of a Protein. In other
 * words, this contains all the fields of protein that are required to display
 * in the visualisation.
 *
 * @author Suresh Hewapathirana
 */
public class ProteinRecord {

    private String id = "Not available";
    private String accessionCode = "Not available";
    private String speciesName = "Not available";
    private String proteinName = "Not available";
    private int distinctPeptides = 0;
    private double score = -1;
    private double coverage = 0;

    /**
     * @return the accessionCode
     */
    public String getAccessionCode() {
        return accessionCode;
    }

    /**
     * @param accessionCode the accessionCode to set
     */
    public void setAccessionCode(String accessionCode) {
        this.accessionCode = accessionCode;
    }

    /**
     * @return the speciesName
     */
    public String getSpeciesName() {
        return speciesName;
    }

    /**
     * @param speciesName the speciesName to set
     */
    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    /**
     * @return the proteinName
     */
    public String getProteinName() {
        return proteinName;
    }

    /**
     * @param proteinName the proteinName to set
     */
    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    /**
     * @return the distinctPeptides
     */
    public int getDistinctPeptides() {
        return distinctPeptides;
    }

    /**
     * @param distinctPeptides the distinctPeptides to set
     */
    public void setDistinctPeptides(int distinctPeptides) {
        this.distinctPeptides = distinctPeptides;
    }

    /**
     * @return the score
     */
    public double getPhdScore() {
        return score;
    }

    /**
     * @param phdScore the score to set
     */
    public void setPhdScore(double phdScore) {

        // round to two decimal places
        DecimalFormat df = new DecimalFormat("#.00");
        this.score = Double.valueOf(df.format(phdScore));
    }

    /**
     * @return the coverage
     */
    public double getCoverage() {
        return coverage;
    }

    /**
     * @param coverage the coverage to set
     */
    public void setCoverage(double coverage) {
        if (coverage <= 100) {
            // round to one decimal places
            DecimalFormat df = new DecimalFormat("#.0");
            this.coverage = Double.valueOf(df.format(coverage));
        } else {
            throw new ArithmeticException("Peptide Coverage cannot exceed 100%");

        }
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ID : " + this.id
                + "accessionCode : " + this.accessionCode
                + "speciesName : " + this.speciesName
                + "proteinName : " + this.proteinName
                + "distinctPeptides : " + this.distinctPeptides
                + "phdScore : " + this.score
                + "coverage : " + this.coverage;
    }

}
