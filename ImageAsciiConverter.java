package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageAsciiConverter {
    int outSize = 100; //height of the ascii output text
    boolean invert = false; //invert brightness values in all conversions

    //this string contains the ascii characters recognized by the software from lightest to darkest
    String greyString = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,^\"`'. ";


    char[] greyArray = greyString.toCharArray(); //array version of string above
    double factor = greyArray.length / 255.0; //scale factor for a rgb value to an index
    double invFactor = 1 / factor; //reciprocal of previous factor
    int charHeight = 3; //ratio of character height to width used for converting ascii to image

    public void setSize(int s) {
        outSize = s;
    }

    public void setInvert(boolean i) {
        invert = i;
    }

    public void setCharHeight(int c) {
        charHeight = c;
    }


    public String convertToAscii(BufferedImage image) {
        StringBuilder outString = new StringBuilder();
        double imgWidth = image.getWidth(); //size of input image
        double imgHeight = image.getHeight();
        double outHeight = outSize; //user defined height
        double outWidth = ((imgWidth / imgHeight) * outSize); //appropriately scaled width
        double chunkWidth = (imgWidth / outWidth); //scale of how many pixels correspond to 1 character
        double chunkHeight = (imgHeight / outHeight);

        for (int oy = 0; oy < outHeight; oy += 3) { //loops through every character it will render
            for (int ox = 0; ox < outWidth; ox++) {
                double greyVal = 0;
                for (int iy = 0; iy < chunkHeight; iy++) { //loops through every pixel in the chunk for that character
                    for (int ix = 0; ix < chunkWidth; ix++) {
                        Color c = new Color(image.getRGB(Math.min((int) (ox * chunkWidth) + ix, (int) imgWidth - 1),
                                Math.min((int) (oy * chunkHeight) + iy, (int) imgHeight - 1))); //finds the color of the pixel (adjusted to not overflow image
                        if (invert) { //adds rgb values to value for that character, respecting invert
                            greyVal += (255 - c.getRed());
                            greyVal += (255 - c.getGreen());
                            greyVal += (255 - c.getBlue());
                        } else {
                            greyVal += c.getRed();
                            greyVal += c.getGreen();
                            greyVal += c.getBlue();
                        }
                    }
                }
                if (chunkHeight < 1 || chunkWidth < 1) { //does the division to average the brightness value for the character
                    greyVal = greyVal / 3;
                } else {
                    greyVal = greyVal / (3 * chunkHeight * chunkWidth);
                }

                int v = (int) (greyVal * factor); //scales the brightness with the factor
                if (v < 0) {
                    v = 0;
                }
                if (v > 69) {
                    v = 69;
                }

                outString.append(greyArray[v]); //adds the appropriate character to the array
            }
            outString.append("\n"); //adds line breaks at eol
        }

        return outString.toString();
    }

    public BufferedImage convertToImage(String in) {
        int width = 0;
        int height = 0;

        char[] inputArray = in.toCharArray();
        int gap = 0; //tally to check longest necessary padding
        for (char c : inputArray) {
            if (c == '\n') {
                height += charHeight; //counts newlines to determine height
                if (gap > width) {
                    width = gap; //counts the longest gap between newlines for width
                }
                gap = 0;
            } else {
                gap++;
            }
        }
        BufferedImage bufferedInput = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphicsInput = bufferedInput.createGraphics();

        int x = 0;
        int y = 0;
        int index = 0;
        if (!invert) { //fill a rect in the background so that no character renders as white
            Color col = new Color(72, 72, 72);
            graphicsInput.setColor(col);
            graphicsInput.fillRect(0, 0, width, height);
        }


        while (index < inputArray.length) { //iterate through every character in the input
            if (inputArray[index] == '\n') {
                y += charHeight; //jump lines on every newline
                x = 0;
            } else {
                int characterColorValue;
                if (invert) { //convert the index of the character in to the appropriate scaled brightness value
                    characterColorValue = (int) ((double) (69 - greyString.indexOf(inputArray[index])) + 1 * invFactor);
                } else {
                    characterColorValue = (int) ((double) (greyString.indexOf(inputArray[index])) + 1 * invFactor);
                }
                Color col = new Color(characterColorValue, characterColorValue, characterColorValue);
                graphicsInput.setColor(col);
                graphicsInput.drawRect(x, y, 1, charHeight); //draw a pixel of determined brightness
                x++;
            }
            index++;
        }

        return bufferedInput;
    }
}
