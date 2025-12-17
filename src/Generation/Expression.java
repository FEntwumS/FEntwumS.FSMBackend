/*
 * STDE - State Transition Diagram Editor
 *
 * 2018 Markus de Koster
 *
 * Component:   Expression
 * Class:       Expression
 * Created:     2018-12
 *
 */
package Generation;

import Generation.Lexer.TOKEN;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Datastructure for handling evaluation of conditions by recursively evaluating
 * (nested) expressions
 *
 * @author Markus de Koster <markus.de_koster@smail.th-koeln.de>
 */
public class Expression {

    private String expressionString;
    private TOKEN token;
    private boolean hasNotFlag;
    private int result;
    private ArrayList<Expression> nestedExpressions;
    private int dataType;   //0 for identifier/number, 1 for operator,
    // 2 for nestedExpression/undefined, 3 for resolved

    /**
     * Constructor for new conditions
     *
     * @param exprString expression as a String, e.g. (A>=B)&&!C
     * @author Markus de Koster <markus.de_koster@smail.th-koeln.de>
     */
    public Expression(String exprString) {
        this(TOKEN.UNKNOWN, exprString, false);
    }

    /**
     * internal Constructor if the expression consists of only true or false
     *
     * @param result
     * @author Markus de Koster <markus.de_koster@smail.th-koeln.de>
     */
    private Expression(boolean result) {
        if (!result) {
            this.result = 0;
        } else {
            this.result = 1;
        }
        dataType = 3;
        hasNotFlag = false;
        nestedExpressions = new ArrayList<Expression>();
    }

    /**
     * internal Constructor for new and nested conditions
     *
     * @param token type of token if it's only a token
     * @param exprString expression as a String, e.g. A>=B&&!C
     * @param hasNotFlag negation status
     * @author Markus de Koster <markus.de_koster@smail.th-koeln.de>
     */
    private Expression(TOKEN token, String exprString, boolean hasNotFlag) {
        this.hasNotFlag = hasNotFlag;
        setDataTypeByToken(token);
        this.expressionString = exprString;
        nestedExpressions = new ArrayList<Expression>();

        switch (dataType) {
            case 0: // identifier
                this.token = token;
                break;
            case 1: // operator
                this.token = token;
                break;
            case 2: // nested Expression or undefined
                Lexer lex = new Lexer();
                // append END token to the string to have a limiter
                lex.setString((exprString + '\0').toCharArray());

                // current Paranthesis level e.g.'(a<b)&&c' at 'c':0, at 'a':1
                int currentParLevel = 0;
                // position of the opening paranthesis at level 0 in exprString
                int paranthPos = -1;
                // value for '!' in front of expressions
                boolean notFlag = false;
                while (lex.getToken() != TOKEN.END) {
                    TOKEN currentToken = lex.getToken();
                    switch (currentToken) {
                        case LPARENTH:
                            if (currentParLevel == 0) {
                                paranthPos = lex.getStringPos();
                            }
                            currentParLevel++;
                            break;
                        case RPARENTH:
                            currentParLevel--;
                            if (currentParLevel == 0) { // if back at the lowest paranthesis level: 
                                // add the content of the parantheses to the nested expression list
                                String nestedExpString = exprString.substring(paranthPos - 1, lex.getStringPos() - 2);
                                nestedExpressions.add(new Expression(TOKEN.UNKNOWN, nestedExpString, notFlag));
                                if (notFlag) { // reset not flag afterwards
                                    notFlag = false;
                                }
                            }
                            break;
                        case IDENTIFIER:
                            if (currentParLevel == 0) {
                                nestedExpressions.add(new Expression(TOKEN.IDENTIFIER, lex.getIdentifier(), notFlag));
                                if (notFlag) {
                                    notFlag = false;
                                }
                            }
                            break;
                        case NUMBER:
                            if (currentParLevel == 0) {
                                nestedExpressions.add(new Expression(TOKEN.NUMBER, Integer.toString(lex.getNumber()), notFlag));
                                if (notFlag) {
                                    notFlag = false;
                                }
                            }
                            break;
                        case NOT: // not operators will be added to the next Identifier or nested expression
                            if (currentParLevel == 0) {
                                notFlag = true;
                            }
                            break;
                        default: // all other operators
                            if (currentParLevel == 0) {
                                nestedExpressions.add(new Expression(currentToken, "", notFlag));
                                if (notFlag) {
                                    notFlag = false;
                                }
                            }
                            this.token = token;
                            break;
                    }
                    lex.getNextToken();
                }
            default:
                break;
        }
    }
    /**
     * Sets the Datatype based on the Token provided.
     * 
     * 
     * @param token token with which the Expression was generated
     * @author Markus de Koster
     */
    private void setDataTypeByToken(TOKEN token) {
        switch (token) {
            case IDENTIFIER:
                dataType = 0;
                break;
            case NUMBER:
                dataType = 0;
                break;
            case AND:
                dataType = 1;
                break;
            case OR:
                dataType = 1;
                break;
            case EQUALS:
                dataType = 1;
                break;
            case UNEQUAL:
                dataType = 1;
                break;
            case LESS:
                dataType = 1;
                break;
            case LESS_EQUAL:
                dataType = 1;
                break;
            case GREATER:
                dataType = 1;
                break;
            case GREATER_EQUAL:
                dataType = 1;
                break;
            default:
                dataType = 2;
                break;
        }
    }

