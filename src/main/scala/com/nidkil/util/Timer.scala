package com.nidkil.util

class Timer {

  private var startTime = 0L
  private var stopTime = 0L
  
  def start() = { startTime = System.currentTimeMillis() }
  
  def stop() = { stopTime = System.currentTimeMillis() }
  
  def execTime(msecs : Boolean = true) : String = {
    if(msecs) "%s msecs".format(stopTime - startTime) 
    else "%s,%s secs".format((stopTime - startTime) / 1000, ((stopTime - startTime) % 1000).toString().padTo(4, "0").mkString) 
  }

}