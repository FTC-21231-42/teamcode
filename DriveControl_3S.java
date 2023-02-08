package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotMap.COLLECT_CURRENT;
import static org.firstinspires.ftc.teamcode.RobotMap.COLLECT_MAX_POS;
import static org.firstinspires.ftc.teamcode.RobotMap.COLLECT_MIN_POS;
import static org.firstinspires.ftc.teamcode.RobotMap.ELE_BOT;
import static org.firstinspires.ftc.teamcode.RobotMap.ELE_COL;
import static org.firstinspires.ftc.teamcode.RobotMap.ELE_MID;
import static org.firstinspires.ftc.teamcode.RobotMap.ELE_TOP;
import static org.firstinspires.ftc.teamcode.RobotMap.Elevator;
import static org.firstinspires.ftc.teamcode.RobotMap.colExtendServo;
import static org.firstinspires.ftc.teamcode.RobotMap.collectorServo;
import static org.firstinspires.ftc.teamcode.RobotMap.initRobot;
import static org.firstinspires.ftc.teamcode.RobotMap.leftBotMotor;
import static org.firstinspires.ftc.teamcode.RobotMap.leftFrontMotor;
import static org.firstinspires.ftc.teamcode.RobotMap.rightBotMotor;
import static org.firstinspires.ftc.teamcode.RobotMap.rightFrontMotor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "DriveControl_new_simple", group = "1")
public class DriveControl_3S extends OpMode {
    //define variables
    double ovaPowerModify = 1.0;
    double basePower = 1.0;
    double leftFrontPower = 0, rightFrontPower = 0, leftBotPower = 0, rightBotPower = 0;

    float stickRightX1, stickRightY1, stickLeftX1, stickLeftY1;

    int elePos = 0;
    int eleTargetPos = 0;
    boolean eleDowning = false;

    ElapsedTime runtimeCol = new ElapsedTime();
    ElapsedTime runtimeEle = new ElapsedTime();

    double timeLast;
    double level = 0;

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
//        basePower = (((gamepad1.left_trigger + gamepad1.right_trigger) * 0.275) + 0.45) * ovaPowerModify;
//        basePower *= basePower;

        if (this.gamepad1.x) basePower = 0.5;
        else if (this.gamepad1.a) basePower = 0.75;
        else if (this.gamepad1.y) basePower = 0.9;
        else if (this.gamepad1.b) basePower = 1.0;

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

//        elePos = eleMotor.getCurrentPosition();

        //elevator button control
//        /*
        if (gamepad2.a && runtimeCol.seconds() >= 0.15) {
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
        if (gamepad2.b) Elevator(ELE_BOT);
        if (gamepad2.x) Elevator(ELE_MID);
        if (gamepad2.y) Elevator(ELE_TOP);

//        */

        //elevator stick control
        /*
        level -= gamepad2.left_stick_y * (runtimeEle.seconds() - timeLast);
        if (level < 0) level = 0;
        if (level > ELE_MAX_POS) level = ELE_MAX_POS;
        Elevator(level);
        timeLast = runtimeEle.seconds();

         */

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

//        //support arm
//        if (gamepad2.left_trigger > 0.1) armServo.setPosition(ARM_CLOSE);
//        else armServo.setPosition(ARM_OPEN);

        //col extend
        if (gamepad2.dpad_up) colExtendServo.setPower(-0.8);
        else if (gamepad2.dpad_down) colExtendServo.setPower(0.8);
        else colExtendServo.setPower(0);

        //logging
//        telemetry.addData("stickRightX1", stickRightX1);
//        telemetry.addData("stickRightY1", stickRightY1);
//        telemetry.addData("left_stick_x;", gamepad1.left_stick_x);
//        telemetry.addData("left_stick_y;", gamepad1.left_stick_y);
//        telemetry.addData("lTrigger", gamepad1.left_trigger);
//        telemetry.addData("rTrigger", gamepad1.right_trigger);
        telemetry.addData("bPower", basePower);
        telemetry.addData("elePos", elePos);
        telemetry.update();

    }

    @Override
    public void stop() {
        super.stop();
    }

}
