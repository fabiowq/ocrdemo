package com.ocr.demo;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

@Component
public class GrayScale {

    public void rescale(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
        //Imgproc.threshold(image, image, 127,255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

    }

}
