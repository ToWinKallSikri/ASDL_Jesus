package it.unicam.cs.asdl2223.mp2;

/**
 * An object of this class is an actor that uses an ASDL2223Deque<Character> as
 * a Stack in order to check that a sequence containing the following
 * characters: '(', ')', '[', ']', '{', '}' in any order is a string of balanced
 * parentheses or not. The input is given as a String in which white spaces,
 * tabs and newlines are ignored.
 * <p>
 * Some examples:
 * <p>
 * - " (( [( {\t (\t) [ ] } ) \n ] ) ) " is a string o balanced parentheses - " " is a
 * string of balanced parentheses - "(([)])" is NOT a string of balanced
 * parentheses - "( { } " is NOT a string of balanced parentheses - "}(([]))" is
 * NOT a string of balanced parentheses - "( ( \n [(P)] \t ))" is NOT a string
 * of balanced parentheses
 *
 * @author Template: Luca Tesei
 * Implementation: Twinkal Sikri, twinkal.sikri@studenti.unicam.it
 */
public class BalancedParenthesesChecker {

    // The stack is to be used to check the balanced parentheses
    private ASDL2223Deque<Character> stack;

    /**
     * Create a new checker.
     */
    public BalancedParenthesesChecker() {
        this.stack = new ASDL2223Deque<Character>();
    }

    /**
     * Check if a given string contains a balanced parentheses sequence of
     * characters '(', ')', '[', ']', '{', '}' by ignoring white spaces ' ',
     * tabs '\t' and newlines '\n'.
     *
     * @param s the string to check
     * @return true if s contains a balanced parentheses sequence, false
     * otherwise
     * @throws IllegalArgumentException if s contains at least a character
     *                                  different form:'(', ')', '[', ']',
     *                                  '{', '}', white space ' ', tab '\t'
     *                                  and newline '\n'
     */
    public boolean check(String s) {
        this.stack.clear();
        // creo un ciclo for per ciclare tante volte quanto è la lunghezza della stringa di caratteri
        for (int i = 0; i < s.length(); i++) {
            // creo un nuovo carattere d'appoggio per confrontarlo con quelli della stringa e usarlo come riferimento
            char newCh = s.charAt(i);
            // se il carattere della stringa è diverso da quelli ammessi, lancia l'eccezione IllegalArgumentException
            if (newCh != '(' && newCh != '[' && newCh != '{' &&
                    newCh != ')' && newCh != ']' && newCh != '}' &&
                    newCh != ' ' && newCh != '\t' && newCh != '\n') {
                throw new IllegalArgumentException("Caratteri non ammessi");
            }
            // se il carattere della stringa è '(' o '[' o '{', faccio il push del carattere nello stack
            if (newCh == '(' || newCh == '[' || newCh == '{') {
                this.stack.push(newCh);
            } else {
                if (newCh != ' ' && newCh != '\t' && newCh != '\n') {
                    // se lo stack è vuoto, faccio il push
                    if (stack.isEmpty()) {
                        this.stack.push(newCh);
                    } else {
                        // se incontro un ')' e se il carattere '(' è contenuto nello stack, faccio il pop dallo stack
                        if (newCh == ')' && stack.peekFirst() == '(') {
                            stack.pop();
                        } else {
                            // se incontro un ']' e se il carattere '[' è contenuto nello stack, faccio il pop dallo stack
                            if (newCh == ']' && stack.peekFirst() == '[') {
                                stack.pop();
                            } else {
                                // se incontro un '}' e se il carattere '{' è contenuto nello stack, faccio il pop dallo stack
                                if (newCh == '}' && stack.peekFirst() == '{') {
                                    stack.pop();
                                // se nessuna delle condizioni sopra è stata rispettata, quindi non sono state eseguite
                                // operazioni, la stringa non è sicuramente bilanciata (perciò ritorno false)
                                } else return false;
                            }
                        }
                    }
                }
            }
        }
        // se lo stack alla fine è vuoto, ritorna true (quindi si evince che la stringa è bilanciata, la quantità di pop
        // è equivalente al numero di push)
        return (stack.isEmpty());
    }
}
