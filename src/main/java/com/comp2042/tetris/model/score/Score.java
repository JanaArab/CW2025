/**
 * keep track of score and keep updating it on the screen
 */

package com.comp2042.tetris.model.score;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty value = new SimpleIntegerProperty(0);

    public IntegerProperty scoreProperty() {
        return value;
    }

    public void add(int i){
        value.setValue(value.getValue() + i);
    }

    public void reset() {
        value.setValue(0);
    }
}
//changed the field name score to value to avoid confusion between method name and field name