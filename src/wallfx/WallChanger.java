/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wallfx;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;
import java.util.HashMap;
import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;
import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;

public class WallChanger
{
   
    public enum StyleType 
    {
        CenterTile(0),
        Stretched(2),
        Fit(6),
        Fill(10);
        
        private int value;
        
        StyleType(int val)
        {
            this.value = val;
        }
        
        public int getOrdinal()
        {
            return value;
        }
    }
    
 private String path;
 public static void main(String[] args) {
      //supply your own path instead of using this one
      String testpath = "C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg";

      SPI.INSTANCE.SystemParametersInfo(
          new UINT_PTR(SPI.SPI_SETDESKWALLPAPER), 
          new UINT_PTR(0), 
          testpath, 
          new UINT_PTR(SPI.SPIF_UPDATEINIFILE | SPI.SPIF_SENDWININICHANGE));
   }
 
    public void load(String path)
    {
          this.path = path;
          SPI.INSTANCE.SystemParametersInfo(
          new UINT_PTR(SPI.SPI_SETDESKWALLPAPER), 
          new UINT_PTR(0), 
          path, 
          new UINT_PTR(SPI.SPIF_UPDATEINIFILE | SPI.SPIF_SENDWININICHANGE));
    }

    public void setTiled(int flag)
    {   
         Advapi32Util.registrySetStringValue(HKEY_CURRENT_USER, "Control Panel\\Desktop", "TileWallpaper", String.valueOf(flag) );        
         Advapi32Util.registrySetStringValue(HKEY_CURRENT_USER, "Control Panel\\Desktop", "WallpaperStyle", String.valueOf(StyleType.CenterTile.getOrdinal()) );
         this.load(this.path);        
    }
    public void setStyle( StyleType type )
    {
         Advapi32Util.registrySetStringValue(HKEY_CURRENT_USER, "Control Panel\\Desktop", "WallpaperStyle", String.valueOf(type.getOrdinal()) );
         this.load(this.path);
    }
    public interface SPI extends StdCallLibrary {

      //from MSDN article
      long SPI_SETDESKWALLPAPER = 20;
      long SPIF_UPDATEINIFILE = 0x01;
      long SPIF_SENDWININICHANGE = 0x02;

      SPI INSTANCE = (SPI) Native.loadLibrary("user32", SPI.class, new HashMap<Object, Object>() {
         {
            put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
            put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
         }
      });

      boolean SystemParametersInfo(
          UINT_PTR uiAction,
          UINT_PTR uiParam,
          String pvParam,
          UINT_PTR fWinIni
        );
  }
}