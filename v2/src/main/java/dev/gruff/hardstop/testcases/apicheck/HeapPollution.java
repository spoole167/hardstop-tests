package dev.gruff.hardstop.testcases.apicheck;

import java.util.ArrayList;
import java.util.List;

public class HeapPollution {
    // Changed to return List<Integer> (erasure is still List, so link succeeds)
    // But runtime behavior changes
    public List<Integer> getList() {
        List<Integer> l = new ArrayList<>();
        l.add(123);
        return l;
    }
}
