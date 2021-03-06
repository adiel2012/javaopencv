/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facerecognition;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import samples.LWF;

/**
 *
 * @author adiel
 */
public class sample1 {

    private static CascadeClassifier[] faceDetectors = null;
    private static String urlHelen;
    private static double percent = 0.4;

    public static void main(String[] args) {
        System.load("C:\\opencv\\build\\java\\x64\\opencv_java310.dll");
        //generate_info();
        //generate_2d_mask();
        draw_initial_points();

    }

    private static File[] get_images() {
        urlHelen = "C:\\Users\\adiel\\Documents\\Adiel\\trabajos\\helen\\tr";
        File carpeta = new File(urlHelen);
        return carpeta.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 3, name.length()).equals("jpg");
                //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private static BufferedImage convert_to_BufferedImage(File image_file) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(image_file);
        } catch (IOException e) {
        }

        return img;
    }

    private static File get_puntos_file(File image_file) {
        return new File(urlHelen + "\\" + image_file.getName().substring(0, image_file.getName().length() - 3) + "pts");
    }

    private static Rect find_enclosing_rectangle(double[][] puntos, File image_file) {

        Mat image = Imgcodecs.imread(image_file.getAbsolutePath());
        int i = 0;
        Mat img2 = image.clone();
        for (CascadeClassifier faceDetector : faceDetectors) {

            // Detect faces in the image.
            // MatOfRect is a special container class for Rect.
            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(image, faceDetections);

            System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

            // Draw a bounding box around each face.
//            double percent = 0.4;
            for (Rect rect : faceDetections.toArray()) {
                Rect piv = rect.clone();
                //  falta expandir
                int h = piv.height, w = piv.width;
                piv.x -= w * percent / 2;
                piv.y -= h * percent / 2;
                piv.height *= (1 + percent);
                piv.width *= (1 + percent);

//            Mat croped = new Mat(image, rect);
//             Imgcodecs.imwrite("face"+(++i)+".png", croped);
                Imgproc.rectangle(img2, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));

                int r = 10;
                boolean dentro = true;
                for (double[] punto : puntos) {
//                    Imgproc.circle(img2, new Point(rect.x, rect.y), r, new Scalar(0, 255, 0));

                    if (piv.contains(new Point(punto)) == false) {
                        dentro = false;
//                        break;
                    }
                }
                if (dentro) {
//                    Imgcodecs.imwrite(urlHelen + "\\face" + (Math.random()) + ".png", img2);
                    return piv;
                }
            }

        }
