/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.qmul.pv.model;

/**
 *
 * @author sureshhewapathirana
 */

  public class Interval {
      private int start;
      private int end;
      private int gap;
      public Interval() { start = 0; end = 0; }
      public Interval(int s, int e) { start = s; end = e; }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * @return the gap
     */
    public int getGap() {
        return this.end - this.start;
    }
  }