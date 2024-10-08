package org.firstinspires.ftc.robotcontroller.internal;

import org.firstinspires.ftc.robotcontroller.internal.subsystems.Claw;
import org.firstinspires.ftc.robotcontroller.internal.subsystems.Drivetrain;
import org.firstinspires.ftc.robotcontroller.internal.subsystems.Elbow;
import org.firstinspires.ftc.robotcontroller.internal.subsystems.LinearRail;

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
    Servo elbow_servo;
    Claw claw;
    Drivetrain drivetrain;
    Elbow elbow;
    LinearRail linear_rail;
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
        elbow_servo = hardwareMap.get(Servo.class, "elbow");

        linear_arm = hardwareMap.dcMotor.get("la");

        //TODO: check these ones to see if they need to be switched
        motor_fl.setDirection(DcMotorSimple.Direction.FORWARD);
        motor_fr.setDirection(DcMotorSimple.Direction.REVERSE);
        motor_bl.setDirection(DcMotorSimple.Direction.FORWARD);
        motor_br.setDirection(DcMotorSimple.Direction.REVERSE);
        //TODO: check these, remember that these have to be flipped from one another
        claw_left.setDirection(Servo.Direction.FORWARD);
        claw_right.setDirection(Servo.Direction.REVERSE);

        linear_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linear_arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        claw = new Claw(claw_left, claw_right);
        drivetrain = new Drivetrain(motor_fl, motor_fr, motor_bl, motor_br);
        elbow = new Elbow(elbow_servo);

        LinearRail linear_rail = new LinearRail(linear_arm);

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
        double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepad1.left_stick_x; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;
        //This if statement is just because sometimes the controller is slightly triggered
        //In that case, if not purposefull, you do not want to go at 95% speed for no reason
        if(gamepad1.right_trigger < 0.05) {
            drivetrain.drive(x,y,rx, 1.1);
        }
        else{
            drivetrain.drive(x,y,rx,1.1, (1-gamepad1.right_trigger));
        }
    }
    public void game_specific(){
        //Control of the claw
        if(gamepad2.a){
            claw.cl_close();
            elbow.slight_up(); //only time claw is closing is if something is being picked up
        }
        else if(gamepad2.y){
            claw.cl_open();
        }
        else if(Math.abs(gamepad2.right_stick_y) > 0.05){
            claw.increment(gamepad2.right_stick_y);
        }
        if(gamepad2.dpad_down){
            elbow.down();
        }
        else if(gamepad2.dpad_up){
            elbow.up();
        }
        linear_rail.drive(gamepad2.left_stick_y);
    }

    public void report_telemetry() {
        telemetry.addData("Front Left", motor_fl.getPower());
        telemetry.addData("Front Right", motor_fr.getPower());
        telemetry.addData("Back Left", motor_bl.getPower());
        telemetry.addData("Back Right", motor_br.getPower());
        telemetry.update();
    }
}