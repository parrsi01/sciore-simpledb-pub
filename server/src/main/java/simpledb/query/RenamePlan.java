
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpledb.query;
import simpledb.record.Schema;

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
