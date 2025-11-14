

package com.comp2042.tetris.model.score;


public final class Score {

    private int value;

    public int getValue() {
        return value;
    }

    public void add(int delta){
        value += delta;
    }

    public void reset() {
        value =0 ;
    }
}
//changed the field name score to value to avoid confusion between method name and field name