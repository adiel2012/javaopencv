/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.ejml.simple.SimpleMatrix;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_8UC3;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
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
        System.load("D:\\opencv\\opencv\\build\\java\\x64\\opencv_java300.dll");

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
            //save_meshed_images(puntos, carpetaalmacen, image, mat, delaunay_triangles);
            //save_mesh_images(puntos, carpetaalmacen, image, mat, delaunay_triangles);
            save_aligned_images(puntos, carpetaalmacen, image, mat, delaunay_triangles);

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

    private static void save_mesh_images(double[][] puntos, File carpetaalmacen, File image, Mat mat, int[][] delaunay_triangles) {
        Mat lienzo = new Mat(300, 300, CV_8UC3, new Scalar(0, 0, 0));
        Mat lienzo2 = new Mat(300, 300, CV_8UC3, new Scalar(0, 0, 0));
        double escala = 128;
        for (int[] tri : faceTemplateTriangles) {
            Imgproc.line(lienzo, new Point(escala * Shape3D[tri[0] - 1][0], escala * Shape3D[tri[0] - 1][1]), new Point(escala * Shape3D[tri[1] - 1][0], escala * Shape3D[tri[1] - 1][1]), new Scalar(0, 255, 0));
            Imgproc.line(lienzo, new Point(escala * Shape3D[tri[1] - 1][0], escala * Shape3D[tri[1] - 1][1]), new Point(escala * Shape3D[tri[2] - 1][0], escala * Shape3D[tri[2] - 1][1]), new Scalar(0, 255, 0));
            Imgproc.line(lienzo, new Point(escala * Shape3D[tri[2] - 1][0], escala * Shape3D[tri[2] - 1][1]), new Point(escala * Shape3D[tri[0] - 1][0], escala * Shape3D[tri[0] - 1][1]), new Scalar(0, 255, 0));

            Imgproc.line(lienzo2, new Point(escala * Shape3D[tri[0] - 1][2], escala * Shape3D[tri[0] - 1][1]), new Point(escala * Shape3D[tri[1] - 1][2], escala * Shape3D[tri[1] - 1][1]), new Scalar(0, 255, 0));
            Imgproc.line(lienzo2, new Point(escala * Shape3D[tri[1] - 1][2], escala * Shape3D[tri[1] - 1][1]), new Point(escala * Shape3D[tri[2] - 1][2], escala * Shape3D[tri[2] - 1][1]), new Scalar(0, 255, 0));
            Imgproc.line(lienzo2, new Point(escala * Shape3D[tri[2] - 1][2], escala * Shape3D[tri[2] - 1][1]), new Point(escala * Shape3D[tri[0] - 1][2], escala * Shape3D[tri[0] - 1][1]), new Scalar(0, 255, 0));

        }
        Imgcodecs.imwrite(carpetaalmacen.getAbsolutePath() + "\\frontal_" + image.getName(), lienzo);
        Imgcodecs.imwrite(carpetaalmacen.getAbsolutePath() + "\\lateral_" + image.getName(), lienzo2);
    }

    private static void save_aligned_images(double[][] puntos, File carpetaalmacen, File image, Mat mat, int[][] delaunay_triangles) {

        
        SimpleMatrix P = new SimpleMatrix(puntos);
        SimpleMatrix M = new SimpleMatrix(Shape3D);
        SimpleMatrix ones = (new SimpleMatrix(68, 1)).plus(1);
        M = M.combine(0, 3, ones);
        SimpleMatrix C = M.solve(P);

        SimpleMatrix A = (P.minus(M.mult(C))).transpose();
        SimpleMatrix B = C.transpose();

        SimpleMatrix Error = B.pseudoInverse().mult(A).transpose();

        SimpleMatrix Cara = M.plus(Error);
//         double v = Cara.get(0, 0);

        int dim = 3000;
        Mat lienzo = new Mat(dim, dim, CV_8UC3, new Scalar(0, 0, 0));
        //Mat lienzo2 = new Mat(300, 300, CV_8UC3, new Scalar(0, 0, 0));
        double escala = 1280;
        int gap = (int) ((dim-escala)/2);
        
        //Cara = M;
        for (int[] tri : faceTemplateTriangles) {
//            Imgproc.line(lienzo, new Point(gap + escala * Shape3D[tri[0] - 1][0], gap + escala * Shape3D[tri[0] - 1][1]), new Point(gap + escala * Shape3D[tri[1] - 1][0], gap + escala * Shape3D[tri[1] - 1][1]), new Scalar(0, 255, 0));
//            Imgproc.line(lienzo, new Point(gap + escala * Shape3D[tri[1] - 1][0], gap + escala * Shape3D[tri[1] - 1][1]), new Point(gap + escala * Shape3D[tri[2] - 1][0], gap + escala * Shape3D[tri[2] - 1][1]), new Scalar(0, 255, 0));
//            Imgproc.line(lienzo, new Point(gap + escala * Shape3D[tri[2] - 1][0], gap + escala * Shape3D[tri[2] - 1][1]), new Point(gap + escala * Shape3D[tri[0] - 1][0], gap + escala * Shape3D[tri[0] - 1][1]), new Scalar(0, 255, 0));
//
//            Imgproc.line(lienzo, new Point(gap + escala * Cara.get(tri[0] - 1, 0), gap + escala * Cara.get(tri[0] - 1, 1)), new Point(gap + escala * Cara.get(tri[1] - 1, 0), gap + escala * Cara.get(tri[1] - 1, 1)), new Scalar(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
//            Imgproc.line(lienzo, new Point(gap + escala * Cara.get(tri[0] - 1, 0), gap + escala * Cara.get(tri[0] - 1, 1)), new Point(gap + escala * Cara.get(tri[1] - 1, 0), gap + escala * Cara.get(tri[1] - 1, 1)), new Scalar(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
//            Imgproc.line(lienzo, new Point(gap + escala * Cara.get(tri[2] - 1, 0), gap + escala * Cara.get(tri[2] - 1, 1)), new Point(gap + escala * Cara.get(tri[0] - 1, 0), gap + escala * Cara.get(tri[0] - 1, 1)), new Scalar(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));

            //  dibuja los rectangulos
//            List<MatOfPoint> border = new ArrayList<MatOfPoint>();
//            border.add(new MatOfPoint(
//                    new Point(gap + escala * Cara.get(tri[0] - 1, 0), gap + escala * Cara.get(tri[0] - 1, 1)),
//                    new Point(gap + escala * Cara.get(tri[1] - 1, 0), gap + escala * Cara.get(tri[1] - 1, 1)),
//                    new Point(gap + escala * Cara.get(tri[2] - 1, 0), gap + escala * Cara.get(tri[2] - 1, 1))));           
//            Imgproc.fillPoly(lienzo, border, new Scalar(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
            
            affine(mat,
                    new double[][]{{puntos[tri[0] - 1][0], puntos[tri[0] - 1][1]},
                    {puntos[tri[1] - 1][0], puntos[tri[1] - 1][1]},
                    {puntos[tri[2] - 1][0], puntos[tri[2] - 1][1]}},
                    new double[][]{
                    {gap + escala * Cara.get(tri[0] - 1, 0), gap + escala * Cara.get(tri[0] - 1, 1)},
                    {gap + escala * Cara.get(tri[1] - 1, 0), gap + escala * Cara.get(tri[1] - 1, 1)},
                    {gap + escala * Cara.get(tri[2] - 1, 0), gap + escala * Cara.get(tri[2] - 1, 1)}},
                    new double[][]{{C.get(0, 0), C.get(1, 0), C.get(2, 0), C.get(3, 0)},
                    {C.get(0, 1), C.get(1, 1), C.get(2, 1), C.get(3, 1)}},
                    lienzo, escala, gap);

//            Imgproc.line(lienzo, new Point(gap + escala * Shape3D[tri[0] - 1][0], gap + escala * Shape3D[tri[0] - 1][1]), new Point(gap + escala * Shape3D[tri[1] - 1][0], gap + escala * Shape3D[tri[1] - 1][1]), new Scalar(0, 255, 0));
//            Imgproc.line(lienzo, new Point(gap + escala * Shape3D[tri[1] - 1][0], gap + escala * Shape3D[tri[1] - 1][1]), new Point(gap + escala * Shape3D[tri[2] - 1][0], gap + escala * Shape3D[tri[2] - 1][1]), new Scalar(0, 255, 0));
//            Imgproc.line(lienzo, new Point(gap + escala * Shape3D[tri[2] - 1][0], gap + escala * Shape3D[tri[2] - 1][1]), new Point(gap + escala * Shape3D[tri[0] - 1][0], gap + escala * Shape3D[tri[0] - 1][1]), new Scalar(0, 255, 0));
//

            //  Imgcodecs.imwrite(carpetaalmacen.getAbsolutePath() + "\\" + image.getName(), lienzo);
        }
        Imgcodecs.imwrite(carpetaalmacen.getAbsolutePath() + "\\" + image.getName(), lienzo);
       // Imgcodecs.imwrite(carpetaalmacen.getAbsolutePath() + "\\" + image.getName(), mat);
//        Imgcodecs.imwrite(carpetaalmacen.getAbsolutePath() + "\\lateral_" + image.getName(), lienzo2);

    }
    public static int faceTemplateTriangles[][] = new int[][]{
         { 54, 36, 53 },
{ 49, 32, 4 },
{ 34, 31, 33 },
{ 56, 57, 11 },
{ 53, 35, 34 },
{ 31, 35, 36 },
{ 31, 36, 30 },
{ 36, 48, 30 },
{ 9, 58, 59 },
{ 46, 16, 17 },
{ 49, 4, 5 },
{ 43, 23, 28 },
{ 3, 4, 32 },
{ 42, 32, 41 },
{ 19, 38, 20 },
{ 37, 1, 2 },
{ 37, 18, 1 },
{ 37, 19, 18 },
{ 37, 38, 19 },
{ 23, 21, 22 },
{ 23, 24, 21 },
{ 45, 46, 26 },
{ 28, 23, 22 },
{ 29, 43, 28 },
{ 24, 20, 21 },
{ 43, 44, 23 },
{ 25, 20, 24 },
{ 23, 44, 24 },
{ 24, 45, 25 },
{ 42, 37, 2 },
{ 36, 14, 15 },
{ 42, 38, 37 },
{ 9, 10, 57 },
{ 40, 41, 30 },
{ 51, 34, 33 },
{ 41, 39, 38 },
{ 60, 49, 6 },
{ 6, 49, 5 },
{ 31, 30, 32 },
{ 52, 53, 34 },
{ 31, 32, 33 },
{ 32, 50, 33 },
{ 54, 55, 36 },
{ 34, 35, 31 },
{ 34, 51, 52 },
{ 16, 46, 47 },
{ 15, 16, 47 },
{ 3, 42, 2 },
{ 40, 28, 22 },
{ 21, 38, 39 },
{ 21, 20, 38 },
{ 22, 39, 40 },
{ 22, 21, 39 },
{ 29, 40, 30 },
{ 29, 28, 40 },
{ 30, 41, 32 },
{ 40, 39, 41 },
{ 32, 42, 3 },
{ 41, 38, 42 },
{ 30, 43, 29 },
{ 45, 26, 25 },
{ 24, 44, 45 },
{ 47, 36, 15 },
{ 43, 48, 44 },
{ 46, 27, 26 },
{ 27, 46, 17 },
{ 45, 44, 48 },
{ 45, 47, 46 },
{ 48, 36, 47 },
{ 45, 48, 47 },
{ 43, 30, 48 },
{ 32, 49, 50 },
{ 6, 7, 60 },
{ 10, 11, 57 },
{ 51, 33, 50 },
{ 61, 60, 50 },
{ 56, 12, 55 },
{ 50, 60, 68 },
{ 53, 36, 35 },
{ 64, 66, 54 },
{ 55, 13, 14 },
{ 52, 63, 64 },
{ 55, 14, 36 },
{ 65, 55, 54 },
{ 12, 13, 55 },
{ 12, 56, 11 },
{ 54, 53, 64 },
{ 67, 59, 58 },
{ 58, 9, 57 },
{ 8, 9, 59 },
{ 63, 52, 62 },
{ 8, 59, 7 },
{ 68, 67, 63 },
{ 59, 60, 7 },
{ 59, 68, 60 },
{ 49, 61, 50 },
{ 49, 60, 61 },
{ 51, 62, 52 },
{ 51, 50, 62 },
{ 57, 66, 67 },
{ 64, 53, 52 },
{ 66, 64, 63 },
{ 66, 56, 54 },
{ 56, 65, 54 },
{ 56, 55, 65 },
{ 67, 66, 63 },
{ 57, 56, 66 },
{ 57, 67, 58 },
{ 63, 62, 68 },
{ 50, 68, 62 },
{ 59, 67, 68 }};
    
    public static Random rnd = new Random();

    private static void affine(Mat mat, double[][] from, double[][] to, double[][] coeficients, Mat lienzo, double escala, double gap) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        //   http://stackoverflow.com/questions/10100715/opencv-warping-from-one-triangle-to-another
        //  https://www.learnopencv.com/warp-one-triangle-to-another-using-opencv-c-python/
        //   http://docs.opencv.org/2.4/doc/tutorials/imgproc/imgtrans/warp_affine/warp_affine.html
        MatOfPoint2f src_pf = new MatOfPoint2f(new Point(from[0][0], from[0][1]), new Point(from[1][0], from[1][1]), new Point(from[2][0], from[2][1]));
        MatOfPoint2f dst_pf = new MatOfPoint2f(new Point(to[0][0], to[0][1]), new Point(to[1][0], to[1][1]), new Point(to[2][0], to[2][1]));

        //  https://www.learnopencv.com/warp-one-triangle-to-another-using-opencv-c-python/#download
