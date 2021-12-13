package core.api.command.libs.injection;

import lombok.Data;

import java.util.HashMap;

@Data
public class ComplexInvocationTarget {
    private final HashMap<Integer, Integer> invocationCount;

    public ComplexInvocationTarget() {
        invocationCount = new HashMap<>();
    }

    public void add(Integer integer) {
        invocationCount.put(integer, get(integer)+1);
    }

    public Integer get(Integer integer) {
        return invocationCount.getOrDefault(integer, 0);
    }
}
