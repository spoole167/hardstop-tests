package dev.gruff.hardstop.testcases.apicheck;

import java.io.Serializable;

public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;

    public UserBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
