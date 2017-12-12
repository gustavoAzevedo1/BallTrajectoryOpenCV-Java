package opencvpoo2;

import static opencvpoo2.OpenCVpoo2.circlesList;
import static opencvpoo2.OpenCVpoo2.imgList;
import org.apache.commons.math3.analysis.interpolation.DividedDifferenceInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionNewtonForm;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ColorBlobDetector {

    public static void circles(Mat img) {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//            long start_time = System.nanoTime();
//        Imgproc.resize(img, img, new Size(img.size().width, img.size().height));

            Mat gray = new Mat();
//            Imgproc.
            Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.blur(gray, gray, new Size(3, 3));

            Mat edges = new Mat();
            int lowThreshold = 120;
            int ratio = 3;
            Imgproc.Canny(gray, edges, lowThreshold, lowThreshold * ratio);

            Mat circles = new Mat();
//            ShowWindow.mostra("ss", gray);
//            ShowWindow.mostra("ss", edges);

            Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 5, 60, 200, 150, 30, 0);

//        System.out.println("#rows " + circles.rows() + " #cols " + circles.cols());
            double x = 0.0;
            double y = 0.0;
            int r = 0;

            for (int i = 0; i < circles.rows(); i++) {
                double[] data = circles.get(i, 0);
                for (int j = 0; j < data.length; j++) {
                    x = data[0];
                    y = data[1];
                    r = (int) data[2];
                }
                Point center = new Point(x, y);
                circlesList.add(center);
                imgList.add(img.clone());

                // circle center
                Imgproc.circle(img, center, 3, new Scalar(0, 255, 0), -1);
                // circle outline
                Imgproc.circle(img, center, r, new Scalar(0, 0, 255), 1);
                Integer part = imgList.size();

                if (circlesList.size() > 3) {
                    geraCurva(img);
                }

                ShowWindow.mostra(part.toString(), img);
//            System.out.println(x);
//            System.out.println(y);
//            System.out.println(r);
            }
//        long end_time = System.nanoTime();
//        long duration = (end_time - start_time) / 1000000;  //divide by 1000000 to get milliseconds.
//        System.out.println("duration :  " + duration * 0.001 + " s");
        } catch (Exception e) {
        }
    }

    public static void geraCurva(Mat img) {
        double[] xx = new double[3];
        double[] yy = new double[3];
        int a = 0;
        for (int z = 0; z < circlesList.size(); z += circlesList.size() / 3) { //pega o primeiro, do meio, e ultimo pontos
            xx[a] = circlesList.get(z).x;
            yy[a] = circlesList.get(z).y;
            a++;
            if (a > 2) {
                break;
            }
        }

        byte cor[] = new byte[3]; // seleciona a cor vermelha para a linha da curva
        cor[0] = 0;
        cor[1] = 0;
        cor[2] = (byte) 255;

        PolynomialFunctionNewtonForm funcao = new DividedDifferenceInterpolator().interpolate(xx, yy); // manda para o metodo de interpolacao

        for (int z = 0; z < img.size().width; z++) { //gera a curva na imagem
            double val = funcao.value(z);
            if (val < img.size().height) {
                img.put((int) val, z, cor);
                img.put((int) val, z + 1, cor);
            }
        }
    }
}
