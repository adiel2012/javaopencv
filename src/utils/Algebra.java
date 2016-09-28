/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 *
 * @author acastano
 */
public class Algebra {

    public static double[] linear_solve(double[][] Ad, double[] Bd) {
        //A*x=B
        Mat mat = new Mat(Ad.length, Ad[0].length, CvType.CV_32F);

        for (int i = 0; i < Ad.length; i++) {
            for (int j = 0; j < Ad[0].length; j++) {
                mat.row(i).col(j).setTo(new Scalar(Ad[i][j]));
            }
        }

        Mat mat2 = new Mat(Bd.length, 1, CvType.CV_32F);
        for (int i = 0; i < Bd.length; i++) {
            mat.row(i).col(0).setTo(new Scalar(Bd[i]));
        }
        System.out.println(mat);
        return null;
    }

    public static void main(String[] args) {
        double[][] ff = new double[3][4];
        double[] ff2 = new double[3];
          linear_solve(ff, ff2);
    }
}