//how do I set up the position numbers in MatOfPoint2f here?
        //  Mat perspective_matrix = Imgproc.getAffineTransform(src_pf, dst_pf);
        Rect r1 = Imgproc.boundingRect(new MatOfPoint(new Point(from[0][0], from[0][1]), new Point(from[1][0], from[1][1]), new Point(from[2][0], from[2][1])));
        Rect r2 = Imgproc.boundingRect(new MatOfPoint(new Point(to[0][0], to[0][1]), new Point(to[1][0], to[1][1]), new Point(to[2][0], to[2][1])));

        MatOfPoint2f tri1Cropped = new MatOfPoint2f(
                new Point(from[0][0] - r1.x, from[0][1] - r1.y),
                new Point(from[1][0] - r1.x, from[1][1] - r1.y),
                new Point(from[2][0] - r1.x, from[2][1] - r1.y));

        MatOfPoint tri2CroppedInt = new MatOfPoint(
                new Point(to[0][0] - r2.x, to[0][1] - r2.y),
                new Point(to[1][0] - r2.x, to[1][1] - r2.y),
                new Point(to[2][0] - r2.x, to[2][1] - r2.y));

        MatOfPoint2f tri2Cropped = new MatOfPoint2f(
                new Point( (to[0][0] - r2.x),  (to[0][1] - r2.y)),
                new Point( (to[1][0] - r2.x), (to[1][1] - r2.y)),
                new Point( (to[2][0] - r2.x),  (to[2][1] - r2.y)));
