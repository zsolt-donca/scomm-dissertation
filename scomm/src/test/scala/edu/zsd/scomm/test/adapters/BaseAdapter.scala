package edu.zsd.scomm.test.adapters

import edu.zsd.festlogging.GUITestBean
import org.fest.swing.timing.Pause
import java.util.concurrent.TimeUnit

@GUITestBean
class BaseAdapter {

  def smallPause() {
    Pause.pause(100, TimeUnit.MILLISECONDS)
  }
}
