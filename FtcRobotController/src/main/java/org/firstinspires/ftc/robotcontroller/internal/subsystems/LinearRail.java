package org.firstinspires.ftc.robotcontroller.internal.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

public class LinearRail {
    DcMotor linear_rail;
    int pos = 0;
    public LinearRail(DcMotor linear_rail){
        this.linear_rail = linear_rail;
    }
    public void drive(double i){
        if(pos + i >= 0) {
            pos += i;
            linear_rail.setTargetPosition(pos);
            linear_rail.setPower(i);
        }
    }
}
