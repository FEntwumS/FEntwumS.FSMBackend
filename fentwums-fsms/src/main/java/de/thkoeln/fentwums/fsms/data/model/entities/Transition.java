package de.thkoeln.fentwums.fsms.data.model.entities;

import java.util.ArrayList;
import java.awt.Point;
import de.thkoeln.fentwums.fsms.data.datalogic.impl.Expression;
import java.util.Hashtable;
import java.util.List;

/**
 * The transitions between different, or the same state.
 *
 * @author Linus Schoendorf and Marvin Jolk
 */
public class Transition {

    private Expression expression;
    private Point conditionPosition;
    private Point startPoint;
    private Point endPoint;
    private ArrayList<Point> ctrlPoints;
    private String target;

    //only for Mealy
    private Hashtable<Signal, Integer> mealyOutputSignals;

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Point getConditionPosition() {
        return conditionPosition;
    }

    public void setConditionPosition(Point conditionPosition) {
        this.conditionPosition = conditionPosition;
    }

    public ArrayList<Point> getCtrlPoints() {
        return ctrlPoints;
    }

    public void setCtrlPoints(ArrayList<Point> ctrlPoints) {
        this.ctrlPoints = ctrlPoints;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    //only for Mealy
    public Hashtable<Signal, Integer> getMealyOutputSignals() {
        return mealyOutputSignals;
    }

    //only for Mealy
    public void setMealyOutputSignals(Hashtable<Signal, Integer> mealyOutputSignals) {
        this.mealyOutputSignals = mealyOutputSignals;
    }

}
