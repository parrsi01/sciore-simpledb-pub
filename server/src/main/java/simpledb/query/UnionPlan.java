/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpledb.query;
import simpledb.metadata.StatInfo;
import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.server.SimpleDB;
import simpledb.tx.*;

/**
 *
 * @author simon
 */

public class UnionPlan implements Plan{
    private Plan p1, p2;
    private Schema schema = new Schema();


    public UnionPlan(Plan p1, Plan p2) {
        this.p1 = p1;
        this.p2 = p2;
        schema.addAll(p1.schema());
        schema.addAll(p2.schema());
    }

    /**
     * Creates a product scan for this query.
     * @see simpledb.query.Plan#open()
     */
    public Scan open() {
        Scan s1 = p1.open();
        Scan s2 = p2.open();
        return new UnionScan(s1,s2);
    }

    
    public int blocksAccessed() {
        return p1.blocksAccessed() + (p1.recordsOutput() * p2.blocksAccessed());
    }

    
    public int recordsOutput() {
        return p1.recordsOutput() + p2.recordsOutput();
    }

    public int distinctValues(String fldname) {
        if (p1.schema().hasField(fldname))
            return p1.distinctValues(fldname);
        else
            return p2.distinctValues(fldname);
    }

    public Schema schema() {
        return schema;
    }
}