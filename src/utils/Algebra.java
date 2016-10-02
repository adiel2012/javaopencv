/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;


import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 *
 * @author acastano
 */
public class Algebra {

    public static double[] Cramer3(double[][] A, double[] B) {
        double detA = det3(A);
        double x1 = det3(new double[][]{{B[0],A[0][1],A[0][2]}, {B[1],A[1][1],A[1][2]}, {B[2],A[2][1],A[2][2]}}) / detA;
        double x2 = det3(new double[][]{{A[0][0],B[0],A[0][2]}, {A[1][0],B[1],A[1][2]}, {A[2][0],B[2],A[2][2]}}) / detA;
        double x3 = det3(new double[][]{{A[0][0],A[0][1],B[0]}, {A[1][0],A[1][1],B[1]}, {A[2][0],A[2][1],B[2]}}) / detA;
        return new double[]{x1, x2, x3};
    }

    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {

        System.load("D:\\opencv\\opencv\\build\\java\\x64\\opencv_java300.dll");
        double[][] ff = new double[3][3];
        double[] ff2 = new double[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ff[i][j] = Math.random();
            }
            ff2[i] = Math.random();
        }

        ff = new double[][]{new double[]{1, -3, 2}, new double[]{5, 6, -1}, new double[]{4, -1, 3}};
        ff2 = new double[]{-3, 13, 8};

        for (double val : Cramer3(ff, ff2)) {
            System.out.println(val);
        }
        
       // System.out.println(det3(new double[][]{{-2,2,-3},{-1,1,3},{2,0,-1}}));
        

    }

    private static double det3(double[][] m) {
        return m[0][0] * m[1][1] * m[2][2]
                + m[0][1] * m[1][2] * m[2][0]
                + m[0][2] * m[1][0] * m[2][1]
                - m[0][2] * m[1][1] * m[2][0]
                - m[0][1] * m[1][0] * m[2][2]
                - m[0][0] * m[1][2] * m[2][1];
    }
}
