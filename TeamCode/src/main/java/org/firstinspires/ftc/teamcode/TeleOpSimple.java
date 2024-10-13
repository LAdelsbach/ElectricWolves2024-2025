package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Elbow;
import org.firstinspires.ftc.teamcode.subsystems.LinearRail;

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
    Claw claw;
    Drivetrain drivetrain;
    LinearRail linear_rail;
    Servo elbow;

    double pos;
    double max = 0.45;
    double min = 5;
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
        linear_arm = hardwareMap.dcMotor.get("la");

        claw_left = hardwareMap.get(Servo.class, "cl_left");
        claw_right = hardwareMap.get(Servo.class, "cl_right");
        elbow = hardwareMap.get(Servo.class, "elbow");


        motor_fl.setDirection(DcMotorSimple.Direction.REVERSE);
        motor_fr.setDirection(DcMotorSimple.Direction.FORWARD);
        motor_bl.setDirection(DcMotorSimple.Direction.REVERSE);
        motor_br.setDirection(DcMotorSimple.Direction.FORWARD);

        //TODO: check these, remember that these have to be flipped from one another

        claw_left.setDirection(Servo.Direction.FORWARD);
        claw_right.setDirection(Servo.Direction.REVERSE);
        linear_arm.setDirection(DcMotorSimple.Direction.REVERSE);

        drivetrain = new Drivetrain(motor_fl, motor_fr, motor_bl, motor_br);
        pos = elbow.getPosition();

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
    public void game_specific() {
        //linear_rail.drive(gamepad2.left_stick_y);
        linear_arm.setPower(gamepad2.left_stick_y);
        //Control of the claw
        if (gamepad1.a) {
            double pos = 0;
            claw_left.setPosition(pos);
            claw_right.setPosition(pos);
            //elbow.slight_up(); //only time claw is closing is if something is being picked up
        } else if (gamepad1.y) {
            double pos = 0.25;
            claw_left.setPosition(pos);
            claw_right.setPosition(pos);
        } else if (Math.abs(gamepad2.right_stick_y) > 0.05) {
            double i = gamepad2.right_stick_y / 90;
            double pos = claw_left.getPosition();
            if (i + pos <= 0.25 && i + pos >= 0) {
                pos += i;
            } else if (pos >= 0) {
                pos = 0.25;
            } else {
                pos = 0;
            }
            claw_left.setPosition(pos);
            claw_right.setPosition(pos);
            //x claw.increment(gamepad2.right_stick_y);
        }
        if (gamepad2.dpad_down) {
            pos = 0;
            elbow.setPosition(pos);
        } else if (gamepad2.dpad_up) {
            pos = max;
            elbow.setPosition(max);
        } else if (gamepad2.dpad_left) {
            pos = min + 0.10;
            elbow.setPosition(pos);
        } else {
            double i = (gamepad2.right_trigger - gamepad2.left_trigger) / 10;
            pos = elbow.getPosition();
            if (pos + i > 0 && pos + i < max) {
                pos += i;
                elbow.setPosition(pos);
            }
        }
    }

    public void report_telemetry() {
        telemetry.addData("Front Left", motor_fl.getPower());
        telemetry.addData("Front Right", motor_fr.getPower());
        telemetry.addData("Back Left", motor_bl.getPower());
        telemetry.addData("Back Right", motor_br.getPower());
        telemetry.update();
    }
}