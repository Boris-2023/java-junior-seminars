package org.example;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "department")
public class Department {

    @Id
    private long id;

    public List<Person> getPersons() {
        return persons;
    }

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "department")
    private List<Person> persons;

    public Department(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Department() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Department {" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
