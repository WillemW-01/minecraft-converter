import princeton.Picture;
import princeton.In;
import princeton.StdIn;
import princeton.StdDraw;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.Color;
import java.util.List;

public class ImageConverter {

  private static List<Texture> textures;
  // private static ArrayList<Integer> textureColorValues = new ArrayList<>();
  private static String[][] writtenPicture;

  /**
   * This function reads in all the texture data from the json file and saves
   * them as an array of Texture objects.
   * 
   */
  public static void fetchTextureData() {
    String inputFile = new In("texture_colors.json").readAll();
    inputFile = inputFile.replace("\n", "");
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      textures = objectMapper.readValue(inputFile, new TypeReference<List<Texture>>() {
      });
    } catch (Exception e) {
      System.out.println("look at this: " + e.toString());
      System.exit(1);
    }
  }

  /**
   * This function converts a pixture's pixels to Minecraft blocks. It first
   * rescales the picture to the new dimensions set by the user, and then
   * after that it will replace each pixel with the most appropriate
   * Minecraft block.
   * 
   * @param original
   * @param totalWidth
   * @param totalHeight
   * @return
   */
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
    StdDraw.save("output-picture.png");
    return new Picture("output-picture.png");
  }

  /**
   * This function determines what size the drawing canvas should be for printing.
   * 
   * @param w
   * @param h
   * @return
   */
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

  /**
   * This function finds the Minecraft block that most closely resembles the
   * current pixel color
   * 
   * @param c
   * @return
   */
  public static String findMatch(Color c) {
    int min = Integer.MAX_VALUE;
    String bestBlock = "";
    for (int i = 0; i < textures.size(); i++) {
      int distance = textures.get(i).getDistance(c, min);
      if (distance < min) {
        min = distance;
        bestBlock = textures.get(i).getName();
      }
    }
    if (min == Integer.MAX_VALUE) {
      return null;
    } else {
      return bestBlock;
    }
  }

  public static void main(String[] args) {
    // load textures in memory
    fetchTextureData();

    String path = args[0];
    Picture original = new Picture(path);

    // get target dimensions
    System.out.println("Enter the dimensions of your picture in the format: <width>x<height>");
    System.out.printf("Current dimensions: %dx%d\n", original.width(), original.height());
    String[] dimensions = StdIn.readString().split("x");

    int width = Integer.parseInt(dimensions[0]);
    int height = Integer.parseInt(dimensions[1]);

    // get the new target size based on the image
    int size = ImageReducer.fitToLength(original, width, height);

    Picture reducedImage = ImageReducer.reduceImage(size, path);
    reducedImage.show();

    convertPicture(original, width, height).show();
  }
}
