package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.RobotMap.*;

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

@Autonomous(name = "Auto_0", group = "0")
public class Auto_0 extends LinearOpMode {
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
            tFod.setZoom(1.2, 16.0 / 9.0);
        }

        Claw(true);
        colExtendServo.setPower(-0.8);
        sleep(1250);
        colExtendServo.setPower(0);

        telemetry.addData("Initialized", "True");
        telemetry.update();
        waitForStart();

        //if can see anything after 5 seconds
        runtime.reset();
        while (opModeIsActive() && targetPos == IconTypeN.NA && runtime.seconds() < 8.0) {
            //detect icon
            targetPos = detectObject();
            sleep(20);

        }

        telemetry.addData("TargetPos", targetPos);
        telemetry.update();

//        sleep(50000);

        Elevator(ELE_COL);
        sleep(50);

        Move(90, 0.35, Direction.LEFT_RIGHT, 4);
//        Move(-10, 0.6, Direction.FRONT_BACK, 1);
        Move(7, 0.75, Direction.FRONT_BACK, 1.5);

        Move(-153, 0.5, Direction.LEFT_RIGHT, 3);
        Move(52, 0.75, Direction.FRONT_BACK, 3);
        Elevator(ELE_TOP);
        sleep(500);
        Move(-42, 0.75, Direction.LEFT_RIGHT, 2);
        Move(10, 0.75, Direction.FRONT_BACK, 2);
        sleep(550);

        Claw(false);
        sleep(400);

        Move(-13, 0.75, Direction.FRONT_BACK, 1);

        //move to stop position
        switch (targetPos) {
            case ONE:
                Move(48, 0.75, Direction.LEFT_RIGHT, 2);

                break;

            case TWO:
                Move(87, 0.75, Direction.LEFT_RIGHT, 2);
                Move(10, 0.75, Direction.FRONT_BACK, 1);
                Move(30, 0.75, Direction.LEFT_RIGHT, 1);

                break;

            case THREE:
                Move(100, 0.75, Direction.LEFT_RIGHT, 2);
                Move(10, 0.75, Direction.FRONT_BACK, 1);
                Move(95, 0.75, Direction.LEFT_RIGHT, 2);

                break;

            case NA:
                Move(120, 0.75, Direction.LEFT_RIGHT, 2);

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
        tFod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);

    }

    /**
     * Move the robot (Global Motor Position Version)
     * axle (by power and direction): y+ being front, y- being back, x+ being left, x- being right
     * side moving alex: front direction being +
     * Unit: centimeter (cm)
     *
     * @param distance
     * @param power
     * @param direction FRONT_BACK, LEFT_RIGHT, SIDE_LEFT_RIGHT (left front to right bottom direction), SIDE_RIGHT_LEF T(right front to left bottom direction)
     * @param timeout
     */
    /*
    private void Move(double distance, double power, Direction direction, double timeout) {
        //wheel constance
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

        lfPos -= (int) (distance * lfrbModify);
        rbPos += (int) (distance * lfrbModify);

        lbPos -= (int) (distance * rflbModify);
        rfPos += (int) (distance * rflbModify);

        //group 1
        leftFrontMotor.setTargetPosition(lfPos);
        rightBotMotor.setTargetPosition(rbPos);

        //group 2
        leftBotMotor.setTargetPosition(lbPos);
        rightFrontMotor.setTargetPosition(rfPos);

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

    */

    /**
     * Move the robot (old)
     * axle (by power and direction): y+ being front, y- being back, x+ being right, x- being left
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

}