//        for (int i = 0; i < 3; i++) {
//           // tri1Cropped.push_back(new MatOfPoint(new Point(from[i][0] - r1.x, from[i][1] - r1.y))); //           new Point( from[i][0]  - r1.x, from[i][1]-  r1.y) );
//            //tri2Cropped.push_back(new MatOfPoint(new Point(to[i][0] - r2.x, to[i][1] - r2.y)));
//
//            // fillConvexPoly needs a vector of Point and not Point2f
//           // tri2CroppedInt.push_back(new MatOfPoint2f(new Point((int) (to[i][0] - r2.x), (int) (to[i][1] - r2.y))));
//
//        }

        // Apply warpImage to small rectangular patches
        Mat img1Cropped = mat.submat(r1);
         //img1(r1).copyTo(img1Cropped);

        // Given a pair of triangles, find the affine transform.
        Mat warpMat = Imgproc.getAffineTransform(tri1Cropped, tri2Cropped);
       
//       Mat bbb = warpMat.mul(tri1Cropped);
//        
//       System.out.println( warpMat.dump() );
//       System.out.println( tri2Cropped.dump() );
//       System.out.println( bbb.dump() );

        // Apply the Affine Transform just found to the src image
        Mat img2Cropped = Mat.zeros(r2.height, r2.width, img1Cropped.type());
        Imgproc.warpAffine(img1Cropped, img2Cropped, warpMat, img2Cropped.size(),0,Imgproc.INTER_LINEAR, new Scalar(Core.BORDER_TRANSPARENT)); //, 0, Imgproc.INTER_LINEAR, new Scalar(Core.BORDER_REFLECT_101));

        // Get mask by filling triangle
        Mat mask = Mat.zeros(r2.height, r2.width, CvType.CV_8UC3);    ///CV_8U    CV_32FC3
        Imgproc.fillConvexPoly(mask, tri2CroppedInt, new Scalar(1.0, 1.0, 1.0),16, 0);

         // Copy triangular region of the rectangular patch to the output image
