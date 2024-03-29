package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


/**
 * A class representing images in the PPM format. This class extends the AbstractImage class,
 * providing functionality to load and save PPM images.
 */
public class PPMImage extends AbstractImage {

  /**
   * Loads a PPM image from the specified file and adds it to the image map.
   *
   * @param imagePath The file path of the PPM image to load.
   * @param imageName The name to associate with the loaded image.
   */
  @Override
  public void loadImage(String imagePath, String imageName) {
    int[][][] imageRGBData = readImageRGBData(imagePath);
    ImageContent image = new ImageContent(imageName, imageRGBData);
    IMAGE_MAP.put(imageName, image);
    System.out.println("Loaded image: " + imageName);
  }

  private String convertToPPMFormat(int[][][] imageRGBData) {
    StringBuilder ppmContent = new StringBuilder();
    int height = imageRGBData.length;
    int width = imageRGBData[0].length;

    ppmContent.append("P3\n");
    ppmContent.append(width + " " + height + "\n");
    ppmContent.append("255\n");

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        ppmContent.append(imageRGBData[i][j][0] + " "); // Red component
        ppmContent.append(imageRGBData[i][j][1] + " "); // Green component
        ppmContent.append(imageRGBData[i][j][2] + " "); // Blue component
      }
      ppmContent.append("\n");
    }

    return ppmContent.toString();
  }

  /**
   * Reads RGB data from a PPM file and returns a 3D array.
   *
   * @param filename The path to the PPM file to be read.
   * @return A 3D array representing the RGB values of the image.
   */
  public static int[][][] readImageRGBData(String filename) {
    Scanner sc = null;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      System.out.println("File " + filename + " not found!");
    }

    StringBuilder builder = new StringBuilder();
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }

    sc = new Scanner(builder.toString());

    String token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();

    int[][][] imageRGBData = new int[height][width][3];
    int maxValue = sc.nextInt(); // Read the maximum color value

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        imageRGBData[i][j][0] = sc.nextInt(); // Red component
        imageRGBData[i][j][1] = sc.nextInt(); // Green component
        imageRGBData[i][j][2] = sc.nextInt(); // Blue component
      }
    }
    return imageRGBData;
  }


  /**
   * Saves the specified PPM image content to a file with the given name and path.
   *
   * @param imagePath The file path where the PPM image should be saved.
   * @param imageName The name of the image to be saved.
   */
  @Override
  public void saveImage(String imagePath, String imageName) {
    ImageContent image = IMAGE_MAP.get(imageName);


    if (image != null) {
      String content = convertToPPMFormat(image.getRgbDataMap());

      // Create a file and write the image content to it
      File file = new File(imagePath);

      try {
        java.io.FileWriter fileWriter = new java.io.FileWriter(file);
        fileWriter.write(content);
        fileWriter.write(content);
        fileWriter.close();
        System.out.println("Image saved as " + imagePath + " in the ppm format");


      } catch (IOException e) {
        System.out.println("Error in saving File");
      }


    } else {
      System.out.println("Image not found: " + imageName);
    }
  }

}