package io.lumine.mythic.lib.api.math;

public class EvaluatedFormula {
    private final String str;

    int pos = -1, ch;

    /**
     * This evaluates a string into a double and is "borrowed" from stack
     * overflow could possibly be changed for the better
     *
     * @param str The formula to evaluate. All placeholders must be replaced
     *            beforehand
     */
    public EvaluatedFormula(String str) {
        this.str = str;
    }

    public double evaluate() {
        nextChar();
        double x = parseExpression();
        if (pos < str.length())
            throw new RuntimeException("Unexpected: " + (char) ch);
        return x;
    }

    private void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ')
            nextChar();
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    private double parseExpression() {
        double x = parseTerm();
        for (;;) {
            if (eat('+'))
                x += parseTerm(); // addition
            else if (eat('-'))
                x -= parseTerm(); // subtraction
            else
                return x;
        }
    }

    private double parseTerm() {
        double x = parseFactor();
        for (;;) {
            if (eat('*'))
                x *= parseFactor(); // multiplication
            else if (eat('/'))
                x /= parseFactor(); // division
            else
                return x;
        }
    }

    private double parseFactor() {
        if (eat('+'))
            return parseFactor(); // unary plus
        if (eat('-'))
            return -parseFactor(); // unary minus

        double x;
        int startPos = this.pos;
        if (eat('(')) { // parentheses
            x = parseExpression();
            eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
            while ((ch >= '0' && ch <= '9') || ch == '.')
                nextChar();
            x = Double.parseDouble(str.substring(startPos, this.pos));
        } else if (ch >= 'a' && ch <= 'z') { // functions
            while (ch >= 'a' && ch <= 'z')
                nextChar();
            String func = str.substring(startPos, this.pos);
            x = parseFactor();
            switch (func) {
                case "sqrt":
                    x = Math.sqrt(x);
                    break;
                case "sin":
                    x = Math.sin(Math.toRadians(x));
                    break;
                case "cos":
                    x = Math.cos(Math.toRadians(x));
                    break;
                case "tan":
                    x = Math.tan(Math.toRadians(x));
                    break;
                default:
                    throw new RuntimeException("Unknown function: " + func);
            }
        } else {
            throw new RuntimeException("Unexpected: " + (char) ch);
        }

        if (eat('^'))
            x = Math.pow(x, parseFactor()); // exponentiation

        return x;
    }
}
