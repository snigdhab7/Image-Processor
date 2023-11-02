package controller;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


import org.junit.Before;
import org.junit.Test;


import javax.imageio.ImageIO;


import model.JPGImage;
import model.Model;
import view.IView;
import view.JFrameView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ControllerTest {

  private static JPGImage pngJpgImage;

  private static String imageName = "outputJPG";
  private static String imagePath = "outputJPG.jpg";

  static int[][][] rgbMatrix = new int[2][2][3];

  private Model model;
  private IView view;
  private Controller controller;

  int[][][] originalMatrix = {
          {{255, 0, 0}, {0, 255, 0}},
          {{0, 0, 255}, {255, 255, 255}}
  };


  @Before
  public void setUp() {
    pngJpgImage = new JPGImage();
    createAndSaveJPG(originalMatrix, imageName, imagePath);
    model = new Model();
    view = new JFrameView(null);
    controller = new Controller(model, view);
  }

  private static void createAndSaveJPG(int[][][] matrix, String fileName, String filePath) {

    int width = matrix[0].length;
    int height = matrix.length;

    // Create a BufferedImage with the specified width and height
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int r = matrix[y][x][0];
        int g = matrix[y][x][1];
        int b = matrix[y][x][2];

        // Create an RGB color from the values
        int rgb = (r << 16) | (g << 8) | b;

        // Set the pixel color in the BufferedImage
        image.setRGB(x, y, rgb);
      }
    }

    try {
      // Save the BufferedImage as a PNG image at the specified file path
      File output = new File(filePath);
      ImageIO.write(image, "jpg", output);
      System.out.println("Image saved as " + filePath);
      rgbMatrix = new int[][][]{
              {{66, 62, 63}, {167, 163, 164}},
              {{53, 49, 50}, {249, 245, 246}}
      };
      pngJpgImage.loadImage(filePath, fileName);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Failed to save the image as " + filePath);
    }
  }

  @Test
  public void testLoad() throws IOException {
    controller.parseAndExecute("load "+ imagePath + " "+ imageName);

    // Check if the image was loaded successfully
    assertTrue(pngJpgImage.getImageMap().containsKey(imageName));
    // Get the RGB data
    int[][][] rgbData = pngJpgImage.getRgbDataMap().get(imageName);

    // Check if the dimensions match
    assertEquals(rgbMatrix.length, rgbData.length);
    assertEquals(rgbMatrix[0].length, rgbData[0].length);

    // Print the RGB data
    for (int y = 0; y < rgbData.length; y++) {
      for (int x = 0; x < rgbData[y].length; x++) {
        int r = rgbData[y][x][0];
        int g = rgbData[y][x][1];
        int b = rgbData[y][x][2];
        System.out.println("RGB at (" + x + ", " + y + "): R=" + r + " G=" + g + " B=" + b);
        assertEquals(rgbMatrix[y][x][0], r);
        assertEquals(rgbMatrix[y][x][1], g);
        assertEquals(rgbMatrix[y][x][2], b);
      }
    }
  }


  @Test
  public void testVerticalFlipImage() throws IOException {
    controller.parseAndExecute("load "+ imagePath + " "+ imageName);
    controller.parseAndExecute("vertical-flip "+ imageName + " vertical-flip-img");


    // Get the flipped image data
    int[][][] flippedImageData = pngJpgImage.getRgbDataMap().get("vertical-flip-img");

    // Check if the flipped image matches the expected result
    int[][][] expectedFlippedImageData = new int[2][2][3];
    expectedFlippedImageData[0][0] = new int[]{53, 49, 50};
    expectedFlippedImageData[0][1] = new int[]{249, 245, 246};
    expectedFlippedImageData[1][0] = new int[]{66, 62, 63};
    expectedFlippedImageData[1][1] = new int[]{167, 163, 164};

    for (int y = 0; y < expectedFlippedImageData.length; y++) {
      for (int x = 0; x < expectedFlippedImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {

          assertEquals(expectedFlippedImageData[y][x][c], flippedImageData[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testHorizontalFlipImage() throws IOException {

    controller.parseAndExecute("load "+ imagePath + " "+ imageName);
    controller.parseAndExecute("horizontal-flip "+ imageName + " horizontal-flip-img");

    // Perform a horizontal flip


    // Get the flipped image data
    int[][][] flippedImageData = pngJpgImage.getRgbDataMap().get("horizontal-flip-img");

    // Check if the flipped image matches the expected result
    int[][][] expectedFlippedImageData = new int[2][2][3];
    expectedFlippedImageData[0][0] = new int[]{167, 163, 164};
    expectedFlippedImageData[0][1] = new int[]{66, 62, 63};
    expectedFlippedImageData[1][0] = new int[]{249, 245, 246};
    expectedFlippedImageData[1][1] = new int[]{53, 49, 50};


    for (int y = 0; y < expectedFlippedImageData.length; y++) {
      for (int x = 0; x < expectedFlippedImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.println(flippedImageData[y][x][c]);
          assertEquals(expectedFlippedImageData[y][x][c], flippedImageData[y][x][c]);

        }
      }
    }
  }

  @Test
  public void testSharpenImage() throws IOException {

    controller.parseAndExecute("load "+ imagePath + " "+ imageName);
    controller.parseAndExecute("sharpen "+ imageName + " sharp-img");
    // Perform sharpening on the image

    // Get the sharpened image data
    int[][][] sharpenedImageData = pngJpgImage.getRgbDataMap().get("sharp-img");

    // Define the expected RGB values for the sharpened image
    int[][][] expectedSharpenedImageData = {
            {{0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}}
    };

    for (int y = 0; y < sharpenedImageData.length; y++) {
      for (int x = 0; x < sharpenedImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.print(sharpenedImageData[y][x][c]+ " ");
          assertEquals(expectedSharpenedImageData[y][x][c], sharpenedImageData[y][x][c]);
        }
      }
      System.out.println(" ");
    }
  }

  @Test
  public void testBlurImage() throws IOException {

    controller.parseAndExecute("load "+ imagePath + " "+ imageName);
    controller.parseAndExecute("blur "+ imageName + " blurred-img");

    // Perform blurring on the image

    // Get the blurred image data
    int[][][] blurredImageData = pngJpgImage.getRgbDataMap().get("blurred-img");

    // Check if the blurred image matches the expected result
    int[][][] expectedBlurredImageData = {
            {{0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}}
    };

    for (int y = 0; y < blurredImageData.length; y++) {
      for (int x = 0; x < blurredImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.print(blurredImageData[y][x][c] + " ");
          assertEquals(expectedBlurredImageData[y][x][c], blurredImageData[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testSepiaImage() throws IOException {


    controller.parseAndExecute("load "+ imagePath + " "+ imageName);
    controller.parseAndExecute("sepia "+ imageName + " sepia-img");
    pngJpgImage.sepiaImage(imageName, "sepia-img");

    // Get the sepia image data
    int[][][] sepiaImageData = pngJpgImage.getRgbDataMap().get("sepia-img");

    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedSepiaImageData = new int[2][2][3];

    // Define the expected RGB values for the sepia image
    expectedSepiaImageData[0][0] = new int[]{85, 76, 59};
    expectedSepiaImageData[0][1] = new int[]{221, 197, 153};
    expectedSepiaImageData[1][0] = new int[]{67, 60, 47};
    expectedSepiaImageData[1][1] = new int[]{255, 255, 230};

    // Compare the actual sepia-toned image data with the expected result
    for (int y = 0; y < expectedSepiaImageData.length; y++) {
      for (int x = 0; x < expectedSepiaImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {

          assertEquals(expectedSepiaImageData[y][x][c], sepiaImageData[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testExtractRedComponent() throws IOException {

    controller.parseAndExecute("load "+ imagePath + " "+ imageName);
    controller.parseAndExecute("red-component "+ imageName + " red-component-img");
    // Define the expected result after extracting the red component
    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedRedComponent = {
            {{66, 0, 0}, {167, 0, 0}},
            {{53, 0, 0}, {249, 0, 0}}
    };


    // Perform the extraction of the "red" component


    // Get the extracted red component data
    int[][][] extractedRedComponent = pngJpgImage.getRgbDataMap().get("red-component-img");

    // Check if the extracted red component matches the expected result
    for (int y = 0; y < expectedRedComponent.length; y++) {
      for (int x = 0; x < expectedRedComponent[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.println(extractedRedComponent[y][x][c]);
          assertEquals(expectedRedComponent[y][x][c], extractedRedComponent[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testExtractGreenComponent() throws IOException {

    controller.parseAndExecute("load "+ imagePath + " "+ imageName);
    controller.parseAndExecute("green-component "+ imageName + " green-component-img");
    // Define the expected result after extracting the red component
    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedGreenComponent = {
            {{0, 62, 0}, {0, 163, 0}},
            {{0, 49, 0}, {0, 245, 0}}
    };


    // Perform the extraction of the "red" component
    pngJpgImage.extractComponent(imageName, "green-component-img", "green");

    // Get the extracted red component data
    int[][][] extractedGreenComponent = pngJpgImage.getRgbDataMap().get("green-component-img");

    // Check if the extracted red component matches the expected result
    for (int y = 0; y < expectedGreenComponent.length; y++) {
      for (int x = 0; x < expectedGreenComponent[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          assertEquals(expectedGreenComponent[y][x][c], extractedGreenComponent[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testExtractBlueComponent() throws IOException {

    controller.parseAndExecute("load "+ imagePath + " "+ imageName);
    controller.parseAndExecute("blue-component "+ imageName + " blue-component-img");
    // Define the expected result after extracting the red component
    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedBlueComponent = {
            {{0, 0, 63}, {0, 0, 164}},
            {{0, 0, 50}, {0, 0, 246}}
    };


    // Perform the extraction of the "red" component
    pngJpgImage.extractComponent(imageName, "blue-component-img", "blue");

    // Get the extracted red component data
    int[][][] extractedBlueComponent = pngJpgImage.getRgbDataMap().get("blue-component-img");

    // Check if the extracted red component matches the expected result
    for (int y = 0; y < expectedBlueComponent.length; y++) {
      for (int x = 0; x < expectedBlueComponent[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          assertEquals(expectedBlueComponent[y][x][c], extractedBlueComponent[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testRGBSplit() throws IOException {

    controller.parseAndExecute("load "+ imagePath + " "+ imageName);
    controller.parseAndExecute("rgb-split "+ imageName + " red-component-img"
            + " green-component-img" + " blue-component-img");
    // Perform RGB splitting on the image

    // Get the split RGB components
    int[][][] extractedRedComponent = pngJpgImage.getRgbDataMap().get("red-component-img");
    int[][][] extractedGreenComponent = pngJpgImage.getRgbDataMap().get("green-component-img");
    int[][][] extractedBlueComponent = pngJpgImage.getRgbDataMap().get("blue-component-img");

    // Check if the split components are not null
    assertNotNull(extractedRedComponent);
    assertNotNull(extractedGreenComponent);
    assertNotNull(extractedBlueComponent);

    // Check if the dimensions match the original image
    assertEquals(rgbMatrix.length, extractedRedComponent.length);
    assertEquals(rgbMatrix[0].length, extractedRedComponent[0].length);

    assertEquals(rgbMatrix.length, extractedGreenComponent.length);
    assertEquals(rgbMatrix[0].length, extractedGreenComponent[0].length);

    assertEquals(rgbMatrix.length, extractedBlueComponent.length);
    assertEquals(rgbMatrix[0].length, extractedBlueComponent[0].length);

    int[][][] expectedRedComponent = {
            {{66, 0, 0}, {167, 0, 0}},
            {{53, 0, 0}, {249, 0, 0}}
    };

    int[][][] expectedGreenComponent = {
            {{0, 62, 0}, {0, 163, 0}},
            {{0, 49, 0}, {0, 245, 0}}
    };

    int[][][] expectedBlueComponent = {
            {{0, 0, 63}, {0, 0, 164}},
            {{0, 0, 50}, {0, 0, 246}}
    };

    // Compare the extracted components with the original image
    for (int y = 0; y < rgbMatrix.length; y++) {
      for (int x = 0; x < rgbMatrix[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          assertEquals(expectedRedComponent[y][x][c], extractedRedComponent[y][x][c]);
          assertEquals(expectedGreenComponent[y][x][c], extractedGreenComponent[y][x][c]);
          assertEquals(expectedBlueComponent[y][x][c], extractedBlueComponent[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testRGBCombine() throws IOException {

    controller.parseAndExecute("load "+ imagePath + " "+ imageName);
    controller.parseAndExecute("rgb-split "+ imageName + " red-component-img"
            + " green-component-img" + " blue-component-img");
    controller.parseAndExecute("rgb-combine " + "combined-img" + " red-component-img"
            + " green-component-img" + " blue-component-img");

    // Get the combined RGB data
    int[][][] combinedRGBData = pngJpgImage.getRgbDataMap().get("combined-img");

    // Check if the combined RGB data is not null
    assertNotNull(combinedRGBData);

    // Check if the dimensions match the original image
    assertEquals(rgbMatrix.length, combinedRGBData.length);
    assertEquals(rgbMatrix[0].length, combinedRGBData[0].length);

    // Compare the combined RGB data with the original image
    for (int y = 0; y < rgbMatrix.length; y++) {
      for (int x = 0; x < rgbMatrix[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          assertEquals(rgbMatrix[y][x][c], combinedRGBData[y][x][c]);
        }
      }
    }
  }

}