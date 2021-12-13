package net.endrealm.libraries.api.commands;

public class InvalidPatternException extends Exception {
    public InvalidPatternException(String pattern) {
        super(String.format("Pattern %s is invalid", pattern));
    }
}
