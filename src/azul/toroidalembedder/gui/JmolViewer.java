/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.JFrame;
import org.openscience.jmol.DisplaySettings;
import org.openscience.miniJmol.JmolApplet;

/**
 *
 * @author nvcleemp
 */
public class JmolViewer
 implements AppletContext, AppletStub
{
  static private boolean debug = false;
  static private Hashtable<String, String> defaultParams;

  static {
    float atomSize = 0.75f;
    float zoom = 1.0f;
    defaultParams = new Hashtable<String, String>();
    defaultParams.put("FCOLOUR", "#000000");
    defaultParams.put("STYLE", "HALFSHADED");
    defaultParams.put("WIREFRAMEROTATION", "OFF");
    defaultParams.put("ZOOM", Float.toString(zoom));
    defaultParams.put("ATOMSIZE", Float.toString(atomSize));
  }

  public void setParameter(String name, String value)
  {
    params.put(name, value);
  }

  private JmolApplet applet;
  private boolean appletStarted;
  private URL baseURL;
  private Hashtable<String, String> params;
  private DisplaySettings displaySettings = null;

  private JFrame frame;
  private int configWidth, configHeight;

  // private int[] dim;

  public JmolViewer()
  {
    applet = null;
    appletStarted = false;
    try {
      baseURL = new URL("file", "", System.getProperty("user.dir") + java.io.File.separator);
    } catch (MalformedURLException e) {
      if (debug) System.err.println("baseURL: MalformedURLException");
      baseURL = null;
    }
    params = new Hashtable<String,String>();
    defaultParams.put("BCOLOUR", htmlColour(SystemColor.window));
    configWidth  = 550;
    configHeight = 500;
    createFrame();
  }

  private void createFrame()
  {
    if (frame != null) {
      return;
    }
    frame = new JFrame("Jmol Viewer Applet");
    appletResize(configWidth, configHeight);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        stopApplet();
        frame.setVisible(false);
      }
    });
  }

  String htmlColour(Color c)
  {
    String x = Integer.toHexString(c.getRGB() & 0x00ffffff), y = "#000000";
    return y.substring(0, 7 - x.length()) + x;
  }

  void installApplet()
  {
    applet = new JmolApplet();
    applet.setBackground(frame.getBackground());
    appletStarted = false;
    frame.add(applet, BorderLayout.CENTER);
  }

  void uninstallApplet()
  {
    stopApplet();
    if (applet != null) {
      try {
	displaySettings = applet.getDisplaySettings();
      } catch (NoSuchMethodError er) {
      }
      if (frame != null) frame.remove(applet);
    }
    applet = null;
  }

  void startApplet()
  {
    if (appletStarted) return;
    if (debug) System.err.println("setting stub");
    applet.setStub(this);
    if (debug) System.err.println("calling init");
    try {
      applet.init();
    } catch (Exception ex) {
      appletStarted = true;
      return;
    }
    if (displaySettings != null) {
      try {
	applet.setDisplaySettings(displaySettings);
      } catch (NoSuchMethodError er) {
      }
    }
    if (debug) System.err.println("calling start");
    applet.start();
    if (debug) System.err.println("applet started");
    appletStarted = true;
  }

  void stopApplet()
  {
    if (! appletStarted) return;
    if (debug) System.err.println("calling stop");
    applet.stop();
    if (debug) System.err.println("calling destroy");
    applet.destroy();
    if (debug) System.err.println("applet destroyed");
    appletStarted = false;
  }

  public static void main(String argv[])
  {
    JmolViewer t;
    t = new JmolViewer();
    if (debug) System.err.println("applet installed");
    if (argv.length > 1) {
      t.setParameter("MODEL", argv[0]);
      t.setParameter("FORMAT", argv[1]);
    } else {
      t.setParameter("MODEL", "/Users/nvcleemp/00000.cml");
      t.setParameter("FORMAT", "CML");
    }
    t.installApplet();
    t.startApplet();
    t.frame.setVisible(true);
  }
  
  public void show(){
    installApplet();
    startApplet();
    frame.setVisible(true);
  }

/* *** AppletContext methods *************************************** */

  public AudioClip getAudioClip(URL url)
  {
    if (debug) System.err.println("getAudioClip: " + url);
    return null;
  }

  public Image getImage(URL url)
  {
    if (debug) System.err.println("getImage: " + url);
    return Toolkit.getDefaultToolkit ().getImage (url);
  }

  public Applet getApplet(String name)
  {
    if (debug) System.err.println("getApplet: " + name);
    return null;
  }

  public Iterator<String> getStreamKeys()
  {
	return null;
  }
  
  public InputStream getStream(String key)
  {
	return null;
  }
  
  public void setStream(String key, InputStream stream)
  {
	//
  }
  
  public java.util.Enumeration<java.applet.Applet> getApplets()
  {
    if (debug) System.err.println("getApplets");
    return null;
  }

  public void showDocument(URL url)
  {
    if (debug) System.err.println("showDocument: " + url);
  }

  public void showDocument(URL url, String target)
  {
    if (debug) System.err.println("showDocument: " + url + ", " + target);
  }

  public void showStatus(String msg)
  {
  }

/* *** AppletStub methods ****************************************** */

  public boolean isActive()
  {
    if (debug) System.err.println("isActive");
    return true;
  }

  public URL getDocumentBase()
  {
    if (debug) System.err.println("getDocumentBase > " + baseURL);
    return baseURL;
  }

  public URL getCodeBase()
  {
    if (debug) System.err.println("getCodeBase > " + baseURL);
    return baseURL;
  }

  public String getParameter(String name)
  {
    if (debug) System.err.println("getParameter: " + name);
    String result;
    if ((result = (String) params.get(name)) != null) {
      return result;
    } else if ((result = (String) defaultParams.get(name)) != null) {
      return result;
    } else {
      return null;
    }
  }

  public AppletContext getAppletContext()
  {
    return this;
  }

  public void appletResize(int width, int height)
  {
    if (debug) System.err.println("appletResize");

    Insets insets = frame.getInsets();
    frame.setSize(
     width + insets.left + insets.right,
     height + insets.top + insets.bottom);
  }
  
  public void destroy(){
      frame.setVisible(false);
      frame.dispose();
  }
  
  public Rectangle getBounds(){
      return frame.getBounds();
  }
  
  public void setBounds(Rectangle bounds){
      if(bounds!=null)
          frame.setBounds(bounds);
  }
}
