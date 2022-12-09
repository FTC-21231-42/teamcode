package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.RobotMap.COLLECT_MAX_POS;
import static org.firstinspires.ftc.teamcode.RobotMap.COLLECT_MIN_POS;
import static org.firstinspires.ftc.teamcode.RobotMap.Direction;
import static org.firstinspires.ftc.teamcode.RobotMap.ELE_COL;
import static org.firstinspires.ftc.teamcode.RobotMap.ELE_MIN_POS;
import static org.firstinspires.ftc.teamcode.RobotMap.ELE_TOP;
import static org.firstinspires.ftc.teamcode.RobotMap.IconTypeN;
import static org.firstinspires.ftc.teamcode.RobotMap.LABELS;
import static org.firstinspires.ftc.teamcode.RobotMap.TFOD_MODEL_ASSET;
import static org.firstinspires.ftc.teamcode.RobotMap.VUFORIA_KEY;
import static org.firstinspires.ftc.teamcode.RobotMap.collectorServo;
import static org.firstinspires.ftc.teamcode.RobotMap.eleMotor;
import static org.firstinspires.ftc.teamcode.RobotMap.initRobot;
import static org.firstinspires.ftc.teamcode.RobotMap.leftBotMotor;
import static org.firstinspires.ftc.teamcode.RobotMap.leftFrontMotor;
import static org.firstinspires.ftc.teamcode.RobotMap.rightBotMotor;
import static org.firstinspires.ftc.teamcode.RobotMap.rightFrontMotor;
import static org.firstinspires.ftc.teamcode.RobotMap.tFod;
import static org.firstinspires.ftc.teamcode.RobotMap.vuforia;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "Auto_new_bule_left", group = "0")
public class Auto_0_bln extends LinearOpMode {
    static ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        //init hardware map
        initRobot(hardwareMap);
        //if can't detect anything, so move to parking
        IconTypeN targetPos = IconTypeN.NA;

        //init api
        initVuforia();
        initTFod();

        //activate tensorflow
        if (tFod != null) {
            tFod.activate();
            tFod.setZoom(1.0, 16.0 / 9.0);
        }

        Claw(true);

        telemetry.addData("Initialized", "True");
        telemetry.update();
        waitForStart();

        //if can see anything after 5 seconds
        runtime.reset();
        while (opModeIsActive() && targetPos == IconTypeN.NA && runtime.seconds() < 10.0) {
            //detect icon
            targetPos = detectObject();
            sleep(20);

        }

        telemetry.addData("TargetPos", targetPos);
        telemetry.update();

        Elevator(ELE_COL);
        sleep(50);

        Move(-60, 0.8, Direction.LEFT_RIGHT, 2);
        Move(70, 0.8, Direction.FRONT_BACK, 3);
        Elevator(ELE_TOP);
        Move(-41, 0.8, Direction.LEFT_RIGHT, 2);
        Move(8, 0.8, Direction.FRONT_BACK, 2);
        sleep(400);

        Claw(false);
        sleep(400);

        Move(-15, 0.8, Direction.FRONT_BACK, 1);

        //move to stop position
        switch (targetPos) {
            case ONE:
                Move(160, 0.8, Direction.LEFT_RIGHT, 3);

                break;

            case TWO:
                Move(100, 0.8, Direction.LEFT_RIGHT, 3);

                break;

            case THREE:
                Move(40, 0.8, Direction.LEFT_RIGHT, 3);

                break;

            case NA:
                Move(100, 0.8, Direction.LEFT_RIGHT, 3);

                break;

        }

        sleep(150);
        Elevator(ELE_MIN_POS);
        sleep(2000);


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
        tFod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);

    }

    private IconTypeN detectObject() {
        List<Recognition> updatedRecognitions = tFod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            for (Recognition recognition : updatedRecognitions) {
                switch (recognition.getLabel()) {
                    case "one":
                        return IconTypeN.ONE;

                    case "two":
                        return IconTypeN.TWO;

                    case "three":
                        return IconTypeN.THREE;

                    default:
                        break;

                }

            }
        }
        return IconTypeN.NA;

    }

    /**
     * Move the robot
     * axle (by power and direction): y+ being front, y- being back, x+ being left, x- being right
     * side moving alex: front direction being +
     * Unit: centimeter (cm)
     *
     * @param distance
     * @param power
     * @param direction FRONT_BACK, LEFT_RIGHT, SIDE_LEFT_RIGHT (left front to right bottom direction), SIDE_RIGHT_LEF T(right front to left bottom direction)
     * @param timeout
     */
    private void Move(double distance, double power, Direction direction, double timeout) {
        distance *= 17.5;
        double lfrbModify = 0;
        double rflbModify = 0;
        //different cases for different direction
        switch (direction) {
            case FRONT_BACK:
                lfrbModify = 1.0;
                rflbModify = 1.0;

                break;

            case LEFT_RIGHT:
                lfrbModify = -1.0;
                rflbModify = 1.0;

                break;

            case SIDE_FRONT_LEFT:
                lfrbModify = 0;
                rflbModify = 1.0;
                // 1 / sin45
                distance *= 1.414;

                break;

            case SIDE_FRONT_RIGHT:
                lfrbModify = 1.0;
                rflbModify = 0;
                // 1 / sin45
                distance *= 1.414;

                break;

            default:
                telemetry.addData("ERROR", "In Move()");
                telemetry.update();

                sleep(50);

        }

        //group 1
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() - (int) (distance * lfrbModify));
        rightBotMotor.setTargetPosition(rightBotMotor.getCurrentPosition() + (int) (distance * lfrbModify));

        //group 2
        leftBotMotor.setTargetPosition(leftBotMotor.getCurrentPosition() - (int) (distance * rflbModify));
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + (int) (distance * rflbModify));

        //reset the time and set the power
        runtime.reset();
        leftFrontMotor.setPower(-power);
        rightBotMotor.setPower(power);
        leftBotMotor.setPower(-power);
        rightFrontMotor.setPower(power);

        //start the motors
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBotMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBotMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive()
                && runtime.seconds() < timeout
                && (leftFrontMotor.isBusy() || rightBotMotor.isBusy() || leftBotMotor.isBusy() || rightFrontMotor.isBusy())) {
            //holding the program till timeout or to target position

        }

        //stop and reset motors
        leftFrontMotor.setPower(0);
        rightBotMotor.setPower(0);
        leftBotMotor.setPower(0);
        rightFrontMotor.setPower(0);

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBotMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBotMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //cool down for the next function
        sleep(250);

    }

    static void Elevator(double level) {
        eleMotor.setTargetPosition((int) level);
        eleMotor.setPower(1);
        eleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    static void Claw(boolean state) {
        if (!state) collectorServo.setPosition(COLLECT_MIN_POS);
        else collectorServo.setPosition(COLLECT_MAX_POS);

    }

}