    /**
     * Evaluates whether the expression is true (=transition passed)
     * or false (=transition not passed) with given key, value pairs
     *
     * @param inputVector set of values and corresponding names to be used as 
     *                    input. Signal Name as String is the key. Value is a 
     *                    positive or negative integer. This implementation 
     *                    requires Constants (such as 011) to be added to input
     *                    Vectors (key is decimal value as string, value is 
     *                    decimal value) <"3", 3> for the example above.
     * @return result of evaluation, true if transition passed, false if not
     *
     * @author Markus de Koster
     */
    public boolean evaluate(ConcurrentHashMap<String, Integer> inputVector) {
        return evaluateInt(inputVector) != 0;
    }

    /**
     * Internal evaluation function
     *
     * @param inputVector set of values and corresponding names to be tested
     * @return result of evaluation, 1 if true, 0 if false, or the value if the
     * datatype is number
     *
     * @author Markus de Koster
     */
    private int evaluateInt(ConcurrentHashMap<String, Integer> inputVector) {
        switch (dataType) {
            case 0: // identifier or number
                result = inputVector.get(expressionString);
                break;
            case 1: // operator
                throw new UnsupportedOperationException("Evaluate is not supported for operators, operator found: " + token.toString());
            case 2: // expressions or nested Expressions

                /*
                    precedence of operators
                    only relevant operators shown, highest priority at top: 
                        - parantheses (already handled by datastructure)
                        - logical not (already handled by not Flag)
                        - relation operators { <,>,<=,>= }
                        - relation operators { == , != }
                        - logical AND { && }
                        - logical OR { || }
                    the following function calls have to be executed in this order
                    to comply with the above mentioned precedence of operations
                 */
                ArrayList<Expression> leftOverExpressions = nestedExpressions;
                // highest priority operators { <,>,<=,>= }
                leftOverExpressions = evaluateByPrio(leftOverExpressions, inputVector, 0);
                // second highest prio operators { == , != }
                leftOverExpressions = evaluateByPrio(leftOverExpressions, inputVector, 1);
                // third highest prio AND operator { && }
                leftOverExpressions = evaluateByPrio(leftOverExpressions, inputVector, 2);
                // fourth highest prio: OR operator { || }
                leftOverExpressions = evaluateByPrio(leftOverExpressions, inputVector, 3);

                result = leftOverExpressions.get(0).evaluateInt(inputVector);
                break; // end of case 2
            case 3: // result, thus already evaluated 
                break;
            default:
                break;
        }
        if (hasNotFlag) { // if the expression is lead by '!'
            // negate the result
            if (result == 0) {
                result = 1;
            } else {
                result = 0;
            }
        }
        return result;
    }