//        Imgcodecs.imwrite( urlHelen + "\\face"+(Math.random())+".png", img2);

        return null;
    }

    private static void guardar_info(Rect rect, File image_file) {
        try {
            BufferedWriter bw = null;

            File f = new File(urlHelen + "\\" + image_file.getName().substring(0, image_file.getName().length() - 3) + "info");
            bw = new BufferedWriter(new FileWriter(f));
            bw.write(rect.height + " " + rect.width + " " + rect.x + " " + rect.y);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(sample1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void generate_info() {
        PrintWriter pw = null;
        try {
            faceDetectors = new CascadeClassifier[]{
                new CascadeClassifier("haarcascade_frontalface_alt_tree.xml"),
                new CascadeClassifier("haarcascade_frontalface_alt2.xml"),
                new CascadeClassifier("haarcascade_profileface.xml")
            };
            File[] image_files = get_images();
            int index = 0;
            int contador = 0;
            File resumen = new File(urlHelen + "\\summary.sum");
            pw = new PrintWriter(resumen);
            for (File image_file : image_files) {
                System.out.println("Analizando imagen " + (++index) + " de " + image_files.length);
//            BufferedImage img = convert_to_BufferedImage(image_file);
                File puntos_file = get_puntos_file(image_file);
                double[][] puntos = LWF.leerpuntos(puntos_file);
                Rect rect = find_enclosing_rectangle(puntos, image_file);
                if (rect != null) {
                    guardar_info(rect, image_file);
                    pw.println(rect.width + " " + rect.height + " " + rect.x + " " + rect.y);
                    contador++;
                }
            }
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(sample1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }
    }
// --------------------------------------------------------------------------------------------------------------------

    private static void generate_2d_mask() {
        PrintWriter pw = null;
        try {
            File[] ficheros = get_images_info();
            double[][] medias = new double[68][2];
            File mask = new File(urlHelen + "\\mask.msk");
            pw = new PrintWriter(mask);
            for (File fichero : ficheros) {
                try {
                    Scanner scan = new Scanner(fichero);
                    int w = scan.nextInt();
                    int h = scan.nextInt();
                    int x = scan.nextInt();
                    int y = scan.nextInt();
                    System.out.println(fichero.getName());
                    System.out.println(w + " " + h + " " + x + " " + y);
                    File image_file = new File(fichero.getAbsolutePath().replaceFirst(".info", ".jpg"));
                    File puntos_file = get_puntos_file(image_file);
                    double[][] puntos = LWF.leerpuntos(puntos_file);
                    int pos = 0;
                    for (double[] punto : puntos) {
                        System.out.println((punto[0] - x) + "  " + (punto[1] - y));
                        medias[pos][0] += (punto[0] - x) / w;
                        medias[pos][1] += (punto[1] - y) / h;
                        pos++;
                    }

                    //System.out.println(imagen_puntos.exists());
                    scan.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(sample1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            for (int i = 0; i < 68; i++) {
                medias[i][0] /= ficheros.length;
                medias[i][1] /= ficheros.length;
            }
            System.out.println(ficheros.length);
            pw.write("x        y\n");
            for (int i = 0; i < 68; i++) {
                System.out.println(medias[i][0] + "  " + medias[i][1]);
                pw.write(medias[i][0] + "  " + medias[i][1] + "\n");
            }

            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(sample1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }
    }

    private static File[] get_images_info() {
        urlHelen = "C:\\Users\\adiel\\Documents\\Adiel\\trabajos\\helen\\tr";
        File carpeta = new File(urlHelen);
        return carpeta.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 4, name.length()).equals("info");
                //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
//-------------------------------------------------------------------

    private static void draw_initial_points() {
//        PrintWriter pw = null;
//        try {
        faceDetectors = new CascadeClassifier[]{
            new CascadeClassifier("haarcascade_frontalface_alt_tree.xml"),
            new CascadeClassifier("haarcascade_frontalface_alt2.xml"),
            new CascadeClassifier("haarcascade_profileface.xml")
        };
        File[] image_files = get_images();
        int index = 0;
        int contador = 0;
//            File resumen = new File(urlHelen + "\\summary.sum");
//            pw = new PrintWriter(resumen);

        double[][] mask = leer_mask();

        for (File image_file : image_files) {
            System.out.println("Analizando imagen " + (++index) + " de " + image_files.length);
//            BufferedImage img = convert_to_BufferedImage(image_file);
//                File puntos_file = get_puntos_file(image_file);
//                double[][] puntos = LWF.leerpuntos(puntos_file);

            Mat image = Imgcodecs.imread(image_file.getAbsolutePath());
            Mat img2 = image.clone();

            for (CascadeClassifier faceDetector : faceDetectors) {

                // Detect faces in the image.
                // MatOfRect is a special container class for Rect.
                MatOfRect faceDetections = new MatOfRect();
                faceDetector.detectMultiScale(image, faceDetections);

                System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

                // Draw a bounding box around each face.
                for (Rect rect : faceDetections.toArray()) {
                    Rect piv = rect.clone();
                    //  falta expandir
                    int h = piv.height, w = piv.width;
                    piv.x -= w * percent / 2;
                    piv.y -= h * percent / 2;
                    piv.height *= (1 + percent);
                    piv.width *= (1 + percent);

//            Mat croped = new Mat(image, rect);
//             Imgcodecs.imwrite("face"+(++i)+".png", croped);
                    Imgproc.rectangle(img2, new Point(piv.x, piv.y), new Point(piv.x + piv.width, piv.y + piv.height), new Scalar(0, 255, 0));

                    for (double[] punto : mask) {
                        Imgproc.circle(img2, new Point(piv.x+piv.width*punto[0], piv.y+piv.height*punto[1]), 5, new Scalar(0, 255, 0));
                    }
                }

            }
//            pw.close();
            Imgcodecs.imwrite(urlHelen + "\\face" + (Math.random()) + ".png", img2);

        }

    }

    private static double[][] leer_mask() {
        double[][] mask = new double[68][2];
        try {

            File f = new File(urlHelen + "\\mask.msk");
            Scanner scan = new Scanner(f);
            scan.nextLine();

            for (int i = 0; i < 68; i++) {
                mask[i][0] = scan.nextDouble();
                mask[i][1] = scan.nextDouble();
                scan.nextLine();
            }

            scan.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(sample1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mask;
    }
}
