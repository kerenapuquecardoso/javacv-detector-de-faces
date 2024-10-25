package com.facial_reconhecimento;

import java.awt.event.KeyEvent;
import java.util.Scanner;

import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

public class Captura {
    public static void main(String[] args) throws Exception, InterruptedException {
        KeyEvent teclado = null;
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);
        camera.start();

        CascadeClassifier detectorFace = new CascadeClassifier(
                "javacv/src/main/resources/haarcascade_frontalface_alt.xml");
        CanvasFrame cFrame = new CanvasFrame("Preview", CanvasFrame.getDefaultGamma() / camera.getGamma());
        Frame frameCapturado = null;
        Mat imagemColorida = new Mat();
        int numeroAmostras = 25;
        int countAmostra = 1;
        System.out.println("Digite seu id: ");
        Scanner cadastro = new Scanner(System.in);
        int idPessoa = cadastro.nextInt();

        while ((frameCapturado = camera.grab()) != null) {
            imagemColorida = converter.convert(frameCapturado);
            Mat imagemCinza = new Mat();
            RectVector facesDetectadas = new RectVector();
            cvtColor(imagemColorida, imagemCinza, COLOR_BGRA2GRAY);

            detectorFace.detectMultiScale(imagemCinza, facesDetectadas, 1.1, 1, 0, new Size(150, 150),
                    new Size(500, 500));

            if (teclado == null) {
                teclado = cFrame.waitKey(5);
            }

            for (int i = 0; i < facesDetectadas.size(); i++) {
                Rect dadosFace = facesDetectadas.get(0);
                rectangle(imagemColorida, dadosFace, new Scalar(0, 0, 255, 0));
                Mat faceCapturada = new Mat(imagemCinza, dadosFace);
                resize(faceCapturada, faceCapturada, new Size(160, 160));
                if (teclado == null) {
                    teclado = cFrame.waitKey(5);
                }
                if (teclado != null) {
                    if (teclado.getKeyChar() == 'q') {
                        if (countAmostra <= numeroAmostras) {
                            imwrite("javacv/src/main/fotos/pessoa." + idPessoa + "." + countAmostra + ".jpg",
                                    faceCapturada);
                            System.out.println("Foto" + countAmostra + " capturada\n");
                            countAmostra++;
                        }
                    }
                    teclado = null;
                }
            }

            if (teclado == null) {
                teclado = cFrame.waitKey(20);
            }
            if (cFrame.isVisible()) {
                cFrame.showImage(frameCapturado);
            }

            if (countAmostra > numeroAmostras) {
                break;
            }
        }
        cFrame.dispose();
        camera.stop();
    }

}
