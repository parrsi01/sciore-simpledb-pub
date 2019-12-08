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
public class RenamePlan implements Plan {
    private Plan p1;
    private Schema schema = new Schema();
    private String tgtCol, renameCol;


    public RenamePlan(Plan p1, String tgtCol, String renameCol) {
        this.p1 = p1;
        this.tgtCol = tgtCol;
        this.renameCol = renameCol;
        executeRename();
        schema.addAll(p1.schema());
    }

    public void executeRename(){
        if (p1.schema().hasField(tgtCol)) {
            Map<String, Schema.FieldInfo> info = p1.schema().getInfo();
            Schema.FieldInfo retainField = info.remove(tgtCol);
            info.put(renameCol, retainField);
            p1.schema().setInfo(info);
        }

    }

    public Scan open() {
        Scan s1 = p1.open();
        return new RenameScan(s1, tgtCol, renameCol);
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