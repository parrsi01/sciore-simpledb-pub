/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpledb.query;
import simpledb.record.Schema;
import java.util.*;

/**
 *
 * @author simon
 */

public class ExtendPlan implements Plan {
    private Plan p;
    private Schema schema = new Schema();
    private String field;
    private Integer type, size;


    public ExtendPlan(Plan p, String field, Integer type, Integer size) {
        this.p = p;
        this.field = field;
        this.type = type;
        this.size = size;
        p.schema().addField(field, type, size);
        schema.addAll(p.schema());
    }

    public Scan open() {
        Scan s = p.open();
        return new ExtendScan(s,field,type,size);
    }

    public int blocksAccessed() {
        return p.blocksAccessed();
    }

    public int recordsOutput() {
        return p.recordsOutput();
    }

    public int distinctValues(String fldname) {
        return p.distinctValues(fldname);
    }

    public Schema schema() {
        return schema;
    }
}