package com.bluerizon.hcmanager.payload.request;

import com.bluerizon.hcmanager.models.Assurances;

import java.util.Date;

public class EtatRequest {
    private Assurances assurance;
    private String type;
    private String start;
    private String end;

    public EtatRequest() {
    }

    public Assurances getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurances assurance) {
        this.assurance = assurance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
