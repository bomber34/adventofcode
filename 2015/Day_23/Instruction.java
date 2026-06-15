import java.util.HashMap;

public class Instruction {

    private final EInstruction _instruction;
    private final Object _var1;
    private final Object _var2;

    public Instruction(EInstruction instr, Object var1, Object var2) {
        if (instr == null || var1 == null || var2 == null) {
            throw new IllegalArgumentException(String.format("not all variables are initialized (%s %s %s", instr, var1, var2));
        }

        _instruction = instr;
        _var1 = var1;
        _var2 = var2;
    }

    @Override
    public String toString() {
        if (_instruction == EInstruction.JIO || _instruction == EInstruction.JIE) {
            return String.format("%s %s, %s", _instruction.name(), _var1, _var2);
        } else {
            return String.format("%s %s", _instruction.name(), _var1);
        }
    }

    private int getValue(HashMap<String, Integer> varMap, Object var) {
        if (varMap == null) {
            throw new IllegalArgumentException("No varMap provided");
        }

        if (var instanceof String) {
            return varMap.get(var);
        } else if (var instanceof Integer) {
            return (Integer) var;
        } else {
            throw new IllegalArgumentException(String.format("var is neither a variable nor a constant but %s", var));
        }
    }

    private void setValue(HashMap<String, Integer> varMap, String varName, Integer varValue) {
        varMap.put(varName, varValue);
    }

    public int nextProgramAddress(HashMap<String, Integer> varMap, int currentAddress) {
        int nextAddress = currentAddress;
        switch (_instruction) {
            case HLF -> {
                int val = getValue(varMap, _var1);
                if (_var1 instanceof String) {
                    setValue(varMap, (String) _var1, val/2);
                }
                nextAddress++;
            }
            case TPL -> {
                int val = getValue(varMap, _var1);
                if (_var1 instanceof String) {
                    setValue(varMap, (String) _var1, val*3);
                }
                nextAddress++;
            }
            case INC -> {
                int val = getValue(varMap, _var1);
                if (_var1 instanceof String) {
                    setValue(varMap, (String) _var1, val+1);
                }
                nextAddress++;
            }
            case JMP -> nextAddress = currentAddress + getValue(varMap, _var1);
            case JIE -> {
                int valToCheck = getValue(varMap, _var1);
                int offset = getValue(varMap, _var2);
                if (valToCheck % 2 == 0) {
                    nextAddress += offset;
                } else {
                    nextAddress++;
                }
            }
            case JIO -> {
                int valToCheck = getValue(varMap, _var1);
                int offset = getValue(varMap, _var2);
                if (valToCheck == 1) {
                    nextAddress += offset;
                } else {
                    nextAddress++;
                }
            }
            default -> throw new IllegalStateException(String.format("Unknown Instruction %s", _instruction));
        }
        return nextAddress;
    }
}