//         Core.multiply(img2Cropped,mask, img2Cropped);
//         
//         Core.multiply(mask, new Scalar(-1), mask);
//        Core.(mask,new Scalar(gap), mask);
         //Core.multiply(lienzo.submat(r2),  (new Scalar(1.0,1.0,1.0)). - Core.multiply(mask,), lienzo.submat(r2));
//         img2(r2) = img2(r2) + img2Cropped;
        
        
       // Core.subtract(Mat.ones(mask.height(), mask.width(), CvType.CV_8UC3), mask, mask);
       // Mat ff =   ;
        
       
        
        //   este
         Core.multiply(img2Cropped,mask, img2Cropped);
         //Core.multiply(lienzo.submat(r2), mask  , lienzo.submat(r2));         
         Core.add(lienzo.submat(r2), img2Cropped, lienzo.submat(r2));
    
         
         
         /*
         Mat bb = new Mat(mat, r2);
         bb.setTo(new Scalar(rnd.nextInt(),rnd.nextInt(),rnd.nextInt()));         
         Core.multiply(bb,mask, bb);
         Core.multiply(lienzo.submat(r2), mask  , lienzo.submat(r2));         
         Core.add(lienzo.submat(r2), bb, lienzo.submat(r2));
         
         */
         
         
        // lienzo.submat(r2).setTo(new Scalar(rnd.nextInt(),rnd.nextInt(),rnd.nextInt()));
         
         
