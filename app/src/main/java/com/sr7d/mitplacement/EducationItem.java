package com.sr7d.mitplacement;

public class EducationItem {
    private String Degree;
    private String CompletionYear;
    private String College;
    private String University;
    private String percentage;

    public EducationItem() {
    }

    public EducationItem(String degree, String completionYear, String college, String university, String percentage) {
        Degree = degree;
        CompletionYear = completionYear;
        College = college;
        University = university;
        this.percentage = percentage;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getCompletionYear() {
        return CompletionYear;
    }

    public void setCompletionYear(String completionYear) {
        CompletionYear = completionYear;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public String getUniversity() {
        return University;
    }

    public void setUniversity(String university) {
        University = university;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