    /**
     * Evaluates Expressions by priority of operators. Performance optimised
     * implementation. Check developer commentary for a more detailed 
     * explanation.
     *
     * @param leftOverExpressions expressions that have not been evaluated yet
     * @param inputVector set of values and corresponding names to be used as 
     *                    input. Signal Name as String is the key. Value is a 
     *                    positive or negative integer. This implementation 
     *                    requires Constants (such as 011) to be added to input
     *                    Vectors (key is decimal value as string, value is 
     *                    decimal value) <"3", 3> for the example above
     * @param priority priority level for evaluation (1 for { <,>,<=,>= },
     *                 2 for { ==, != }, 3 for { && }, 4 for { || } )
     * @return evaluated expressions at the current priority level
     *
     * @author Markus de Koster
     */
    private ArrayList<Expression> evaluateByPrio(ArrayList<Expression> leftOverExpressions, ConcurrentHashMap<String, Integer> inputVector, int priority) {

        if (leftOverExpressions.size() < 3) { // list is already evaluated
            return leftOverExpressions;
        }
        // List iterator is used to modify the list while iterating throught it.
        // This design decision was chosen for performance issues since it
        // allows evaluation of multiple operator - operands combinations at the
        // same priority level with only one function call.
        // ConcurrentHashMap permits modification while iterating through
        ListIterator<Expression> iterator = leftOverExpressions.listIterator();
        while (iterator.hasNext()) {
            // cursor position is in front of the first element --> get next
            Expression leftExp = iterator.next(); // first identifier or number
            Expression operator = iterator.next(); // operator
            Expression rightExp = iterator.next(); // second identifier or number
            int leftValue = leftExp.evaluateInt(inputVector);
            int rightValue = rightExp.evaluateInt(inputVector);
            //variable used to save if cursor has to be moved and objects be 
            //removed after replacing an expression: 
            boolean expressionReplaced = false;

            switch (priority) {
                /*
                highest priority operators 
                    { <,>,<=,>= }
                 */
                case 0:
                    switch (operator.token) {
                        case LESS: // '<'
                            // replace last expression with new expression
                            iterator.set(new Expression(leftValue < rightValue));
                            expressionReplaced = true;
                            break;
                        case GREATER: // '>'
                            // replace last expression with new expression
                            iterator.set(new Expression(leftValue > rightValue));
                            expressionReplaced = true;
                            break;
                        case LESS_EQUAL: // '<='
                            // replace last expression with new expression
                            iterator.set(new Expression(leftValue <= rightValue));
                            expressionReplaced = true;
                            break;
                        case GREATER_EQUAL: // '>='
                            // replace last expression with new expression
                            iterator.set(new Expression(leftValue >= rightValue));
                            expressionReplaced = true;
                            break;
                        default:
                            break;
                    }
                    break;
                /*
                second highest priority operators
                    { == , != }
                 */
                case 1:
                    switch (operator.token) {
                        case EQUALS: // '=='
                            // replace last expression with new expression
                            iterator.set(new Expression(leftValue == rightValue));
                            expressionReplaced = true;
                            break;
                        case UNEQUAL: // '!='
                            // replace last expression with new expression
                            iterator.set(new Expression(leftValue != rightValue));
                            expressionReplaced = true;
                            break;
                        default:
                            break;
                    }
                    break;
                /*
                third highest priority:
                    AND operator { && }
                 */
                case 2:
                    if (operator.token == TOKEN.AND) {  // '&&'
                        // replace last expression with new expression
                        iterator.set(new Expression(leftValue != 0 && rightValue != 0));
                        expressionReplaced = true;
                    }
                    break;
                /*
                fourth highest prio: 
                    OR operator { || }
                 */
                case 3:
                    if (operator.token == TOKEN.OR) {  // '||'
                        // replace last expression with new expression
                        iterator.set(new Expression(leftValue != 0 || rightValue != 0));
                        expressionReplaced = true;
                    }
                    break;
                default:
                    break;
            }

            // Removes the now replaced conditions and moves cursor back to start
            if (expressionReplaced) {
                // move cursor over the replaced expression to the left
                iterator.previous();
                // move cursor over the previous operator to the left
                iterator.previous();
                // remove the last element that was returned (operator)
                iterator.remove();
                // move cursor over the (previous) left expression to the left
                iterator.previous();
                // cursor is back at starting position
                // remove the previous left expression
                iterator.remove();
            }

           // check whether there are enough expressions left to evaluate (>2)
            if (!iterator.hasNext()) { //list is empty
                return leftOverExpressions;
            }
            iterator.next();
            if (!iterator.hasNext()) { //list has one element
                return leftOverExpressions;
            }
            
            // move cursor back
            iterator.previous();

            // if the cursor is at a position where only an operator can be
            if (iterator.nextIndex() % 2 != 0) {
                iterator.previous(); // move cursor left
            }

        }
        return leftOverExpressions;
    }
}
