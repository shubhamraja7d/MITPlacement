package com.sr7d.mitplacement;

import java.util.List;

public class Student {
    private String name;
    private String roll;
    private String phone;
    private String email;
    private String address;
    private List<EducationItem> educations;
    private List<String> skills;
    private String isSet;

    public Student() {
    }

    public Student(String name, String roll, String phone, String email, String address, List<EducationItem> educations, List<String> skills, String isSet) {
        this.name = name;
        this.roll = roll;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.educations = educations;
        this.skills = skills;
        this.isSet = isSet;
    }

    public String getIsSet() {
        return isSet;
    }

    public void setIsSet(String isSet) {
        this.isSet = isSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<EducationItem> getEducations() {
        return educations;
    }

    public void setEducations(List<EducationItem> educations) {
        this.educations = educations;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
