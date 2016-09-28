/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Rect;

/**
 *
 * @author LABORATORIO 10
 */
public class LWF {

    public static void main(String[] args) {
        System.out.println("Hello, OpenCV");
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String urlHelen = "C:\\Users\\LABORATORIO 5\\Downloads\\helen\\trainset";
        File carpeta = new File(urlHelen);
        File[] images = carpeta.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 3, name.length()).equals("jpg");
                //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        for (File image : images) {
            System.out.println(image.getName());
            String puntos = image.getName().substring(0, image.getName().length() - 3) + "pts";
            System.out.println(puntos);
            System.out.println("------------------------");
//            Rect[] rectangles = detect_faces(image);
//            for (Rect rectangle : rectangles) {
//
//            }



        }

        System.out.println(images.length);

    }

    public static BufferedImage toBufferedImage(File file) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
        }

        return img;
    }

    private static Rect[] detect_faces(File image) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
