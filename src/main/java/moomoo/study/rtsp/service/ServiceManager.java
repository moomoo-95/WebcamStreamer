package moomoo.study.rtsp.service;

import moomoo.study.rtsp.service.job.CameraManager;
import moomoo.study.rtsp.service.schedule.ScheduleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class ServiceManager {

    private static final Logger log = LoggerFactory.getLogger(ServiceManager.class);

    private static final String MAIN_SCHEDULE_JOB = "MAIN_SCHEDULE_JOB";
    private static final int DELAY_TIME = 1000;

    private static ServiceManager serviceManager = null;

    private final ScheduleManager scheduleManager = new ScheduleManager();

    private CameraManager cameraManager;
    private boolean isQuit = false;

    public ServiceManager() {
        // nothing
    }

    public static ServiceManager getInstance() {
        if (serviceManager == null) {
            serviceManager = new ServiceManager();
        }
        return serviceManager;
    }

    public void loop() {
        if (!start()) {
            log.error("() () () Fail to start service");
        }

        while (!isQuit) {
            try {
                Thread.sleep(DELAY_TIME);
            } catch (Exception e) {
                log.error("ServiceManager.loop ", e);
            }
        }
    }

    public boolean start() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Process is about to quit (Ctrl+C)");
            this.isQuit = true;
            this.stop();
        }));

        scheduleManager.initJob(MAIN_SCHEDULE_JOB, 10, 10 * 2);
        cameraManager = new CameraManager(
                scheduleManager,
                CameraManager.class.getSimpleName(),
                0, 0, TimeUnit.MILLISECONDS,
                1, 1, false
        );
        scheduleManager.startJob(MAIN_SCHEDULE_JOB, cameraManager);

        return true;
    }

    public void stop() {
        cameraManager.stop();
    }
}
