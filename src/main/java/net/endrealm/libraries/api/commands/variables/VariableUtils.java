package net.endrealm.libraries.api.commands.variables;


import net.endrealm.libraries.api.commands.InvalidPatternException;
import net.endrealm.libraries.api.commands.annotations.Command;
import net.endrealm.libraries.api.commands.annotations.Inject;
import net.endrealm.libraries.api.commands.annotations.Param;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VariableUtils {

    /**
     * Reads all variables specified by a command annotation
     *
     * @param command the command annotation to check
     * @param previous a list of previous variables used to calculate variables
     * @return a new list of variables extracted from the command + the previous
     */
    public static List<Variable> readVariables(Command command, List<Variable> previous) {
        List<Variable> temp =  Arrays.stream(command.variables())
                .map(e-> {

                    String value = e.val();

                    try {
                        value = calculateValue(e.val(), previous).getValue();
                    } catch (InvalidPatternException ignored) {}

                    return new Variable(e.key(), value);
                })
                .collect(Collectors.toList());


        return mergeVarLists(previous, temp);
    }


    /**
     * Reads all variables specified by a injection annotation.
     * Uses syntax declared in docs of
     * {@link #calculateValue(String, List)}
     *
     * @param inject the inject annotation
     * @param previous the previous annotations
     * @return a new list of variables extracted from the inject + the previous
     */
    public static List<Variable> readVariables(Inject inject, List<Variable> previous) {
        List<Variable> temp = Arrays.stream(inject.using())
                .map(e-> {

                    try {
                        return calculateValue(e, previous);
                    } catch (InvalidPatternException ignored) {}

                    return new Variable(e, "error");
                })
                .collect(Collectors.toList());


        return mergeVarLists(previous, temp);
    }

    /**
     * Reads all variables specified by a param annotation.
     * Uses syntax declared in docs of
     * {@link #calculateValue(String, List)}
     *
     * @param param the param annotation
     * @param previous the previous annotations
     * @return a new list of variables extracted from the param + the previous
     */
    public static List<Variable> readVariables(Param param, List<Variable> previous) {
        List<Variable> temp = Arrays.stream(param.variables())
                .map(e-> {

                    try {
                        return calculateValue(e, previous);
                    } catch (InvalidPatternException ignored) {}

                    return new Variable(e, "error");
                })
                .collect(Collectors.toList());


        return mergeVarLists(previous, temp);
    }

    /**
     *
     * If a variable declaration does not meet
     * the specified standards an exception will be thrown.
     *
     *
     * Uses the following syntax:
     * <ul>
     *     <li><code>(VariableName):==(Value)</code> - define a
     *     variable with a value</li>
     *     <li><code>(VariableName):==$(Var1)</code> - define a
     *     variable with the value of another variable</li>
     *     <li><code>(VariableName):==$(Var1)$(Var2)</code> -
     *     define a variable as the string addition of 2 or
     *     more variables</li>
     * </ul>
     *
     * Look at the examples below:
     * <ul>
     *     <li><code>myFooVar:==Its such a foo lol</code></li>
     *     <li><code>myBarVar:==$myChaVar$myFooVar</code></li>
     * </ul>
     *
     * The following symbols are prohibited to be used in variable names and values:
     * <ul>
     *     <li>$</li>
     *     <li>:==</li>
     * </ul>
     *
     *
     * @param variableDefinition the variable definition
     * @param variables the variables to use for calculation
     * @return the calculated variable
     */
    public static Variable calculateValue(String variableDefinition, List<Variable> variables) throws InvalidPatternException {

        if(variableDefinition == null || variableDefinition.isEmpty())
            throw new InvalidPatternException(variableDefinition);

        final String equals = Pattern.quote(":==");
        final String preVariable = Pattern.quote("$");

        String[] termParts = variableDefinition.split(equals);

        if(termParts.length != 2) // Check that there are only 2 sides
            throw new InvalidPatternException(variableDefinition);

        String variableName = termParts[0];
        String unparsedValue = termParts[1];


        if(variableName.split(preVariable).length > 1)
            throw new InvalidPatternException(variableDefinition);

        StringBuilder parsedValue = new StringBuilder();

        if(unparsedValue.split(preVariable).length > 1) {

            List<String> foreignVariables = Arrays.stream(unparsedValue.split(preVariable)).skip(1).collect(Collectors.toList());

            for(String foreignVar : foreignVariables) {
                for(Variable variable : variables) {

                    if(!variable.getKey().equals(foreignVar))
                        continue;

                    parsedValue.append(variable.getValue());
                    break;
                }
            }

        } else {
            parsedValue = new StringBuilder(unparsedValue);
        }

        return new Variable(variableName, parsedValue.toString());
    }

    private static List<Variable> mergeVarLists(List<Variable> previous, List<Variable> temp) {
        return Stream.concat(previous.stream(), temp.stream())
                .filter(new ConcurrentSkipListSet<>(Comparator.comparing(Variable::getKey))::add)
                .collect(Collectors.toList());
    }

}
