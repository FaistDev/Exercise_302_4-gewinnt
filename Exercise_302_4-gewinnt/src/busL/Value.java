/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busL;

/**
 *
 * @author Ben
 */

public enum Value {
    X(1),O(-1),EMPTY(0),FLIP(23);
    
    private final int num;
    
    Value(int num){
        this.num=num;
    }
    
    public int getNum() {
        return num;
    }
}


