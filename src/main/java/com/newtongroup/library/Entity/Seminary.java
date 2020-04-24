package com.newtongroup.library.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "seminaries")
public class Seminary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seminary_id")
    private Long seminary_id;

    @Column(name = "title")
    private String title;

    @Column(name = "information")
    private String information;


    @Column(name = "occurrence")
    private java.sql.Date occurrence;

    public Seminary() {
    }

    public Long getSeminary_id() {
        return seminary_id;
    }

    public void setSeminary_id(Long seminary_id) {
        this.seminary_id = seminary_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Date getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(java.sql.Date occurrence) {
        this.occurrence = occurrence;
    }
}
