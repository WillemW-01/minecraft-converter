import princeton.Picture;
import princeton.StdIn;
import princeton.In;
import java.awt.Color;
import java.util.ArrayList;

public class TextureHandler {

  public static ArrayList<String> textureNames = new ArrayList<>();
  public static ArrayList<Picture> textures = new ArrayList<>();
  public static ArrayList<Color> textureColors = new ArrayList<>();

  public static void writeTextureData() {
    In fileNames = new In("filenames.txt");
    while (fileNames.hasNextLine()) {
      String str = fileNames.readLine();
      textureNames.add(str);
    }

    for (String str : textureNames) {
      Picture currentTexture = new Picture("textures/"+str);
      int r = 0, g = 0, b = 0;
      for (int i = 0; i < 16; i++) {
        for (int j = 0; j < 16; j++) {
          Color c  = currentTexture.get(j, i);
          r += c.getRed(); g += c.getGreen(); b += c.getBlue();
        }
      }
      textureColors.add(new Color(r/256, g/256, b/256));
      Picture temp = new Picture(16, 16);
      for (int i = 0; i < 16; i++) {
        for (int j = 0; j < 16; j++) {
          temp.set(j, i, new Color(r/256, g/256, b/256));
        }
      }
      temp.save("texturemaps/"+str);
      System.out.println(str+","+(r/256)+","+(b/256)+","+(g/256));
      textures.add(currentTexture);
    }
  }

  public static void main(String[] args) {
    writeTextureData();
  }

}
