  package com.qksnap.www.snap.gui.tabpane.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ImgCache<Key, Val> {
	  
	private class CacheVal {
	    public int rank;
	    public Val val;
			
	    public CacheVal(int rank, Val val) {
	      this.rank = rank;
	      this.val = val;
	    }
	  }
		
	  public ImgCache(int limit) {
	    this.limit = limit;
	  }
		

	  public Val get(Key key) {
	    if (this.cache.containsKey(key)) {
	      CacheVal cacheVal;
	      // remove existing entry from ranked list
	      cacheVal = this.cache.get(key);
	      Key k = this.rankedList.remove(cacheVal.rank);
	      // add it to the top of the list
	      this.rankedList.addFirst(k);
	      return cacheVal.val;
	    } else {
	      return null;
	    }
	  }
		
	  public void set(Key key, Val val) {
	    // remove existing entry from ranked
	    if (this.cache.containsKey(key)) {
	      CacheVal oldVal;
	      oldVal = this.cache.get(key);
	      this.rankedList.remove(oldVal.rank);
	    }
			
	    // check if cache is full
	    if (this.cache.size() == this.limit) {
	      // make space in cache
	      Key toRemove = this.rankedList.removeLast();
	      this.cache.remove(toRemove);
	    }


	// insert new element in cache and ranked list
	    CacheVal newVal = new CacheVal(0, val);
	    this.cache.put(key, newVal);
	    this.rankedList.addFirst(key);
	  }
		
	  private Map<Key, CacheVal> cache = new HashMap<Key, CacheVal>();
	  private LinkedList<Key> rankedList = new LinkedList<Key>();
	  private int limit;
	}