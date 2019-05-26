package com.endgame.vectordesigntool;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

class Bitmap{

    // Export method will need the filename, and user's desired dimension x and y
    static void export(File filename, int dimensionX, int dimensionY) {
        BufferedImage img = map( dimensionX, dimensionY );
        saveBMP( img, filename.toString() );
    }

    private static BufferedImage map( int sizeX, int sizeY ){
        final BufferedImage res = new BufferedImage( sizeX, sizeY, BufferedImage.TYPE_INT_RGB );
        for (int x = 0; x < sizeX; x++){
            for (int y = 0; y < sizeY; y++){
                res.setRGB(x, y, Color.WHITE.getRGB() );
            }
        }
        return res;
    }

    private static void saveBMP( final BufferedImage bi, final String path ){
        try {
            RenderedImage rendImage = bi;
            ImageIO.write(rendImage, "bmp", new File(path));
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }
}
