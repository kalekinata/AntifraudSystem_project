package com.system.antifraud.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(name = "id")
    public String clid;
    private String surname;
    private String name;
    private String patronymic;

    public Client() {
    }

    public Client(String clid, String surname, String name, String patronymic) {
        this.clid = clid;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
    }

    public String getClid() {
        return clid;
    }

    public void setClid(String clid) {
        this.clid = clid;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
}
