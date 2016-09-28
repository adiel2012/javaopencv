/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author LABORATORIO 10
 */
public class LWF {

    public static void main(String[] args) {
        System.out.println("Hello, OpenCV");
        // Load the native library.
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.load("D:\\opencv_instalaciones\\opencv\\build\\java\\x64\\opencv_java310.dll");

        String urlHelen = "D:\\borrar\\helen\\trainset";
        File carpeta = new File(urlHelen);
        File[] images = carpeta.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 3, name.length()).equals("jpg");
                //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        File carpetaalmacen = new File("D:\\generated");
        if (carpetaalmacen.exists() == false) {
            carpetaalmacen.mkdir();
        }
        
        int[][] delaunay_triangles = new int[200][3];
        for (File image : images) {
            System.out.println(image.getName());
            String puntos_file = image.getName().substring(0, image.getName().length() - 3) + "pts";
            System.out.println(puntos_file);
            System.out.println("------------------------");
            double[][] puntos = leerpuntos(new File(urlHelen + "\\" + puntos_file));
            Mat mat = Imgcodecs.imread(image.getAbsolutePath());
            save_meshed_images(puntos, carpetaalmacen, image, mat, delaunay_triangles);

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

    private static double[][] leerpuntos(File file) {
        double[][] res = new double[68][2];
        try {
            Scanner scan = new Scanner(file);
            String trash = scan.nextLine();
            trash = scan.nextLine();
            trash = scan.nextLine();

            for (int i = 0; i < 68; i++) {
                String[] numbers = scan.nextLine().split(" ");
                res[i][0] = Double.parseDouble(numbers[0]);
                res[i][1] = Double.parseDouble(numbers[1]);
            }

            scan.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LWF.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    private static void save_meshed_images(double[][] puntos, File carpetaalmacen, File image, Mat mat, int[][] delaunay_triangles) {
        Mat mat_copy = mat.clone();
        int radii = 1000;
        for (double[] punto : puntos) {
            Imgproc.ellipse(mat_copy, new Point(punto),new Size(radii, radii),0,0,0,new Scalar(0, 255, 0));
        }
        Imgcodecs.imwrite(carpetaalmacen.getAbsolutePath() + "\\" + image.getName(), mat_copy);
    }
}
