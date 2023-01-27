package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotMap.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Testing", group = "1")
public class DriveControl_Test extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        //init hardware
        initRobot(hardwareMap);

        //define variables
        double ovaPowerModify = 0.35;
        double basePower = 0;
        double leftFrontPower = 0, rightFrontPower = 0, leftBotPower = 0, rightBotPower = 0;

        int elePos = 0;
        double collectorPos = 0;
        int eleTargetPos = 0;

        float stickRightX1, stickRightY1;
        float fixedStickRightX1, fixedStickRightY1;
        boolean x, y;
        boolean x0, y0;

        ElapsedTime runtime = new ElapsedTime();

        //update log
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        while (opModeIsActive()) {
            //main

            //get the input from the game pad
            stickRightX1 = this.gamepad1.right_stick_x;
            stickRightY1 = this.gamepad1.right_stick_y;

            /*
            //using 0.05 but not 0
            //for stability and error-tolerant
            if (Math.abs(stickRightX1) > 0.05){
                fixedStickRightX1 = stickRightX1;
                x0 = false;

            } else {
                fixedStickRightX1 = 0;
                x0 = true;

            }

            if (Math.abs(stickRightY1) > 0.05){
                fixedStickRightY1 = stickRightY1;
                y0 = false;

            } else {
                fixedStickRightY1 = 0;
                y0 = true;

            }

            //moving logic part
            //boolean operation
            y = fixedStickRightY1 < 0;
            x = fixedStickRightX1 < 0;
            basePower = (Math.abs(fixedStickRightX1) + Math.abs(fixedStickRightY1)) * (1 + gamepad1.left_trigger);

//            rotateAble = false;
            if ((y && x0) || (!x && y0) || (y && !x)){
                leftFrontPower = basePower;
                rightBotPower = basePower;

            } else if ((!y && x0) || (x && y0) || (!y && x)){
                leftFrontPower = -basePower;
                rightBotPower = -basePower;

            } else {
                leftFrontPower = 0;
                rightBotPower = 0;
//                rotateAble = true;

            }

            if ((y && x0) || (x && y0) || (y && x)){
                rightFrontPower = basePower;
                leftBotPower = basePower;

            } else if ((!y && x0) || (!x && y0) || (!y && !x)){
                rightFrontPower = -basePower;
                leftBotPower = -basePower;

            } else {
                rightFrontPower = 0;
                leftBotPower = 0;
//                rotateAble = true;

            }

            if (Math.abs(gamepad1.left_stick_x) > 0.1){
                leftFrontPower = 0.5 * gamepad1.left_stick_x;
                leftBotPower = 0.5 * gamepad1.left_stick_x;
                rightFrontPower = -0.5 * gamepad1.left_stick_x;
                rightBotPower = -0.5 * gamepad1.left_stick_x;

            }

            //set the power of motor
            //FINAL POWER = power * modify
            leftFrontMotor.setPower(-leftFrontPower * ovaPowerModify);
            rightFrontMotor.setPower(rightFrontPower * ovaPowerModify);
            leftBotMotor.setPower(-leftBotPower * ovaPowerModify);
            rightBotMotor.setPower(rightBotPower * ovaPowerModify);

             */


            //elevator
            elePos = eleMotor.getCurrentPosition();
//            if (gamepad2.left_stick_y < -0.1) {
            if (elePos < ELE_MAX_POS && gamepad2.left_stick_y < -0.1) {
                //up
                eleMotor.setPower(0.8);

//            } else if (gamepad2.left_stick_y > 0.1) {
            } else if (elePos > ELE_MIN_POS && gamepad2.left_stick_y > 0.1) {
                //down
                eleMotor.setPower(-0.6);

            } else {
                //hold
                eleMotor.setPower(0);

            }



//            elePos = eleMotor.getCurrentPosition();
//
//            if (gamepad2.a) eleTargetPos = 0;
//            if (gamepad2.b) eleTargetPos = ELE_BOT;
//            if (gamepad2.x) eleTargetPos = ELE_MID;
//            if (gamepad2.y) eleTargetPos = ELE_TOP;
//
//            eleMotor.setTargetPosition(eleTargetPos);
//            eleMotor.setPower(1);
//            eleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //collector
            collectorPos = collectorServo.getPosition();
            if (gamepad1.x) {
                //open
                collectorServo.setPosition(collectorPos + 0.01);
                sleep(100);

            } else if (gamepad1.y) {
                //close
                collectorServo.setPosition(collectorPos - 0.01);
                sleep(100);

            }

//            if (gamepad1.a) {
//                //open
//                armServo.setPosition(armServo.getPosition() + 0.01);
//                sleep(100);
//
//            } else if (gamepad1.b) {
//                //close
//                armServo.setPosition(armServo.getPosition() - 0.01);
//                sleep(100);
//
//            }

            if (gamepad1.left_trigger > 0.1) armServo.setPosition(ARM_CLOSE);
            else armServo.setPosition(ARM_OPEN);


            //one button open and close
            if (gamepad1.right_trigger > 0.1 && runtime.seconds() >= 0.5) {
                if (COLLECT_CURRENT) {
                    collectorServo.setPosition(COLLECT_MAX_POS);
                    COLLECT_CURRENT = false;

                } else {
                    collectorServo.setPosition(COLLECT_MIN_POS);
                    COLLECT_CURRENT = true;

                }
                //if last press was too close, won't do anything
                runtime.reset();

            }

            //col extend
            if (gamepad2.dpad_up) colExtendServo.setPower(0.8);
            else if (gamepad2.dpad_down) colExtendServo.setPower(-0.8);
            else colExtendServo.setPower(0);


            //logging
            telemetry.addData("stickRightX1", stickRightX1);
            telemetry.addData("stickRightY1", stickRightY1);
            telemetry.addData("left_stick_x;", gamepad1.left_stick_x);
            telemetry.addData("lTrigger", gamepad1.left_trigger);
            telemetry.addData("rTrigger", gamepad1.right_stick_x);
            telemetry.addData("left_stick_y2", gamepad2.left_stick_y);
            telemetry.addData("bPower", basePower);
            telemetry.addData("elePos", elePos);
            telemetry.addData("armPos", armServo.getPosition());
            telemetry.addData("collectorPos", collectorPos);
            telemetry.addData("gamepad2.dpad_up", gamepad2.dpad_up);
            telemetry.addData("gamepad2.dpad_down", gamepad2.dpad_down);
            telemetry.update();

        }
    }

}
