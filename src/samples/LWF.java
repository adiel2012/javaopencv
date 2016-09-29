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
            Imgproc.line(mat_copy, new Point(puntos[tri[0] - 1]), new Point(puntos[tri[1] - 1]), new Scalar(0, 255, 0));
            Imgproc.line(mat_copy, new Point(puntos[tri[1] - 1]), new Point(puntos[tri[2] - 1]), new Scalar(0, 255, 0));
            Imgproc.line(mat_copy, new Point(puntos[tri[2] - 1]), new Point(puntos[tri[0] - 1]), new Scalar(0, 255, 0));
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

    double meanshape[][] = new double[][]{{1.256838e-002, 2.106873e-001},
    {1.747292e-002, 3.394221e-001},
    {3.578013e-002, 4.682388e-001},
    {6.520592e-002, 5.949315e-001},
    {1.155413e-001, 7.129952e-001},
    {1.915206e-001, 8.167731e-001},
    {2.843574e-001, 9.059237e-001},
    {3.905894e-001, 9.783953e-001},
    {5.149991e-001, 9.969693e-001},
    {6.368846e-001, 9.746899e-001},
    {7.421490e-001, 8.990841e-001},
    {8.339061e-001, 8.067633e-001},
    {9.080745e-001, 6.989241e-001},
    {9.545934e-001, 5.769892e-001},
    {9.762189e-001, 4.461308e-001},
    {9.857039e-001, 3.152591e-001},
    {9.851535e-001, 1.860417e-001},
    {1.022593e-001, 9.931757e-002},
    {1.644867e-001, 4.994748e-002},
    {2.446417e-001, 3.827954e-002},
    {3.271538e-001, 5.143408e-002},
    {4.052978e-001, 8.173952e-002},
    {5.740510e-001, 7.556236e-002},
    {6.551629e-001, 4.036641e-002},
    {7.364918e-001, 2.548583e-002},
    {8.167213e-001, 3.339841e-002},
    {8.796170e-001, 7.498893e-002},
    {4.946619e-001, 1.822989e-001},
    {4.967373e-001, 2.696140e-001},
    {4.986898e-001, 3.572696e-001},
    {5.006424e-001, 4.464839e-001},
    {4.090080e-001, 4.983330e-001},
    {4.544147e-001, 5.163332e-001},
    {5.022232e-001, 5.329508e-001},
    {5.510655e-001, 5.145033e-001},
    {5.947884e-001, 4.959074e-001},
    {2.016858e-001, 1.965780e-001},
    {2.496515e-001, 1.631910e-001},
    {3.132342e-001, 1.632745e-001},
    {3.635419e-001, 2.033908e-001},
    {3.093938e-001, 2.201657e-001},
    {2.485716e-001, 2.204947e-001},
    {6.281433e-001, 1.970816e-001},
    {6.756425e-001, 1.546035e-001},
    {7.381816e-001, 1.518766e-001},
    {7.874967e-001, 1.830104e-001},
    {7.457050e-001, 2.081456e-001},
    {6.865413e-001, 2.109641e-001},
    {3.218994e-001, 6.692314e-001},
    {3.881646e-001, 6.364731e-001},
    {4.559404e-001, 6.212181e-001},
    {5.024618e-001, 6.324677e-001},
    {5.548361e-001, 6.193335e-001},
    {6.259208e-001, 6.325519e-001},
    {6.927118e-001, 6.595991e-001},
    {6.318220e-001, 7.277653e-001},
    {5.639777e-001, 7.575285e-001},
    {5.076104e-001, 7.641836e-001},
    {4.561192e-001, 7.603441e-001},
    {3.885973e-001, 7.338657e-001},
    {3.505859e-001, 6.728396e-001},
    {4.566433e-001, 6.668332e-001},
    {5.039410e-001, 6.702354e-001},
    {5.570302e-001, 6.640377e-001},
    {6.637214e-001, 6.650139e-001},
    {5.586920e-001, 6.916318e-001},
    {5.047532e-001, 6.987404e-001},
    {4.565848e-001, 6.943512e-001}};
    int CantidadTriangulos = 111;

    double frontalancho = 89.13250393181819, frontalalto = 90.57584652272726;
    double lateralancho = 54.54846572727275, lateralalto = 80.93841643181817;
}
