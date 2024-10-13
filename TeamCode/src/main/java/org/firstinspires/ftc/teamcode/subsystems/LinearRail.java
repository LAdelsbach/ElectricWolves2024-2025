package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

public class LinearRail {
    DcMotor linear_rail;
    public LinearRail(DcMotor linear_rail){
        this.linear_rail = linear_rail;
    }
    public void drive(double i){

            linear_rail.setPower(i);
    }
}
