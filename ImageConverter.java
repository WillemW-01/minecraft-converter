import princeton.Picture;
import princeton.StdIn;
import princeton.In;
import princeton.StdDraw;
import java.awt.Color;
import java.util.ArrayList;

public class ImageConverter {

  private static ArrayList<String> textureNames = new ArrayList<>();
  private static ArrayList<Color> textureColors = new ArrayList<>();
  private static ArrayList<Integer> textureColorValues = new ArrayList<>();
  private static ArrayList<Picture> textures = new ArrayList<>();
  private static String[][] writtenPicture;

  public static void fetchTextureData() {
    In fileNames = new In("filenames.txt");
    while (fileNames.hasNextLine()) {
      String[] str = fileNames.readLine().split(",");
      textureNames.add(str[0]);
      int r = Integer.parseInt(str[1]), g = Integer.parseInt(str[2]), b = Integer.parseInt(str[3]);
      textureColors.add(new Color(r, g, b));
      textureColorValues.add(r + b + g);
      textures.add(new Picture("textures/" + str[0]));
    }
  }

  public static Picture convertPicture(Picture original, int totalWidth, int totalHeight) {
    int scaling_factor = ImageReducer.fitToLength(original, totalWidth, totalHeight);
    Picture reducedImage = ImageReducer.reduceImage(scaling_factor, original);

    int newWidth = reducedImage.width();
    int newHeight = reducedImage.height();
    System.out.printf("w:%d, h:%d\n", newWidth, newHeight);
    writtenPicture = new String[newWidth][newHeight];

    int[] resizedDimensions = resize(original.width(), original.height());
    System.out.printf("Canvas size: w:%d, h:%d\n", resizedDimensions[0], resizedDimensions[1]);
    StdDraw.setCanvasSize(resizedDimensions[0], resizedDimensions[1]);
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, newWidth);
    StdDraw.setYscale(0, newHeight);

    int count = 1;
    for (int row = 0; row < newHeight; row++) {
      for (int col = 0; col < newWidth; col++) {
        // System.out.printf("Row: %d, Col: %d", row, col);
        String bestBlock = findMatch(reducedImage.get(col, row));
        writtenPicture[col][row] = bestBlock;
        StdDraw.picture(col - 0.5, newHeight - row - 0.5, "textures/" + writtenPicture[col][row], 1, 1);
        System.out.printf("%.2f%% finished\r", (double) (count * 100) / (double) (newHeight * newWidth));
        count++;
      }
    }
    System.out.println();
    StdDraw.show();
    StdDraw.save("picture.png");
    return new Picture("picture.png");
  }

  public static int[] resize(int w, int h) {
    double ratio = 1;
    if (h == w && h > 800) {
      h = 800;
      w = 800;
    } else if (h > 800) {
      if (h > w) {
        ratio = (double) h / (double) w;
        h = (int) (800 * ratio);
        w = 800;
      } else {
        ratio = (double) w / (double) h;
        w = (int) (800 * ratio);
        h = 800;
      }
      System.out.println(ratio);
      System.out.printf("%d, %d\n", w, h);
    } else if (w > 1200) {
      ratio = (double) w / (double) h;
      w = 1200;
      h *= ratio;
    }
    return new int[] { w, h };
  }

  public static String findMatch(Color c) {
    int min = Integer.MAX_VALUE;
    String bestBlock = "";
    for (int i = 0; i < textureColors.size(); i++) {
      Color current = textureColors.get(i);
      int difference = Math.abs(c.getRed() - current.getRed()) +
          Math.abs(c.getGreen() - current.getGreen()) +
          Math.abs(c.getBlue() - current.getBlue());
      if (difference < min) {
        min = difference;
        bestBlock = textureNames.get(i);
      }
    }
    if (min == -1)
      return null;
    else
      return bestBlock;
  }

  public static boolean fallsInRange(int value, int lo, int hi) {
    return value <= hi && value >= lo;
  }

  public static void main(String[] args) {
    fetchTextureData();
    String path = args[0];
    System.out.println(path);
    Picture original = new Picture(path);
    System.out.println("Enter the dimensions of your picture in the format: widthxheight");
    System.out.println("Current dimensions: " + original.width() + "x" + original.height());
    String[] dimensions = StdIn.readString().split("x");
    int width = Integer.parseInt(dimensions[0]);
    int height = Integer.parseInt(dimensions[1]);
    int size = ImageReducer.fitToLength(original, width, height);
    Picture reducedImage = ImageReducer.reduceImage(size, path);
    reducedImage.show();
    convertPicture(original, width, height).show();
  }
}
