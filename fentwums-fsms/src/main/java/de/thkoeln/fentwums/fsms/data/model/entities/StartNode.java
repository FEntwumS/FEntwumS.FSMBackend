/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thkoeln.fentwums.fsms.data.model.entities;

import java.awt.Point;

/**
 * The start node of the state machine.
 *
 * @author Marvin Jolk
 */
public class StartNode {

    State target;
    Integer condition;
    Point conditionPosition;
    Point position;
    Point targetPosition;

    public State getTarget() {
        return target;
    }

    public void setTarget(State target) {
        this.target = target;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Point targetPosition) {
        this.targetPosition = targetPosition;
    }

    public Point getConditionPosition() {
        return conditionPosition;
    }

    public void setConditionPosition(Point conditionPosition) {
        this.conditionPosition = conditionPosition;
    }

}
