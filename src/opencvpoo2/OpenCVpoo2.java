/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opencvpoo2;

import java.util.ArrayList;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

/**
 *
 * @author Gustavo Armborst Guedes de Azevedo
 */
public class OpenCVpoo2 {

    public static ArrayList<Point> circlesList = new ArrayList<>();
    public static ArrayList<Mat> imgList = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture clipe = new VideoCapture("poo2.mp4"); //seleciona o video

        System.out.println(clipe.isOpened());
        Mat m = new Mat();

//        ColorBlobDetector blob = new ColorBlobDetector();
        int framesContador = (int) clipe.get(Videoio.CV_CAP_PROP_FRAME_COUNT);

//        FeatureDetector blob = FeatureDetector.create(FeatureDetector.SIFT);
        System.out.println(framesContador);

        for (int i = 0; i < framesContador; i++) {
            clipe.read(m);
            ColorBlobDetector.circles(m); //le frame a frame e passa pelo metodo de reconhecimento de circulo
        }

        m = imgList.get(imgList.size() / 2); //seleciona um com circulo para gerar a imagem da previsão final

        ColorBlobDetector.geraCurva(m); // gera a curva

        ShowWindow.mostra("Final", m); // mostra a imagem final
    }

}
