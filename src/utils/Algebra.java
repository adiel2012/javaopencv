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

    public static double[] linear_solve(double[][] Ad, double[] Bd) {
        //A*x=B
        Mat mat = new Mat(Ad.length, Ad[0].length, CvType.CV_32F);

        for (int i = 0; i < Ad.length; i++) {
            for (int j = 0; j < Ad[0].length; j++) {
                mat.put(i, j, Ad[i][j]);
            }
        }

        Mat mat2 = new Mat(Bd.length, 1, CvType.CV_32F);
        for (int i = 0; i < Bd.length; i++) {
            mat2.put(i, 0, Bd[i]);
        }

        Mat res = new Mat(Ad[0].length, 1, CvType.CV_32F);
        
       // Core.gemm(m2, m3, 1, new Mat(), 0, m1, 0);
        
        Core.solve(mat, res, mat2, org.opencv.core.Core.DECOMP_SVD);
        System.out.println(mat);
        
        double[] mires = new double[Ad[0].length];
        for (int i = 0; i < Ad[0].length; i++) {
            mires[i] = res.get ;
        }
        return mires;
    }

    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {

        System.load("D:\\opencv_instalaciones\\opencv\\build\\java\\x64\\opencv_java310.dll");
        double[][] ff = new double[3][3];
        double[] ff2 = new double[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ff[i][j]=Math.random();
            }
            ff2[i]=Math.random();
        }
        
        ff = new double[][]{new double[]{3,2,1},new double[]{2,2,4},new double[]{-1,0.5,-1}};
        ff2 = new double[]{1,-2,0};
        
        for (double val : linear_solve(ff, ff2)) {
            System.out.println(val);
        }
        
    }
}
