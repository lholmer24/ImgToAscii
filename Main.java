package com.company;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class Main {
    public static void main(String[] args) {

        BufferedImage image = null;

        try{
            File imageFile = new File(args[0]);
            image = ImageIO.read(imageFile);
        } catch (IOException e){}

        boolean invert = true;
        int outSize = 120;

        double imgWidth = image.getWidth();
        double imgHeight = image.getHeight();
        double outHeight = outSize;
        double outWidth = ((imgWidth / imgHeight) * outSize);
        double chunkWidth = (imgWidth / outWidth);
        double chunkHeight = (imgHeight / outHeight);

        String greyString = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,^\"`'. ";
        char[] greyArray = greyString.toCharArray();

        double factor = greyArray.length / 255.0;

        for(int oy = 0; oy < outHeight; oy += 3){
            for(int ox = 0; ox < outWidth; ox++){
                double greyVal = 0;
                for(int iy = 0; iy < chunkHeight; iy++){
                    for(int ix = 0; ix < chunkWidth; ix++){
                        Color c = new Color(image.getRGB(Math.min((int)(ox * chunkWidth) + ix,(int) imgWidth - 1),
                                                    Math.min((int)(oy * chunkHeight) + iy, (int) imgHeight - 1)));
                        if(invert){
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
                greyVal = greyVal / (3 * chunkHeight * chunkWidth);

                int v = (int) (greyVal * factor);
                if (v < 0){v = 0;}if (v > 69){v = 69;}

                System.out.print(greyArray[v]);
            }
            System.out.println();
        }
    }
}
