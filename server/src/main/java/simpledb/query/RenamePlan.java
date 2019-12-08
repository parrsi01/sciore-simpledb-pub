
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
    private Plan p;
    private Schema schema = new Schema();
    
    public RenamePlan(Plan p, String oldName, String newName) {
        this.p = p;
        int type = p.schema().type(oldName);
        int length = p.schema().length(oldName);
        for (String fldname: p.schema().fields())
            if (!fldname.equals(oldName)) {
                schema.add(fldname, p.schema());
            }
        schema.addField(newName, type, length);
    }
    
    public Scan open() {
      Scan s = p.open();
      return new ProjectScan(s, schema.fields());
   }
   
   /**
    * Estimates the number of block accesses in the projection,
    * which is the same as in the underlying query.
    */
   public int blocksAccessed() {
      return p.blocksAccessed();
   }
   
   /**
    * Estimates the number of output records in the projection,
    * which is the same as in the underlying query.
    */
   public int recordsOutput() {
      return p.recordsOutput();
   }
   
   /**
    * Estimates the number of distinct field values
    * in the projection,
    * which is the same as in the underlying query.
    */
   public int distinctValues(String fldname) {
      return p.distinctValues(fldname);
   }
   
   /**
    * Returns the schema of the projection,
    * which is taken from the field list.
    */
   public Schema schema() {
      return schema;
   }
}
