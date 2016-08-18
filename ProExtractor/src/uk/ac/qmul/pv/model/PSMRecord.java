/*
 * @(#) PSMRecord    Version 1.0.0    02-09-2016
 *
 */ 

package uk.ac.qmul.pv.model;

import java.text.DecimalFormat;

/**
 *
 * @author sureshhewapathirana
 */
public class PSMRecord {

    private String id;
    private int rank;
    private int chargeState;
    private double experimentalMassToCharge;
    private double calculatedMassToCharge;

    /**
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @return the chargeState
     */
    public int getChargeState() {
        return chargeState;
    }

    /**
     * @param chargeState the chargeState to set
     */
    public void setChargeState(int chargeState) {
        this.chargeState = chargeState;
    }

    /**
     * @return the experimentalMassToCharge
     */
    public double getExperimentalMassToCharge() {
        return experimentalMassToCharge;
    }

    /**
     * @param experimentalMassToCharge the experimentalMassToCharge to set
     */
    public void setExperimentalMassToCharge(double experimentalMassToCharge) {
        // round to four decimal places
        DecimalFormat df = new DecimalFormat("#.0000");
        this.experimentalMassToCharge = Double.valueOf(df.format(experimentalMassToCharge));
    }

    /**
     * @return the calculatedMassToCharge
     */
    public double getCalculatedMassToCharge() {
        return calculatedMassToCharge;
    }

    /**
     * @param calculatedMassToCharge the calculatedMassToCharge to set
     */
    public void setCalculatedMassToCharge(double calculatedMassToCharge) {
         // round to four decimal places
        DecimalFormat df = new DecimalFormat("#.0000");
        this.calculatedMassToCharge = Double.valueOf(df.format(calculatedMassToCharge));
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

}
