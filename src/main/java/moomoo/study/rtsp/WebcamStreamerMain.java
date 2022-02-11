package moomoo.study.rtsp;

import moomoo.study.rtsp.service.ServiceManager;

public class WebcamStreamerMain {

    public static void main(String[] args) {

        ServiceManager serviceManager = ServiceManager.getInstance();
        serviceManager.loop();
    }
}