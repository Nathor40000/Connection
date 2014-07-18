package NCWMod.CCTV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class PlasmaProps
{
  private String fileName;
  private List lines = new ArrayList();
  private Map props = new HashMap();

  public PlasmaProps(String var1)
  {
    this.fileName = var1;
    File var2 = new File(this.fileName);
    if (var2.exists())
      try {
        load();
      } catch (IOException var4) {
        System.out.println(new StringBuilder().append("[PlasmaProps] Unable to load ").append(this.fileName).append("!").toString());
      }
    else
      save();
  }

  public void load()
    throws IOException
  {
    BufferedReader var1 = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileName), "UTF8"));
    this.lines.clear();
    this.props.clear();
    String var2;
    while ((var2 = var1.readLine()) != null) {
      var2 = new String(var2.getBytes(), "UTF-8");
      boolean var3 = false;

      char var14 = '\000';
      for (int var4 = 0; (var4 < var2.length()) && (Character.isWhitespace(var14 = var2.charAt(var4))); var4++);
      if ((var2.length() - var4 != 0) && (var2.charAt(var4) != '#') && (var2.charAt(var4) != '!')) {
        boolean var6 = var2.indexOf(92, var4) != -1;
        StringBuffer var7 = var6 ? new StringBuffer() : null;
        if (var7 != null)
        {
          while ((var4 < var2.length()) && (!Character.isWhitespace(var14 = var2.charAt(var4++))) && (var14 != '=') && (var14 != ':')) {
            if ((var6) && (var14 == '\\')) {
              if (var4 == var2.length()) {
                var2 = var1.readLine();
                if (var2 == null) {
                  var2 = "";
                }

                var4 = 0;

                while ((var4 < var2.length()) && (Character.isWhitespace(var14 = var2.charAt(var4))))
                {
                  var4++;
                }
              }
              else {
                var14 = var2.charAt(var4++);
              }
            }
            else switch (var14) {
              case 'n':
                var7.append('\n');
                break;
              case 'o':
              case 'p':
              case 'q':
              case 's':
              default:
                var7.append('\000');
                break;
              case 'r':
                var7.append('\r');
                break;
              case 't':
                var7.append('\t');
                break;
              case 'u':
                if (var4 + 4 <= var2.length()) {
                  char var8 = (char)Integer.parseInt(var2.substring(var4, var4 + 4), 16);
                  var7.append(var8);
                  var4 += 4;
                }
                break;
              }
          }
        }
        boolean var15 = (var14 == ':') || (var14 == '=');
        String var9;
        String var9;
        if (var6) {
          var9 = var7.toString();
        }
        else
        {
          String var9;
          if ((!var15) && (!Character.isWhitespace(var14)))
            var9 = var2.substring(var4, var4);
          else {
            var9 = var2.substring(var4, var4 - 1);
          }
        }
        while ((var4 < var2.length()) && (Character.isWhitespace(var14 = var2.charAt(var4)))) {
          var4++;
        }

        if ((!var15) && ((var14 == ':') || (var14 == '='))) {
          var4++;

          while ((var4 < var2.length()) && (Character.isWhitespace(var2.charAt(var4)))) {
            var4++;
          }
        }

        if (!var6) {
          this.lines.add(var2);
        } else {
          StringBuilder var10 = new StringBuilder(var2.length() - var4);

          while (var4 < var2.length()) {
            char var11 = var2.charAt(var4++);
            if (var11 == '\\') {
              if (var4 == var2.length()) {
                var2 = var1.readLine();
                if (var2 == null)
                {
                  break;
                }
                for (var4 = 0; (var4 < var2.length()) && (Character.isWhitespace(var2.charAt(var4))); var4++);
                var10.ensureCapacity(var2.length() - var4 + var10.length());
              }
              else
              {
                char var12 = var2.charAt(var4++);
                switch (var12) {
                case 'n':
                  var10.append('\n');
                  break;
                case 'o':
                case 'p':
                case 'q':
                case 's':
                default:
                  var10.append('\000');
                  break;
                case 'r':
                  var10.append('\r');
                  break;
                case 't':
                  var10.append('\t');
                  break;
                case 'u':
                  if (var4 + 4 > var2.length())
                  {
                    continue;
                  }
                  char var13 = (char)Integer.parseInt(var2.substring(var4, var4 + 4), 16);
                  var10.append(var13);
                  var4 += 4;
                }
              }
            }
            else var10.append('\000');
          }

          this.lines.add(new StringBuilder().append(var9).append("=").append(var10.toString()).toString());
        }
      } else {
        this.lines.add(var2);
      }
    }

    var1.close();
  }

  public void save() {
    FileOutputStream var1 = null;
    try
    {
      var1 = new FileOutputStream(this.fileName);
    } catch (FileNotFoundException var11) {
      System.out.println(new StringBuilder().append("[PlasmaProps] Unable to open ").append(this.fileName).append("!").toString());
    }

    PrintStream var2 = null;
    try
    {
      var2 = new PrintStream(var1, true, "UTF-8");
    } catch (UnsupportedEncodingException var10) {
      System.out.println(new StringBuilder().append("[PlasmaProps] Unable to write to ").append(this.fileName).append("!").toString());
    }

    ArrayList var3 = new ArrayList();
    Iterator var4 = this.lines.iterator();

    while (var4.hasNext()) {
      String var5 = (String)var4.next();
      if (var5.trim().length() == 0) {
        var2.println(var5);
      } else if (var5.charAt(0) == '#') {
        var2.println(var5);
      } else if (var5.contains("=")) {
        int var6 = var5.indexOf(61);
        String var7 = var5.substring(0, var6).trim();
        if (this.props.containsKey(var7)) {
          String var8 = (String)this.props.get(var7);
          var2.println(new StringBuilder().append(var7).append("=").append(var8).toString());
          var3.add(var7);
        } else {
          var2.println(var5);
        }
      } else {
        var2.println(var5);
      }
    }

    var4 = this.props.entrySet().iterator();

    while (var4.hasNext()) {
      Map.Entry var12 = (Map.Entry)var4.next();
      if (!var3.contains(var12.getKey())) {
        var2.println(new StringBuilder().append((String)var12.getKey()).append("=").append((String)var12.getValue()).toString());
      }
    }

    var2.close();
    try
    {
      this.props.clear();
      this.lines.clear();
      load();
    } catch (IOException var9) {
      System.out.println(new StringBuilder().append("[PlasmaProps] Unable to load ").append(this.fileName).append("!").toString());
    }
  }

  public Map returnMap() throws Exception
  {
    HashMap var1 = new HashMap();
    BufferedReader var2 = new BufferedReader(new FileReader(this.fileName));
    String var3;
    while ((var3 = var2.readLine()) != null) {
      if ((var3.trim().length() != 0) && (var3.charAt(0) != '#') && (var3.contains("="))) {
        int var4 = var3.indexOf(61);
        String var5 = var3.substring(0, var4).trim();
        String var6 = var3.substring(var4 + 1).trim();
        var1.put(var5, var6);
      }
    }

    var2.close();
    return var1;
  }

  public boolean containsKey(String var1) {
    Iterator var2 = this.lines.iterator();

    while (var2.hasNext()) {
      String var3 = (String)var2.next();
      if ((var3.trim().length() != 0) && (var3.charAt(0) != '#') && (var3.contains("="))) {
        int var4 = var3.indexOf(61);
        String var5 = var3.substring(0, var4);
        if (var5.equals(var1)) {
          return true;
        }
      }
    }

    return false;
  }

  public String getProperty(String var1) {
    Iterator var2 = this.lines.iterator();

    while (var2.hasNext()) {
      String var3 = (String)var2.next();
      if ((var3.trim().length() != 0) && (var3.charAt(0) != '#') && (var3.contains("="))) {
        int var4 = var3.indexOf(61);
        String var5 = var3.substring(0, var4).trim();
        String var6 = var3.substring(var4 + 1);
        if (var5.equals(var1)) {
          return var6;
        }
      }
    }

    return "";
  }

  public void removeKey(String var1) {
    Boolean var2 = Boolean.valueOf(false);
    if (this.props.containsKey(var1)) {
      this.props.remove(var1);
      var2 = Boolean.valueOf(true);
    }
    try
    {
      for (int var3 = 0; var3 < this.lines.size(); var3++) {
        String var4 = (String)this.lines.get(var3);
        if ((var4.trim().length() != 0) && (var4.charAt(0) != '#') && (var4.contains("="))) {
          int var5 = var4.indexOf(61);
          String var6 = var4.substring(0, var5).trim();
          if (var6.equals(var1)) {
            this.lines.remove(var3);
            var2 = Boolean.valueOf(true);
          }
        }
      }
    } catch (ConcurrentModificationException var7) {
      removeKey(var1);
      return;
    }

    if (var2.booleanValue())
      save();
  }

  public boolean keyExists(String var1)
  {
    try
    {
      return containsKey(var1); } catch (Exception var3) {
    }
    return false;
  }

  public String i(String var1)
  {
    return containsKey(var1) ? getProperty(var1) : "";
  }

  public String i(String var1, String var2) {
    if (containsKey(var1)) {
      return getProperty(var1);
    }
    a(var1, var2);
    return var2;
  }

  public void a(String var1, String var2)
  {
    this.props.put(var1, var2);
    save();
  }

  public int getInt(String var1) {
    return containsKey(var1) ? Integer.parseInt(getProperty(var1)) : 0;
  }

  public int getInt(String var1, int var2) {
    if (containsKey(var1)) {
      return Integer.parseInt(getProperty(var1));
    }
    setInt(var1, var2);
    return var2;
  }

  public void setInt(String var1, int var2)
  {
    this.props.put(var1, String.valueOf(var2));
    save();
  }

  public double h(String var1) {
    return containsKey(var1) ? Double.parseDouble(getProperty(var1)) : 0.0D;
  }

  public double h(String var1, double var2) {
    if (containsKey(var1)) {
      return Double.parseDouble(getProperty(var1));
    }
    a(var1, var2);
    return var2;
  }

  public void a(String var1, double var2)
  {
    this.props.put(var1, String.valueOf(var2));
    save();
  }

  public long f(String var1) {
    return containsKey(var1) ? Long.parseLong(getProperty(var1)) : 0L;
  }

  public long f(String var1, long var2) {
    if (containsKey(var1)) {
      return Long.parseLong(getProperty(var1));
    }
    a(var1, var2);
    return var2;
  }

  public void a(String var1, long var2)
  {
    this.props.put(var1, String.valueOf(var2));
    save();
  }

  public boolean m(String var1) {
    return containsKey(var1) ? Boolean.parseBoolean(getProperty(var1)) : false;
  }

  public boolean m(String var1, boolean var2) {
    if (containsKey(var1)) {
      return Boolean.parseBoolean(getProperty(var1));
    }
    a(var1, var2);
    return var2;
  }

  public void a(String var1, boolean var2)
  {
    this.props.put(var1, String.valueOf(var2));
    save();
  }
}