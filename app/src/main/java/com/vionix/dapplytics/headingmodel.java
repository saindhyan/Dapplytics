package com.vionix.dapplytics;

public class headingmodel {
    String tittle;
    String desp;

    String source;

    public headingmodel(String tittle, String desp, String source) {
        this.tittle = tittle;
        this.desp = desp;
        this.source = source;
    }


    public String getTittle() {
        return tittle;
    }

    public String getDesp() {
        return desp;
    }

    public String getSource() {
        return source;
    }
}
