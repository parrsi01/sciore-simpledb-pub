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
public class ExtendScan implements Scan {
    private Scan s1;
    private String field;
    private Integer type, size;


    public ExtendScan(Scan s1, String field, Integer type, Integer size) {
        this.s1 = s1;
        s1.next();
        this.field = field;
        this.type = type;
        this.size = size;
    }

    public void beforeFirst() {
        s1.beforeFirst();
        
    }

    public boolean next() {
        return s1.next();
    }

    public void close() {
        s1.close();
    }

    public Constant getVal(String fldname) {
        return s1.getVal(fldname);
    }

    public int getInt(String fldname) {
        return s1.getInt(fldname);
    }

    public String getString(String fldname) {
        return s1.getString(fldname);
    }

    public boolean hasField(String fldname) {
        return s1.hasField(fldname);
    }
}