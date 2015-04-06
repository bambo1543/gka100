package it.bambo.gka100.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreas on 06.04.2015.
 */
public class PhoneBook {

    private String name;
    private List<String> numbers = new ArrayList<>();

    public PhoneBook(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNumbers() {
        return numbers;
    }
}
