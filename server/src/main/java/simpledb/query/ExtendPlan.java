/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpledb.query;
import simpledb.record.Schema;
import java.util.Map;

/**
 *
 * @author simon
 */

public class ExtendPlan implements Plan {
    private Plan p1;
    private Schema schema = new Schema();
    private String field;
    private Integer type, size;


    public ExtendPlan(Plan p1, String field, Integer type, Integer size) {
        this.p1 = p1;
        this.field = field;
        this.type = type;
        this.size = size;
        p1.schema().addField(field, type, size);
        schema.addAll(p1.schema());
    }

    public Scan open() {
        Scan s1 = p1.open();
        return new ExtendScan(s1,field,type,size);
    }

    public int blocksAccessed() {
        return p1.blocksAccessed();
    }

    public int recordsOutput() {
        return p1.recordsOutput();
    }

    public int distinctValues(String fldname) {
        return p1.distinctValues(fldname);
    }

    public Schema schema() {
        return schema;
    }
}