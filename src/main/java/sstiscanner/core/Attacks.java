package sstiscanner.core;

import sstiscanner.utils.ExecutedAttack;

import java.util.ArrayList;
import java.util.List;

public class Attacks {
    private final List<ExecutedAttack> allAttacks;

    public Attacks() {
        this.allAttacks = new ArrayList<>();
    }

    public void addAll(List<ExecutedAttack> currentAttacks) {
        this.allAttacks.addAll(currentAttacks);
    }

    public List<ExecutedAttack> getAllAttacks() {
        return this.allAttacks;
    }

    public void clear() {
        this.allAttacks.clear();
    }


}
