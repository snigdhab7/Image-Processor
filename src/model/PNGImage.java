package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The `PNGImage` class extends `AbstractImage`. It includes methods for loading PNG images and
 * saving images in the PNG format.
 */
public class PNGImage extends AbstractImage {

  public static int HEIGHT;
  public static int WIDTH;


  /**
   * Converts RGB image data represented as a three-dimensional array into a `BufferedImage`.
   *
   * @param rgbData The three-dimensional array representing the RGB image data.
   * @return A `BufferedImage` object containing the image data.
   */
  protected static BufferedImage convertRGBDataToBufferedImage(int[][][] rgbData) {
    int height = rgbData.length;
    int width = rgbData[0].length;

    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int r = rgbData[y][x][0];
        int g = rgbData[y][x][1];
        int b = rgbData[y][x][2];

        int rgb = (r << 16) | (g << 8) | b;

        bufferedImage.setRGB(x, y, rgb);
      }
    }

    return bufferedImage;
  }


  /**
   * Loads a PNG image from the specified file path and stores it in the image map and RGB data map.
   *
   * @param imagePath The file path of the PNG image to load.
   * @param imageName The name to associate with the loaded image.
   * @throws IOException If an I/O error occurs during the loading process.
   */
  @Override
  public void loadImage(String imagePath, String imageName) throws IOException {
    int[][][] imageRGBData;

    imageRGBData = convertPNGToRGB(imagePath);


    if (imageRGBData != null) {

      ImageContent image = new ImageContent(imageName, imageRGBData);
      image.setWidth(WIDTH);
      image.setHeight(HEIGHT);
      IMAGE_MAP.put(imageName, image);
      System.out.println("Loaded image: " + imageName);
    } else {
      System.out.println("Failed to load the image from: " + imagePath);
    }
  }


  /**
   * Converts a PNG image file to a 3D array representing RGB values.
   *
   * @param imagePath The path to the PNG image file.
   * @return A 3D array representing the RGB values of the image, or null if an error occurs.
   */
  public static int[][][] convertPNGToRGB(String imagePath) {
    try {
      File imageFile = new File(imagePath);
      if (!imageFile.exists()) {
        System.out.println("Image file not found: " + imagePath);
        return null;
      }

      BufferedImage bufferedImage = ImageIO.read(imageFile);
      if (bufferedImage == null) {
        System.out.println("Failed to read image from: " + imagePath);
        return null;
      }

      WIDTH = bufferedImage.getWidth();
      HEIGHT = bufferedImage.getHeight();

      int[][][] imageRGBData = new int[HEIGHT][WIDTH][3];

      for (int y = 0; y < HEIGHT; y++) {
        for (int x = 0; x < WIDTH; x++) {
          int rgb = bufferedImage.getRGB(x, y);
          imageRGBData[y][x][0] = (rgb >> 16) & 0xFF; // Red component
          imageRGBData[y][x][1] = (rgb >> 8) & 0xFF;  // Green component
          imageRGBData[y][x][2] = rgb & 0xFF;         // Blue component
        }
      }

      return imageRGBData;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error while converting PNG to RGB: " + imagePath);
      return null;
    }
  }

  /**
   * Saves an image as a PNG file to the specified file path.
   *
   * @param imagePath The file path where the PNG image should be saved.
   * @param imageName The name of the image to be saved.
   */
  @Override
  public void saveImage(String imagePath, String imageName) {
    // Retrieve ImageContent from imageMap
    ImageContent imageContent = IMAGE_MAP.get(imageName);

    if (imageContent != null) {
      int[][][] rgbData = imageContent.getRgbDataMap();
      double[][] pixels = imageContent.getPixels();
      BufferedImage bufferedImage;

      if (rgbData != null) {
        if (pixels != null) {
          bufferedImage = convertRGBAndPixelsDataToBufferedImage(rgbData, pixels);
        } else {
          bufferedImage = convertRGBDataToBufferedImage(rgbData);
        }

        //String format = imagePath.substring(imagePath.lastIndexOf('.') + 1);

        // Check if the format is "png" before saving as PNG
        File output = new File(imagePath);

        try {
          ImageIO.write(bufferedImage, "png", output);
          System.out.println("Image saved as " + imagePath + " in the png format");
        } catch (Exception e) {
          System.out.println("Error in saving File");
          e.printStackTrace(); // Print the stack trace for better error diagnostics
        }
      } else {
        System.out.println("RGB data is null for image: " + imageName);
      }
    } else {
      System.out.println("ImageContent not found for image: " + imageName);
    }
  }


  /**
   * Converts RGB data and corresponding pixel values to a BufferedImage.
   *
   * @param rgbData The 3D array representing the RGB values of an image.
   * @param pixels  The 2D array representing pixel values of the same image.
   * @return A BufferedImage generated from the provided RGB data and pixel values.
   */
  protected static BufferedImage convertRGBAndPixelsDataToBufferedImage(int[][][] rgbData,
                                                                        double[][] pixels) {
    int height = rgbData.length;
    int width = rgbData[0].length;

    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int r = rgbData[y][x][0];
        int g = rgbData[y][x][1];
        int b = rgbData[y][x][2];

        int grayValue = (int) (pixels[y][x] * 255); // Scale to 0-255 range

        int rgb = (r << 16) | (g << 8) | b;
        rgb = (rgb & 0xFF000000) | (grayValue << 16) | (grayValue << 8) | grayValue;

        bufferedImage.setRGB(x, y, rgb);
      }
    }

    return bufferedImage;
  }

}