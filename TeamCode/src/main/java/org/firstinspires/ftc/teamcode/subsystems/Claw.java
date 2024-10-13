package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.robotcore.hardware.Servo;
public class Claw {
    Servo cl_left;
    Servo cl_right;
    int pos;
    public Claw(Servo left, Servo right){
        cl_left = left;
        cl_right = right;
        pos = cl_close();

    }
    //TODO: GIVE MIN_POSITION A VALUE
    public int cl_open(){
        pos = 15;
        cl_left.setPosition(pos);
        cl_right.setPosition(pos);
        return pos;
    }
    //TODO: GIVE CLOSED POSITION A VALUE
    public int cl_close(){
        pos = 0;
        cl_left.setPosition(pos);
        cl_right.setPosition(pos);
        return pos;
    }
    //for increment control of the claw
    public int increment(double i){
        if(i + pos <= 180 && i + pos >=0 ){
            pos += i;
        }
        else if(pos >=0){
            pos = 180;
        }
        else{
            pos = 0;
        }
        cl_left.setPosition(pos);
        cl_right.setPosition(pos);
        return pos;
    }
    public void set_position(int i){
        cl_left.setPosition(i);
        cl_right.setPosition(i);
    }
}
