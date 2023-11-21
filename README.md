# Minecraft Converter

Minecraft is one of the most popular video games ever produced, and has interested
me since it was released.

This program takes in a filepath to an image to convert (png, jpg) and converts
each pixel to the closest-matching Minecraft block. This allows the image to be
built in-game.

This program was written as a side-project in Year II of bachelor studies, and
requires a Java enviroment (v8+) to run.

## Execution

**Compile**:

`javac *.java`

**Run**:

`java ImageConverter <input_image_path>`

## Notes

You must provide a scaling to the image, and will be prompted to do this in
the terminal.

This program uses the Princeton Standard library used in their IntroCS course.
This library was used during bachelor studies to illustrate the use of proper
API development, OOP practices, as well as encapsulating simple graphical actions.

The program works due to "texture baking", where each minecraft block's "average"
texture color was calculated and saved. All these images are contained `texturemaps/`.
Then, for each pixel in the scaled original image, the program chooses the "baked"
texture that is the closest distance to the target pixel. This is then saved in
the output image.
