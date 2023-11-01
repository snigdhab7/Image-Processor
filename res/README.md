# Assignment4_MVC

1. Model.ImageContent:
   The `Model.ImageContent` class is a simple data class used to store the name and content of images.
2. Model.AbstractImage:
   Manipulation operations for images are based on `Model.AbstractImage` class, which serves as an
   abstract foundation. For image operations, a set of abstract methods is defined which are
   for the three types of images(i.e. PNG, JPG, and PPM). there are two kernels defined as well
   which are used for image operations.
3. PNGJPGImage:
   Extending the abstract `Model.AbstractImage` class is the `PNGJPGImage` class.
   The PNG image formats utilize the image operations specified in the `Model.ImageOperations` interface
   that are implemented in the abstract class.
   Images in PNG format can be loaded, converted from RGB data, and saved with this class.
4. Model.PPMImage:
   The `Model.PPMImage` class, which makes use of the abstract `Model.AbstractImage` class, has been formulated.
   The PPM image format is utilized with the `Model.ImageOperations` interface to execute image
   operations.
   In PPM format, images can be loaded, RGB data converted, and images saved.
5. Model.JPGImage:
   Extending the `PNGJPGImage` class is the `Model.JPGImage` class.
   To capture photos in the JPG file format, the `saveImage` function is overridden and the rest
   of the functions are the same as the PNG class.
6. Model.Model-View-Controller.Controller:

   Controller.Controller (Controller.Controller): The `Controller.Controller` class is responsible for controlling the interaction
   between the model and view.
   It implements the ActionListener interface to handle user actions in the view.
   It interacts with the model and the view to update and display data based on user input.

   IModel (Interface): The `IModel` interface defines methods that the `model` must implement.

   View.IView (Interface): The `View.IView` interface defines methods that the `view` must implement.
7. `MVCExampleBasicMVC`:
   This class serves as the entry point of the application.
   It initializes the model, view, and controller and sets them up to work together.

The Model.Model class is responsible for interpreting the script file and executing the commands in it.
Model.Model command processing can be executed by following a specific protocol.
Using the executeScriptFromFile approach, the Model.Model class of the model is responsible for
administering and performing commands in a sequential order.
Using image manipulation techniques, the program carries out instructions found in a script
file that it interprets.

Implementing the Model.ImageOperations interface are three classes: Model.PPMImage, Model.JPGImage, and PNGJPGImage,
each with their unique image manipulation abilities.

**Overall Flow**:

The main method initializes the model, view, and controller.
The user interacts with the view by entering either the file path or clicking the button
that redirects them to the command prompt.
The controller responds to user actions and communicates with the model.
The model processes image commands and saves the resulting images in the specified file.

**Citations**:

test-image: "https://w7.pngwing.com/pngs/306/257/png-transparent-spongebob-squarepants-spongebob-heroes-cartoon-vehicle.png" 
(png image)