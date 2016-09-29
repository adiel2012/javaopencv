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
            Imgproc.ellipse(mat_copy, new Point(punto), new Size(radii, radii), 0, 0, 0, new Scalar(0, 255, 0));
//            Imgproc.line(mat_copy, null, null, null);
        }
        for (int[] tri : faceTemplateTriangles) {
            Imgproc.line(mat_copy, new Point(puntos[tri[0]-1]), new Point(puntos[tri[1]-1]), new Scalar(0, 255, 0));
            Imgproc.line(mat_copy, new Point(puntos[tri[1]-1]), new Point(puntos[tri[2]-1]), new Scalar(0, 255, 0));
            Imgproc.line(mat_copy, new Point(puntos[tri[2]-1]), new Point(puntos[tri[0]-1]), new Scalar(0, 255, 0));
        }
        Imgcodecs.imwrite(carpetaalmacen.getAbsolutePath() + "\\" + image.getName(), mat_copy);
    }

    public static int faceTemplateTriangles[][] = new int[][]{
        {54, 36, 53},
        {49, 32, 4},
        {34, 31, 33},
        {56, 57, 11},
        {53, 35, 34},
        {31, 35, 36},
        {31, 36, 30},
        {36, 48, 30},
        {9, 58, 59},
        {46, 16, 17},
        {49, 4, 5},
        {43, 23, 28},
        {3, 4, 32},
        {42, 32, 41},
        {19, 38, 20},
        {37, 1, 2},
        {37, 18, 1},
        {37, 19, 18},
        {37, 38, 19},
        {23, 21, 22},
        {23, 24, 21},
        {45, 46, 26},
        {28, 23, 22},
        {29, 43, 28},
        {24, 20, 21},
        {43, 44, 23},
        {25, 20, 24},
        {23, 44, 24},
        {24, 45, 25},
        {42, 37, 2},
        {36, 14, 15},
        {42, 38, 37},
        {9, 10, 57},
        {40, 41, 30},
        {51, 34, 33},
        {41, 39, 38},
        {60, 49, 6},
        {6, 49, 5},
        {31, 30, 32},
        {52, 53, 34},
        {31, 32, 33},
        {32, 50, 33},
        {54, 55, 36},
        {34, 35, 31},
        {34, 51, 52},
        {16, 46, 47},
        {15, 16, 47},
        {3, 42, 2},
        {40, 28, 22},
        {21, 38, 39},
        {21, 20, 38},
        {22, 39, 40},
        {22, 21, 39},
        {29, 40, 30},
        {29, 28, 40},
        {30, 41, 32},
        {40, 39, 41},
        {32, 42, 3},
        {41, 38, 42},
        {30, 43, 29},
        {45, 26, 25},
        {24, 44, 45},
        {47, 36, 15},
        {43, 48, 44},
        {46, 27, 26},
        {27, 46, 17},
        {45, 44, 48},
        {45, 47, 46},
        {48, 36, 47},
        {45, 48, 47},
        {43, 30, 48},
        {32, 49, 50},
        {6, 7, 60},
        {10, 11, 57},
        {51, 33, 50},
        {61, 60, 50},
        {56, 12, 55},
        {50, 60, 68},
        {53, 36, 35},
        {64, 66, 54},
        {55, 13, 14},
        {52, 63, 64},
        {55, 14, 36},
        {65, 55, 54},
        {12, 13, 55},
        {12, 56, 11},
        {54, 53, 64},
        {67, 59, 58},
        {58, 9, 57},
        {8, 9, 59},
        {63, 52, 62},
        {8, 59, 7},
        {68, 67, 63},
        {59, 60, 7},
        {59, 68, 60},
        {49, 61, 50},
        {49, 60, 61},
        {51, 62, 52},
        {51, 50, 62},
        {57, 66, 67},
        {64, 53, 52},
        {66, 64, 63},
        {66, 56, 54},
        {56, 65, 54},
        {56, 55, 65},
        {67, 66, 63},
        {57, 56, 66},
        {57, 67, 58},
        {63, 62, 68},
        {50, 68, 62},
        {59, 67, 68}};
}
