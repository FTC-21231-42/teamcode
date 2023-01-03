package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotMap.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "DriveControl_new", group = "1")
public class DriveControl_3 extends OpMode {
    //define variables
    double ovaPowerModify = 1.0;
    double basePower = 0;
    double leftFrontPower = 0, rightFrontPower = 0, leftBotPower = 0, rightBotPower = 0;

    float stickRightX1, stickRightY1, stickLeftX1, stickLeftY1;

    int elePos = 0;
    int eleTargetPos = 0;
    boolean eleDowning = false;

    ElapsedTime runtimeCol = new ElapsedTime();
    ElapsedTime runtimeEle = new ElapsedTime();

    @Override
    public void init() {
        //init hardware
        initRobot(hardwareMap);

        //update log
        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }

    @Override
    public void loop() {
        //main

        //get the input from the game pad
//        stickLeftX1 = this.gamepad1.right_stick_x;
//        stickLeftY1 = this.gamepad1.right_stick_y;
//        stickRightX1 = this.gamepad1.left_stick_x;
//        stickRightY1 = this.gamepad1.left_stick_y;

        stickRightX1 = this.gamepad1.right_stick_x;
        stickRightY1 = this.gamepad1.right_stick_y;
        stickLeftX1 = this.gamepad1.left_stick_x;
        stickLeftY1 = this.gamepad1.left_stick_y;

        //use left trigger 1 and right trigger 1 to control the speed, push to set faster
        basePower = (((gamepad1.left_trigger + gamepad1.right_trigger) * 0.325) + 0.35) * ovaPowerModify;
        basePower *= basePower;

        //set the power of motor
        //FINAL POWER = power * modify

        leftFrontPower = (-stickLeftX1 + (stickRightY1 - stickRightX1)) * basePower;
        rightFrontPower = (stickLeftX1 + (stickRightY1 + stickRightX1)) * basePower;
        leftBotPower = (-stickLeftX1 + (stickRightY1 + stickRightX1)) * basePower;
        rightBotPower = (stickLeftX1 + (stickRightY1 - stickRightX1)) * basePower;

        leftFrontMotor.setPower(leftFrontPower);
        rightFrontMotor.setPower(-rightFrontPower);
        leftBotMotor.setPower(leftBotPower);
        rightBotMotor.setPower(-rightBotPower);
        //end of driving

//        elePos = eleMotor.getCurrentPosition();

        //elevator
        if (gamepad2.a && runtimeCol.seconds() >= 0.2) {
            if (eleDowning) {
//                eleTargetPos = 0;
                Elevator(0);
                eleDowning = false;

            } else {
//                eleTargetPos = ELE_COL;
                Elevator(ELE_COL);
                eleDowning = true;

            }

            runtimeCol.reset();

        }
//        if (gamepad2.b) eleTargetPos = ELE_BOT;
//        if (gamepad2.x) eleTargetPos = ELE_MID;
//        if (gamepad2.y) eleTargetPos = ELE_TOP;

        if (gamepad2.b) Elevator(ELE_BOT);
        if (gamepad2.x) Elevator(ELE_MID);;
        if (gamepad2.y) Elevator(ELE_TOP);;

//        eleMotor.setTargetPosition(eleTargetPos);
//        eleMotor.setPower(1);
//        eleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //collector
        if (gamepad2.right_trigger > 0.1 && runtimeCol.seconds() >= 0.5) {
            if (COLLECT_CURRENT) {
                collectorServo.setPosition(COLLECT_MAX_POS);
                COLLECT_CURRENT = false;

            } else {
                collectorServo.setPosition(COLLECT_MIN_POS);
                COLLECT_CURRENT = true;

            }
            //if last press was too close, won't do anything
            runtimeCol.reset();

        }

        //support arm
        if (gamepad2.left_trigger > 0.1) armServo.setPosition(ARM_CLOSE);
        else armServo.setPosition(ARM_OPEN);

        //logging
        telemetry.addData("stickRightX1", stickRightX1);
        telemetry.addData("stickRightY1", stickRightY1);
        telemetry.addData("left_stick_x;", gamepad1.left_stick_x);
        telemetry.addData("left_stick_y;", gamepad1.left_stick_y);
        telemetry.addData("lTrigger", gamepad1.left_trigger);
        telemetry.addData("rTrigger", gamepad1.right_trigger);
        telemetry.addData("bPower", basePower);
        telemetry.addData("elePos", elePos);
        telemetry.update();

    }

    @Override
    public void stop() {
        super.stop();
    }

}
