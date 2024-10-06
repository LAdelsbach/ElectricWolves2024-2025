package org.firstinspires.ftc.robotcontroller.internal.subsystems;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Drivetrain {
    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;
    public Drivetrain(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br){
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;
    }
    public void drive(double x, double y, double rx, double adj){
        //adjust for imperfect strafing
        x *= adj;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        fl.setPower(frontLeftPower);
        bl.setPower(backLeftPower);
        fr.setPower(frontRightPower);
        br.setPower(backRightPower);
    }
    private void resetMotors() {
        fl.setPower(0.0);
        fr.setPower(0.0);
        bl.setPower(0.0);
        br.setPower(0.0);
    }
}
