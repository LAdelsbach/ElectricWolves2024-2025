package org.firstinspires.ftc.robotcontroller.internal;



import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "OLD TeleOp", group = "Iterative Opmode")

public class OLDTeleOp extends OpMode{
    DcMotor motorFL;
    DcMotor motorFR;
    DcMotor motorBL;
    DcMotor motorBR;
    DcMotor intakeSpin;
    DcMotor lift;
    DcMotor hang;
    TouchSensor touch;

    DistanceSensor left;

    DistanceSensor right;

    Servo reference;
    Servo reverse;

    Servo clamp;
    Servo clawRotate;
    double xo;
    double deltaY;
    double angleOfArm;

    int disableTouchSensor;
    int manualClaw;

    public OLDTeleOp(){
        super();
    }
    public void init() {
        //motors needs to be configured on the controller phones exactly as they are here to work
        //the following code is used to map our motor variables to the physical motors
        motorFL = hardwareMap.dcMotor.get("FL");
        motorFR = hardwareMap.dcMotor.get("FR");
        motorBL = hardwareMap.dcMotor.get("BL");
        motorBR = hardwareMap.dcMotor.get("BR");
        //
        intakeSpin = hardwareMap.dcMotor.get("hang");
        lift = hardwareMap.dcMotor.get("lift");
//        hang = hardwareMap.dcMotor.get("hang");

        //
        touch = hardwareMap.get(TouchSensor.class, "Touch");
        //
        reverse = hardwareMap.get(Servo.class, "reference");
        reference = hardwareMap.get(Servo.class, "reverse");
        //

        clawRotate = hardwareMap.get(Servo.class, "clawRotate");
        left = hardwareMap.get(DistanceSensor.class, "left");
        right = hardwareMap.get(DistanceSensor.class, "right");

        //the following code is used to set the initial directions of the motors.
        //normally, the motors on the left should be set to Direction.REVERSE,
        //but testing should resolve any issues with motor directions.
        motorFL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBL.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBR.setDirection(DcMotorSimple.Direction.REVERSE);
        //TODO: check these bottom ones to see if they need to be switched
        intakeSpin.setDirection(DcMotorSimple.Direction.FORWARD);
        lift.setDirection(DcMotorSimple.Direction.FORWARD);

        //taking out a line of code that says hang.setDirection(DcMotorSimple.Direction.FORWARD);
        //hang.setDirection(DcMotorSimple.Direction.FORWARD);

        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        reference.setDirection(Servo.Direction.FORWARD);
        reverse.setDirection(Servo.Direction.FORWARD);
        clawRotate.setDirection(Servo.Direction.FORWARD);

        //TODO: Set angle of Arm to actual value
        angleOfArm = 45;

        disableTouchSensor = 1;
        manualClaw = 1;
    }