//         
//      Imgproc.fillConvexPoly(lienzo, new MatOfPoint(
//                new Point(to[0][0] , to[0][1]),
//                new Point(to[1][0] , to[1][1]),
//                new Point(to[2][0] , to[2][1] )), new Scalar(1,1,1));
         
         
         
         
         
         
         
         
         
         
        
//        img2Cropped.copyTo(lienzo);
//        return;
        
        
        
        
        
        
       // http://stackoverflow.com/questions/14111716/how-to-set-a-mask-image-for-grabcut-in-opencv  

      //  Imgproc.warpAffine(mat, lienzo, perspective_matrix, lienzo.size());
        // Imgproc.getAffineTransform(null, null);
        /*     
         // Find bounding rectangle for each triangle
         Rect r1 = boundingRect(tri1);
         Rect r2 = boundingRect(tri2);
    
         // Offset points by left top corner of the respective rectangles
         vector<Point2f> tri1Cropped, tri2Cropped;
         vector<Point> tri2CroppedInt;
         for(int i = 0; i < 3; i++)
         {
         tri1Cropped.push_back( Point2f( tri1[i].x - r1.x, tri1[i].y -  r1.y) );
         tri2Cropped.push_back( Point2f( tri2[i].x - r2.x, tri2[i].y - r2.y) );

         // fillConvexPoly needs a vector of Point and not Point2f
         tri2CroppedInt.push_back( Point((int)(tri2[i].x - r2.x), (int)(tri2[i].y - r2.y)) );

         }

         // Apply warpImage to small rectangular patches
         Mat img1Cropped;
         img1(r1).copyTo(img1Cropped);

         // Given a pair of triangles, find the affine transform.
         Mat warpMat = getAffineTransform( tri1Cropped, tri2Cropped );
    
         // Apply the Affine Transform just found to the src image
         Mat img2Cropped = Mat::zeros(r2.height, r2.width, img1Cropped.type());
         warpAffine( img1Cropped, img2Cropped, warpMat, img2Cropped.size(), INTER_LINEAR, BORDER_REFLECT_101);

         // Get mask by filling triangle
         Mat mask = Mat::zeros(r2.height, r2.width, CV_32FC3);
         fillConvexPoly(mask, tri2CroppedInt, Scalar(1.0, 1.0, 1.0), 16, 0);
    
         // Copy triangular region of the rectangular patch to the output image
         multiply(img2Cropped,mask, img2Cropped);
         multiply(img2(r2), Scalar(1.0,1.0,1.0) - mask, img2(r2));
         img2(r2) = img2(r2) + img2Cropped;*/
    }

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
   // int CantidadTriangulos = 111;

    static double Shape3D[][] = new double[][]{
        {0.0079472212856871, 0.1718322301369845, 0.009608727145607697},
        {0.0029027686449141343, 0.3124944808466294, 0.007441504229196194},
        {0.013773105410669916, 0.4434437098969056, 0.01939895479844035},
        {0.03651859955128158, 0.5703311263585017, 0.04218938793887264},
        {0.07214407167584935, 0.6976600444775358, 0.08460478104076212},
        {0.1367266588066494, 0.808918322891433, 0.15410715421192583},
        {0.22533360324173343, 0.8966887413831958, 0.2490664825250194},
        {0.33741682112655597, 0.9682119210625493, 0.36325717441374705},
        {0.49553910913978944, 1.0, 0.5014642938449363},
        {0.6619740080932731, 0.9695364240275048, 0.36325717441374705},
        {0.7776197734163803, 0.8929801330311357, 0.2490664825250194},
        {0.870154653940633, 0.7987637968145085, 0.15410715421192583},
        {0.9271554094403093, 0.685033113334411, 0.08460478104076212},
        {0.957025997955651, 0.5552317883927378, 0.04218938793887264},
        {0.9735598702269629, 0.42286975745116906, 0.01939895479844035},
        {0.9803195764526476, 0.2877704196459939, 0.007441504229196194},
        {0.9707281025996445, 0.14993377472629232, 0.009608727145607697},
        {0.09032219801999612, 0.1006622519341335, 0.2941536134345652},
        {0.15517882670168917, 0.034701987417528085, 0.3549770137625481},
        {0.23894436028389646, 0.018189845553113367, 0.4098714096890551},
        {0.32079159999881435, 0.024900663068025675, 0.4666058409414325},
        {0.3932300600607115, 0.0736423852764097, 0.4998287060462161},
        {0.58789129546406, 0.07055187743814043, 0.4998287060462161},
        {0.6669981138988914, 0.01660044204535044, 0.4666058409414325},
        {0.7467443632882594, 0.008918322791064913, 0.4098714096890551},
        {0.8266733076303693, 0.02198675584254764, 0.3549770137625481},
        {0.887145263468339, 0.08309050840032962, 0.2941536134345652},
        {0.49791414051439253, 0.12803532006835958, 0.48720019336654347},
        {0.4996497406835394, 0.22092715357095097, 0.5369799021016239},
        {0.5008372559944612, 0.3091390737201519, 0.6032795978818601},
        {0.5021161189072139, 0.3918763803930594, 0.6737457983989252},
        {0.4201775320923048, 0.4802649009041317, 0.5196379475293907},
        {0.4489519534003944, 0.4766445909765689, 0.545907831058906},
        {0.4986449195725997, 0.4824724064312042, 0.5695198807300385},
        {0.5466936346830074, 0.4755849893071796, 0.545907831058906},
        {0.5796700321256403, 0.48176600498371835, 0.5196379475293907},
        {0.18751579444438424, 0.1851655634803546, 0.3500226162268996},
        {0.24278095052767318, 0.1489624714814003, 0.3935669973177866},
        {0.30708949485298104, 0.14781456938015564, 0.3935669973177866},
        {0.36125848197257987, 0.1818101553501979, 0.3500226162268996},
        {0.30590197979297895, 0.2037086100081306, 0.3911139642293072},
        {0.24460789779681108, 0.2067108176654642, 0.3911139642293072},
        {0.6329255469907287, 0.17924944759386385, 0.3500226162268996},
        {0.6929407655723047, 0.14286975799315585, 0.3935669973177866},
        {0.7552396676757331, 0.1426048569485091, 0.3935669973177866},
        {0.8031970351843101, 0.17739514429605316, 0.3500226162268996},
        {0.7512203842356533, 0.19955849949679305, 0.3911139642293072},
        {0.6956811860996318, 0.19805739616996593, 0.3911139642293072},
        {0.3304744204499691, 0.6566887419552928, 0.4547478332616692},
        {0.39624452364445034, 0.6167770421061183, 0.5048376919423653},
        {0.44876925844765275, 0.5975275936290166, 0.5360160730709402},
        {0.4962698879470763, 0.6075938195250055, 0.5623899169537041},
        {0.5463282450284441, 0.5969977927943222, 0.5360160730709402},
        {0.6040597798372469, 0.6139514348106561, 0.5048376919423653},
        {0.6747626399809173, 0.6476821189832923, 0.4547478332616692},
        {0.6123723897738182, 0.6911258278263349, 0.48858404375587344},
        {0.5590255288124177, 0.7083443703854757, 0.514548991180352},
        {0.5112508557547426, 0.7129359823033317, 0.540411713577669},
        {0.45826938419698576, 0.7095805749259343, 0.514548991180352},
        {0.4035523127210016, 0.694481235956491, 0.48858404375587344},
        {0.3757827137784508, 0.6542163356344938, 0.4891973891452939},
        {0.4579953423951728, 0.637174393186304, 0.5281448097204178},
        {0.4984622246198579, 0.6395584995770874, 0.5537008601250171},
        {0.5381983292918547, 0.6379690955674849, 0.5281448097204178},
        {0.6253437153596053, 0.6470640182185818, 0.4891973891452939},
        {0.5393858448536967, 0.660309050377337, 0.5213980124023716},
        {0.49873626692351036, 0.6613686530504052, 0.5432739915936363},
        {0.45872612120246004, 0.6602207501964011, 0.5213980124023716}
    };

    double frontalancho = 89.13250393181819, frontalalto = 90.57584652272726;
    double lateralancho = 54.54846572727275, lateralalto = 80.93841643181817;
}
