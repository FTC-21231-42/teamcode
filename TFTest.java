package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotMap.VUFORIA_KEY;
import static org.firstinspires.ftc.teamcode.RobotMap.tFod;
import static org.firstinspires.ftc.teamcode.RobotMap.vuforia;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "TFTest", group = "0")
public class TFTest extends LinearOpMode {
    static ElapsedTime runtime = new ElapsedTime();
    static final String[] LABELS_LOC = {
            "Masked",
            "No Mask"
    };
    static final String TFOD_MODEL_ASSET_LOC = "/sdcard/FIRST/tflitemodels/model.tflite";

    @Override
    public void runOpMode() throws InterruptedException {
        //init hardware map
//        initRobot(hardwareMap);
        //if can't detect anything, so move to parking

        //init api
        initVuforia();
        initTFod();

        //activate tensorflow
        if (tFod != null) {
            tFod.activate();
            tFod.setZoom(1.0, 16.0 / 9.0);
        }

        telemetry.addData("Initialized", "True");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()){
            telemetry.addData("Initialized", "True");
            telemetry.update();

        }

    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

    }

    private void initTFod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.7f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tFod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tFod.loadModelFromFile(TFOD_MODEL_ASSET_LOC, LABELS_LOC);

    }

}