    public void loop() {
        controlRobot();
        reportTelemetry();
    }
    private void controlRobot() {
        movement();
        //TODO: Make this turn on and off using right bumber
        gameSpecific();
    }
    private void movement(){

        if(!gamepad1.a) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFL.setPower(frontLeftPower);
            motorBL.setPower(backLeftPower);
            motorFR.setPower(frontRightPower);
            motorBR.setPower(backRightPower);
        }
        else{

            deltaY = left.getDistance(DistanceUnit.CM) - right.getDistance(DistanceUnit.CM);
            deltaY = Math.abs(deltaY);
            boolean isRight = false;
            double smallY = left.getDistance(DistanceUnit.CM);
            if(smallY > right.getDistance(DistanceUnit.CM)){
                smallY = right.getDistance(DistanceUnit.CM);
                isRight = true;
            }
            double theta = Math.atan(xo/deltaY);
            double fl = 1;
            double fr = 1;
            double bl = 1;
            double br = 1;
            int distanceFromWall = 6;
            if(smallY > 26){
                fl = (smallY-distanceFromWall)/smallY;
                fr = (smallY-distanceFromWall)/smallY;
                bl = (smallY-distanceFromWall)/smallY;
                br = (smallY-distanceFromWall)/smallY;
            }
            if(deltaY>0){
                if(isRight){
                    if(br + deltaY >= 1 || fr + deltaY >= 1){
                        if(deltaY > 0.3){
                            fl -= 0.3;
                            bl -= 0.3;
                        }
                        else{
                            fl -= deltaY;
                            bl -= deltaY;
                        }
                    }
                    else{
                        br +=  deltaY;
                        fr += deltaY;
                    }
                }
            }
        }
    }
    private void gameSpecific(){

        if(manualClaw == 1 && gamepad2.back){
            manualClaw ++;
        }
        if(manualClaw == 2 && !gamepad2.back){
            manualClaw ++;
        }//claw is equal to three --- of position
        if(manualClaw == 3 && gamepad2.back){
            manualClaw ++;
        }
        if(manualClaw == 4 && !gamepad2.back){
            manualClaw =1;
        }

        if(disableTouchSensor == 1 && gamepad2.left_bumper){
            disableTouchSensor ++;
        }
        else if(disableTouchSensor == 2 && !gamepad2.left_bumper){
            disableTouchSensor ++;
        }
        else if(disableTouchSensor == 3 && gamepad2.left_bumper){
            disableTouchSensor ++;
        }
        else if(disableTouchSensor == 4 && gamepad2.left_bumper){
            disableTouchSensor = 1;
        }
        //touch sensor disabled
        if(disableTouchSensor == 1){
            if(gamepad2.dpad_up){
                intakeSpin.setPower(0.7);
            }
            if(gamepad2.dpad_down){
                intakeSpin.setPower(-0.7);
            }
            lift.setPower(gamepad2.left_stick_y);
            if (gamepad2.dpad_down) {
                intakeSpin.setPower(0.7);
            }
            else if (gamepad2.dpad_up) {
                intakeSpin.setPower(-0.7);
            }
            else{
                intakeSpin.setPower(-0.1);
            }

        }
        //touch sensor is enabled
        else {
            if(touch.isPressed()){
                if(gamepad2.left_stick_y > 0){
                    lift.setPower(gamepad2.left_stick_y);
                }
            }
            else{
                lift.setPower(gamepad2.left_stick_y);
            }
            if (touch.isPressed() && gamepad2.dpad_down) {
                intakeSpin.setPower(0.7);
            }

            else if (touch.isPressed() && gamepad2.dpad_up) {
                intakeSpin.setPower(-0.7);
            }
            else{
                intakeSpin.setPower(-0.1);
            }
        }
        //Hang
        if(gamepad2.left_bumper && gamepad2.dpad_up){
            hang.setPower(1);
        }
        else if(gamepad2.left_bumper && gamepad2.dpad_down){
            hang.setPower(-0.7);
        }


        if(Math.abs(gamepad2.right_stick_y) < 0.05) {
            //5 stack (like in valorant)
            if (gamepad2.y) {
                reference.setPosition(0.75);
                reverse.setPosition(0.25);
            }
            //4 stack
            if (gamepad2.b) {
                reference.setPosition(0.65);
                reverse.setPosition(0.45);
            }
            //3 stack
            if (gamepad2.a) {
                reference.setPosition(0.5);
                reverse.setPosition(0.5);
            }
            //two stack
            if (gamepad2.x) {
                reference.setPosition(0.45);
                reverse.setPosition(0.65);

            }
            //off the floor
            if (gamepad2.right_bumper) {
                reference.setPosition(0);
                reverse.setPosition(1);
            }
        }
        else{
            reference.setPosition(0.25 - gamepad2.right_stick_y/4);
            reverse.setPosition(0.75 + gamepad2.right_stick_y/4);
        }
        if(manualClaw == 1) {
            //TODO: CHANGE THE NUMBER TO WHERE YOU WANT IT TO BE HOMED
            if (lift.getCurrentPosition() < 100) {
                homeClaw();

            } else {
                placementClaw();
            }
        }
        else {
            if (gamepad2.dpad_left) {
                homeClaw();
            } else if (gamepad2.dpad_right) {
                placementClaw();
            } else if (gamepad2.right_trigger + gamepad2.left_trigger > 0) {
                manualClaw();
            }
        }

    }

    public void manualClaw(){
        if(clawRotate.getPosition() + gamepad2.right_trigger - gamepad2.left_trigger >= 0){
            if(clawRotate.getPosition() + gamepad2.right_trigger - gamepad2.left_trigger <= 1){
                clawRotate.setPosition(clawRotate.getPosition() + gamepad2.right_trigger - gamepad2.left_trigger);
            }
            else{
                clawRotate.setPosition(1);
            }
        }
        else{
            clawRotate.setPosition(0);
        }

    }

    //TODO: set the right home position
    public void homeClaw(){
        clawRotate.setPosition(0);
    }
    //TODO: set the right placement position

    public void placementClaw(){
        clawRotate.setPosition((210-angleOfArm)/180);
    }

    public void reportTelemetry() {

        //Drive Motors Power STatus
        telemetry.addData("Left:", left.getDistance(DistanceUnit.CM));
        telemetry.addData("Right:", right.getDistance(DistanceUnit.CM));


        telemetry.addData("Front Left", motorFL.getPower());
        telemetry.addData("Front Right", motorFR.getPower());
        telemetry.addData("Back Left", motorBL.getPower());
        telemetry.addData("Back Right", motorBR.getPower());

        //Arm Height
        telemetry.addData("Lift Height", lift.getCurrentPosition());

        //intake Angle
        telemetry.addData("Reference Intake Servo", reference.getPosition());
        telemetry.addData("Reverse Intake Servo", reverse.getPosition());
        //Claw Servo Information
        telemetry.addData("Claw Rotate", clawRotate.getPosition());
        //Touch Sensor Information
        telemetry.addData("Touch Sensor", touch.isPressed());

        telemetry.update();
    }
    private void resetMotors() {
        motorFL.setPower(0.0);
        motorFR.setPower(0.0);
        motorBL.setPower(0.0);
        motorBR.setPower(0.0);
    }

}

