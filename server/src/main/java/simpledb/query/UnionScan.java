/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpledb.query;

/**
 *
 * @author simon
 */

public class UnionScan implements Scan{
    private Scan s1, s2;


    public UnionScan(Scan s1, Scan s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    public void beforeFirst() {
        s1.beforeFirst();
        s2.beforeFirst();
    }

    public boolean next() {
        return s1.next() && s2.next();
    }

 
    public void close() {
        s1.close();
        s2.close();
    }

    public Constant getVal(String fldname) {
        if (s1.hasField(fldname))
            return s1.getVal(fldname);
        else
            return s2.getVal(fldname);
    }

    public int getInt(String fldname) {
        if (s1.hasField(fldname))
            return s1.getInt(fldname);
        else
            return s2.getInt(fldname);
    }

    public String getString(String fldname) {
        if (s1.hasField(fldname))
            return s1.getString(fldname);
        else
            return s2.getString(fldname);
    }


    public boolean hasField(String fldname) {
        return s1.hasField(fldname) || s2.hasField(fldname);
    }
}