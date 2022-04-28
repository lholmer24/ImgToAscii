package com.company;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {
    public static void main(String[] args) throws IOException {

        BufferedImage image = null; //initialize image


        try {
            File imageFile = new File("C:\\Users\\pogo3\\Downloads\\Images\\fractal.jpg");
            image = ImageIO.read(imageFile); //import image from cmd argument
        } catch (IOException e) {
            System.out.println("Image not found");
        }


        ImageAsciiConverter c = new ImageAsciiConverter();
        c.setInvert(true);
        c.setSize(500);
        c.setCharHeight(2);
        //String myImg = c.convertToAscii(image);
        //System.out.println(myImg);
        String myImg = Files.readString(Paths.get("C:\\Users\\pogo3\\Downloads\\Images\\statue.txt"));
        File outputFile = new File("saved.png");
        ImageIO.write(c.convertToImage(myImg), "png", outputFile);


    }
}
