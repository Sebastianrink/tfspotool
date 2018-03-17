package de.datev.tfspotool;

/**
 * Created by t05252a on 05.09.2017.
 */
public class Initiative extends WorkItem{

    private double stackRank;
    private String acceptanceCriteria;

    public double getStackRank() {
        return stackRank;
    }

    public void setStackRank(double stackRank) {
        this.stackRank = stackRank;
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(String acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }
}
