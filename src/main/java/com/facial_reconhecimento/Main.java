package com.facial_reconhecimento;

import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

public class Main {
    public static void main(String[] args) {
        LBPHFaceRecognizer recognizer = LBPHFaceRecognizer.create();
        System.out.println(recognizer);
    }
}
