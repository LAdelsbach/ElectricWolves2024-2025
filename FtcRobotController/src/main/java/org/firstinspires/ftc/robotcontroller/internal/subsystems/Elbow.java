package org.firstinspires.ftc.robotcontroller.internal.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

public class Elbow {
    Servo elbow;
    public Elbow(Servo elbow){
        this.elbow = elbow;
    }
    public void down(){
        elbow.setPosition(0);
    }
    public void slight_up(){
        elbow.setPosition(10);
    }
    public void up(){
        elbow.setPosition(90);
    }
}
