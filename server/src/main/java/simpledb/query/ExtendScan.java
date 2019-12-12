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
    private Scan s;
    private String field;
    private Integer type, size;


    public ExtendScan(Scan s, String field, Integer type, Integer size) {
        this.s = s;
        s.next();
        this.field = field;
        this.type = type;
        this.size = size;
    }

    public void beforeFirst() {
        s.beforeFirst();
        
    }

    public boolean next() {
        return s.next();
    }

    public void close() {
        s.close();
    }

    public Constant getVal(String fldname) {
        return s.getVal(fldname);
    }

    public int getInt(String fldname) {
        return s.getInt(fldname);
    }

    public String getString(String fldname) {
        return s.getString(fldname);
    }

    public boolean hasField(String fldname) {
        return s.hasField(fldname);
    }
}