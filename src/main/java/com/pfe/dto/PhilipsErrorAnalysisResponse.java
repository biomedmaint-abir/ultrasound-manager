package com.pfe.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhilipsErrorAnalysisResponse {

    private String code;
    private String symptomes;
    private String causesProbables;
    private String actionsCorrectives;
    private String piecesConcernees;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymptomes() {
        return symptomes;
    }

    public void setSymptomes(String symptomes) {
        this.symptomes = symptomes;
    }

    public String getCausesProbables() {
        return causesProbables;
    }

    public void setCausesProbables(String causesProbables) {
        this.causesProbables = causesProbables;
    }

    public String getActionsCorrectives() {
        return actionsCorrectives;
    }

    public void setActionsCorrectives(String actionsCorrectives) {
        this.actionsCorrectives = actionsCorrectives;
    }

    public String getPiecesConcernees() {
        return piecesConcernees;
    }

    public void setPiecesConcernees(String piecesConcernees) {
        this.piecesConcernees = piecesConcernees;
    }
}
