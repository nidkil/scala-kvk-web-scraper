package nl.newparadigm.util

class Timer {

  private var startTime = 0L
  private var stopTime = 0L
  
  def start() = { startTime = System.currentTimeMillis() }
  
  def stop() = { stopTime = System.currentTimeMillis() }
  
  def execTime() : String = "%s msecs".format(stopTime - startTime)

}