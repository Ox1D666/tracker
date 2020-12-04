package ru.job4j.tracker;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(name = "create_date")
    private Timestamp create;
    private String description;

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item(String name, Timestamp create, String description) {
        this.name = name;
        this.create = create;
        this.description = description;
    }

    public Item(String name) {
        this.name = name;
    }

    public Item() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(name, item.name)
                && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public String toString() {
        return id + ", " + name + ", " + create + ", " + description;
    }
}
