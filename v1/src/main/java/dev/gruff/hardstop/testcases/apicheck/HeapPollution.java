package dev.gruff.hardstop.testcases.apicheck;

import java.util.ArrayList;
import java.util.List;

public class HeapPollution {
    public List<String> getList() {
        List<String> l = new ArrayList<>();
        l.add("string");
        return l;
    }
}
