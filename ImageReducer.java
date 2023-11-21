import princeton.Picture;
import princeton.StdIn;
import java.awt.Color;

public class ImageReducer {

  // reduces the image's resolution determined by the gridsize used around each
  // pixel
  // in the grid used for reduction
  public static Picture reduceImage(int gridSize, String path) {
    return reduceImage(gridSize, new Picture(path));
  }

  public static Picture reduceImage(int gridSize, Picture original) {
    int halfSize = gridSize / 2;
    int width = original.width();
    int height = original.height();
    Picture newPicture = new Picture(reduce(width, gridSize), reduce(height, gridSize));
    int i_ = 0;
    for (int i = halfSize; i < height; i += gridSize) {
      int j_ = 0;
      for (int j = halfSize; j < width; j += gridSize) {
        int r = 0, g = 0, b = 0, count = 0;

        // first pass gets the total of the red, green and blue values in the
        // gridSize x gridSize roster
        for (int k = i - halfSize; k <= i + halfSize; k++) {
          for (int l = j - halfSize; l <= j + halfSize; l++) {
            // ensures code only runs when k or l is within the picture range
            if (isWithinBorder(k, height) && isWithinBorder(l, width)) {
              Color c = original.get(l, k);
              r += c.getRed();
              g += c.getGreen();
              b += c.getBlue();
              count++;
            }
          }
        }

        // second pass is to fill the grids with the average color value of the
        // grid
        // for (int k = i-halfSize; k <= i+halfSize; k++) {
        // for (int l = j-halfSize; l <= j+halfSize; l++) {
        // if (isWithinBorder(k, height) && isWithinBorder(l, width)) {
        // newPicture.set(l, k, new Color(r/count, g/count, b/count));
        // }
        // }
        // }
        newPicture.set(j_, i_, new Color(r / count, g / count, b / count));
        j_++;
      }
      i_++;
    }
    return newPicture;
  }

  // returns the amount of times the gridSize can be used on the indicated num
  // e.g. with 3x3, 8 can be reduced to 3, indicating 2 3x3 grids and 1 1x3 grid
  // can be applied to the height of the image
  public static int reduce(int num, int gridSize) {
    int rem = num % gridSize;
    int div = num / gridSize;
    if (rem > 1)
      div++;
    return div;
  }

  public static boolean isWithinBorder(int i, int length) {
    return i >= 0 && i < length;
  }

  public static int fitToLength(Picture picture, int targetWidth, int targetHeight) {
    int width = picture.width();
    int width_1 = width;
    int height = picture.height();
    int height_1 = height;
    if ((width > height && targetWidth < targetHeight) || (width < height && targetWidth > targetHeight)) {
      throw new IllegalArgumentException("Error in dimension proportions: original picture is " + width + " x " + height
          + ", but target is " + targetWidth + " x " + targetHeight);
    }
    if ((double) (width / targetWidth) != (double) (height / targetHeight)) {
      throw new IllegalArgumentException(
          "Error: original dimensions are not of a proper scaling factor from target dimensions.");
    }
    int length = 1;
    while (width > targetWidth | height > targetHeight) {
      width = width_1;
      height = height_1;
      length++;
      width = reduce(width, length);
      height = reduce(height, length);
    }
    return length;
  }

  public static Picture enlargeImage(Picture original, int factor) {
    int width = original.width();
    int height = original.height();
    int newWidth = width * factor;
    int newHeight = height * factor;
    Picture enlargedImage = new Picture(newWidth, newHeight);
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Color c = original.get(col, row);
        for (int i = 0; i < factor; i++) {
          for (int j = 0; j < factor; j++) {
            enlargedImage.set((col * factor) + j, (row * factor) + i, c);
          }
        }
      }
    }
    return enlargedImage;
  }

  public static void show(Picture picture, int scale) {
    enlargeImage(picture, scale).show();
  }

  public static void main(String[] args) {
    // TEST FIT TO LENGTH
    System.out.println("Enter the dimensions of your picture in the format: widthxheight");
    String[] dimensions = StdIn.readString().split("x");
    int width = Integer.parseInt(dimensions[0]);
    int height = Integer.parseInt(dimensions[1]);
    String path = "test2.png";
    Picture original = new Picture(path);
    int size = fitToLength(original, width, height);
    System.out.println("Size determined: " + size);
    Picture newPicture = reduceImage(size, path);
    System.out.println("Image Size: " + newPicture.width() + " x " + newPicture.height() + "\t| Grid size: " + size);
    newPicture.show();
    enlargeImage(newPicture, 5).show();

    // TEST MAIN FUNCTION
    // String path = "symbol.jpg";
    // Picture original = new Picture(path);
    // int width = original.width();
    // int height = original.height();
    // original.show();
    // int size = 0;
    // System.out.println("Enter number for the size of the grid (eg 3 for 3x3), or
    // else \"stop\" to stop");
    // while (!StdIn.isEmpty()) {
    // String readString = StdIn.readString().toLowerCase();
    // if (readString.equals("stop")) {
    // break;
    // } else {
    // size += Integer.parseInt(readString);
    // System.out.println("Image Size: "+reduce(width, size)+" x "+reduce(height,
    // size)+"\t| Grid size: "+size);
    // reduceImage(size, path).show();
    // System.out.println("Enter new number to add to current gridSize, or else
    // \"stop\" to stop");
    // }
    // }
  }

}
