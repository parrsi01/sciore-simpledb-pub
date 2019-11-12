package simpledb.buffer;

import simpledb.file.*;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
class BasicBufferMgr {
   private Buffer[] bufferpool;
   private int numAvailable;
   private Integer clockhand;
   private int strategy;
   
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    */
   BasicBufferMgr(int numbuffs) {
      bufferpool = new Buffer[numbuffs];
      numAvailable = numbuffs;
      for (int i=0; i<numbuffs; i++)
         bufferpool[i] = new Buffer();
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   synchronized void flushAll(int txnum) {
      for (Buffer buff : bufferpool)
         if (buff.isModifiedBy(txnum))
         buff.flush();
   }
   
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
   synchronized Buffer pin(Block blk) {
      Buffer buff = findExistingBuffer(blk);
      if (buff == null) {
         buff = chooseUnpinnedBuffer();
         if (buff == null)
            return null;
         buff.assignToBlock(blk);
      }
      if (!buff.isPinned())
         numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a pageformatter object, used to format the new block
    * @return the pinned buffer
    */
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
      Buffer buff = chooseUnpinnedBuffer();
      if (buff == null)
         return null;
      buff.assignToNew(filename, fmtr);
      numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   synchronized void unpin(Buffer buff) {
      buff.unpin();
      if (!buff.isPinned())
         numAvailable++;
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   
   private Buffer findExistingBuffer(Block blk) {
      for (Buffer buff : bufferpool) {
         Block b = buff.block();
         if (b != null && b.equals(blk))
            return buff;
      }
      return null;
   }
   
   private Buffer chooseUnpinnedBuffer() {
      for (Buffer buff : bufferpool)
         if(buff.block() == null){
             return buff;
         }
      
      return null;
   }
   
   public Buffer[] getBuffers() {
        return this.bufferpool;
    }

    /**
     * Set buffer selection strategy
     *
     * @param s (cases: 0 - Naive, 1 - FIFO, 2 - LRU, 3 - Clock)
     */
    public void setStrategy(int s) {
        this.strategy = s;
    }
    
    private Buffer useNaiveStrategy() {
        for (Buffer b : bufferpool) {
            if (!b.isPinned()) {
                return b;
            }
        }
        switch (this.strategy) {
            case 0:
                return useNaiveStrategy();
            case 1:
                return useFIFOStrategy();
            case 2:
                return useLRUStrategy();
            case 3:
                return useClockStrategy();
            default:
                return null;
    } 
}
    
    private Buffer useFIFOStrategy() {
        Buffer dbBuffer = null;
        long min = Long.MAX_VALUE; // shortest time in pool
        for (Buffer b : bufferpool){
            if(!b.isPinned() && (b.getTimeIn() < min)){
                min = b.getTimeIn();
                dbBuffer = b;
            }
        }
        return dbBuffer;
    }
    
    private Buffer useLRUStrategy() {
        Buffer dbBuffer = null;
        long min = Long.MAX_VALUE; //shortest time out
        for (Buffer b : bufferpool){
            if(!b.isPinned() && (b.getTimeOut() < min)){
                min = b.getTimeOut();
                dbBuffer = b;
            }
        }
        return dbBuffer;
    }
    
    private Buffer useClockStrategy() {
        if(clockhand == null){
            long lastUnpin = Long.MIN_VALUE; //last unpinned buffer
            for(int i = 0; i < bufferpool.length; ++i){
                if (!bufferpool[i].isPinned() && bufferpool[i].getTimeOut() > lastUnpin){
                    clockhand = i;
                    lastUnpin = bufferpool[clockhand].getTimeOut();
                }
            }
        }
        for(int i = clockhand + 1; i < bufferpool.length + clockhand; ++i){
            if(!bufferpool[i % bufferpool.length].isPinned()){
                clockhand = i % bufferpool.length;
                return bufferpool[i % bufferpool.length];
            }
        }
        return null;
    }
}

    