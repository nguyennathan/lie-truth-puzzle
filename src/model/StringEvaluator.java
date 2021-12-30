package model;

public class StringEvaluator {

    private char truthfulChar;
    private char guiltyChar;


    public StringEvaluator(char truthfulChar, char guiltyChar) {
        this.truthfulChar = truthfulChar;
        this.guiltyChar = guiltyChar;
    }


    public boolean evaluate(String statement, Suspect suspect) throws NotWellFormedException {
        boolean truthful = suspect.getTruthful();
        boolean guilty = suspect.getGuilty();
        return evaluateRecurse(statement, truthful, guilty);
    }


    private boolean evaluateRecurse(String statement, boolean truthful, boolean guilty) throws NotWellFormedException {
        // base cases
        if (statement.equals("" + truthfulChar)) {
            return truthful;
        }
        if (statement.equals("" + guiltyChar)) {
            return guilty;
        }

        // empty string
        if (statement.equals("")) {
            throw new NotWellFormedException();
        }

        // if statement is some other single character
        if (statement.length() == 1) {
            throw new NotWellFormedException();
        }

        int operatorIndex = findOperator(statement);

        // if "(!x)", return !evaluateRecurse(x)
        if (statement.charAt(operatorIndex) == '!') {
            String substring = statement.substring(2, statement.length()-1);
            return !evaluateRecurse(substring, truthful, guilty);
        }

        // if "(x&&y)", return evaluateRecurse(x) && evaluateRecurse(y)
        if (statement.charAt(operatorIndex) == '&') {
            String substring1 = statement.substring(1, operatorIndex);
            String substring2 = statement.substring(operatorIndex+2, statement.length()-1);
            return evaluateRecurse(substring1, truthful, guilty) && evaluateRecurse(substring2, truthful, guilty);
        }

        // if "(x||y)", return evaluateRecurse(x) || evaluateRecurse(y)
        if (statement.charAt(operatorIndex) == '|') {
            String substring1 = statement.substring(1, operatorIndex);
            String substring2 = statement.substring(operatorIndex+2, statement.length()-1);
            return evaluateRecurse(substring1, truthful, guilty) || evaluateRecurse(substring2, truthful, guilty);
        }

        // else, must not be well-formed
        throw new NotWellFormedException();

    }

    // assumes statement is not truthfulChar or guiltyChar
    private int findOperator(String statement) throws NotWellFormedException {
        // check parentheses
        int depth = 0;
        for (int i = 0; i < statement.length(); i++) {
            if (statement.charAt(i) == '(') {
                depth++;
            }
            else if (statement.charAt(i) == ')') {
                depth--;
            }
            if (depth < 0) {
                throw new NotWellFormedException();
            }
        }
        if (depth != 0) {
            throw new NotWellFormedException();
        }

        // if "(!x)"
        if (statement.charAt(1) == '!') {
            return 1;
        }

        // if "(x&&y)" or "(x||y), note that depth == 0 at this point
        for (int i = 0; i < statement.length(); i++) {
            if (statement.charAt(i) == '(') {
                depth++;
            }
            else if (statement.charAt(i) == ')') {
                depth--;
            }
            if (depth == 1 && statement.charAt(i) == '&' && statement.charAt(i+1) == '&') {
                return i;
            }
            if (depth == 1 && statement.charAt(i) == '|' && statement.charAt(i+1) == '|') {
                return i;
            }
        }

        // else, must not be well-formed
        throw new NotWellFormedException();
    }

}
