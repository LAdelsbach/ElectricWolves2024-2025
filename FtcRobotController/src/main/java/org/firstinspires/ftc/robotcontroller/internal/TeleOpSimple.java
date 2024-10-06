package org.firstinspires.ftc.robotcontroller.internal;

import org.firstinspires.ftc.robotcontroller.internal.subsystems.Claw;
import org.firstinspires.ftc.robotcontroller.internal.subsystems.Drivetrain;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;



@TeleOp(name = "TeleOp_Simple", group = "Iterative Opmode")

public class TeleOpSimple extends OpMode{
    DcMotor motor_fl;
    DcMotor motor_fr;
    DcMotor motor_bl;
    DcMotor motor_br;
    DcMotor linear_arm;
    Servo claw_left;
    Servo claw_right;
    Claw cl;
    Drivetrain drivetrain;
    public TeleOpSimple(){
        super();
    }
    public void init() {
        //motors needs to be configured on the controller phones exactly as they are here to work
        //the following code is used to map our motor variables to the physical motors
        motor_fl = hardwareMap.dcMotor.get("FL");
        motor_fr = hardwareMap.dcMotor.get("FR");
        motor_bl = hardwareMap.dcMotor.get("BL");
        motor_br = hardwareMap.dcMotor.get("BR");

        claw_left = hardwareMap.get(Servo.class, "cl_left");
        claw_right = hardwareMap.get(Servo.class, "cl_right");


        //the following code is used to set the initial directions of the motors.
        //normally, the motors on the left should be set to Direction.REVERSE,
        //but testing should resolve any issues with motor directions.
        motor_fl.setDirection(DcMotorSimple.Direction.FORWARD);
        motor_fr.setDirection(DcMotorSimple.Direction.REVERSE);
        motor_bl.setDirection(DcMotorSimple.Direction.FORWARD);
        motor_br.setDirection(DcMotorSimple.Direction.REVERSE);
        //TODO: check these bottom ones to see if they need to be switched

        cl = new Claw(claw_left, claw_right);
        drivetrain = new Drivetrain(motor_fl, motor_fr, motor_bl, motor_br);



        //taking out a line of code that says hang.setDirection(DcMotorSimple.Direction.FORWARD);
        //hang.setDirection(DcMotorSimple.Direction.FORWARD);


    }

    public void loop() {
        control_robot();
        report_telemetry();
    }
    private void control_robot() {
        movement();
        game_specific();
    }
    private void movement(){

        if(!gamepad1.a) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            drivetrain.drive(x,y,rx, 1.1);
        }


    }
    public void game_specific(){
        
    }

    public void report_telemetry() {

        //Drive Motors Power STatus



        telemetry.addData("Front Left", motor_fl.getPower());
        telemetry.addData("Front Right", motor_fr.getPower());
        telemetry.addData("Back Left", motor_bl.getPower());
        telemetry.addData("Back Right", motor_br.getPower());


        telemetry.update();
    }
    private void resetMotors() {
        motor_fl.setPower(0.0);
        motor_fr.setPower(0.0);
        motor_bl.setPower(0.0);
        motor_br.setPower(0.0);
    }

}

