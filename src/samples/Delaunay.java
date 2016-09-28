/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

//import java.awt.Point;

import org.opencv.core.Point;


/**
 *
 * @author LABORATORIO 10
 */
public class Delaunay {
    public static int[][] calculate(Point[] points){
        int N = points.length;
        
        // https://www.learnopencv.com/delaunay-triangulation-and-voronoi-diagram-using-opencv-c-python/
        
//        for (int i = 0; i < N; i++) {
//            for (int j = i+1; j < N; j++) {
//                for (int k = j+1; k < N; k++) {
//                    boolean isTriangle = true;
//                    for (int a = 0; a < N; a++) {
//                        if (a == i || a == j || a == k) continue;
//                        if (points[a].inside(points[i], points[j], points[k])) {
//                           isTriangle = false;
//                           break;
//                        }
//                    }
//                    if (isTriangle) {
//                        points[i].drawTo(points[j], draw);
//                        points[i].drawTo(points[k], draw);
//                        points[j].drawTo(points[k], draw);
//                    }
//                }
//            }
//        }
        
        return null;
    }
}
