package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
public class Items {
	static private Servo finger0;
	static private Servo finger1;
	static DcMotorEx frontLeftMotor;
	static DcMotorEx frontRightMotor;
	static DcMotorEx backLeftMotor;
	static DcMotorEx backRightMotor;
	
	static DcMotorEx liftMotor;
	
	static final double ticksPerRotation = 537.7;
	static final double gearReduction = 1.0;
	
	static final double wheelDiameter = 3.77953;
	static final double ticksPerInch = (ticksPerRotation * gearReduction) / (wheelDiameter * Math.PI);
	
	static final double lsDiameter = 1.404;
	static final double ticksPerInchLs =  (ticksPerRotation * gearReduction) / (lsDiameter * Math.PI);

	public Items(HardwareMap hardwareMap){
		frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
		frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightDrive");
		backLeftMotor = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
		backRightMotor = hardwareMap.get(DcMotorEx.class, "backRightDrive");
		
		liftMotor = hardwareMap.get(DcMotorEx.class, "lift");
	}

	public static void liftMove(double position){
		liftMotor.setTargetPosition((int)Math.ceil(position));
		liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		liftMotor.setVelocity(800.0);
	}
	
	public static void clawOpen(){
		finger0.setPosition(0.75);
		finger1.setPosition(0.25);
	}
	 public static void moveFoward(double position){
	 	frontLeftMotor.setTargetPosition(((int)Math.ceil(position)));
		frontRightMotor.setTargetPosition((int)Math.ceil(position));
		backLeftMotor.setTargetPosition((int)Math.ceil(position));
		backRightMotor.setTargetPosition((int)Math.ceil(position));
		
		frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		
		frontLeftMotor.setVelocity(100.0);
		frontRightMotor.setVelocity(00.0);
		backLeftMotor.setVelocity(100.0);
		backRightMotor.setVelocity(100.0);
		
		frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
	}
	
	
	public static void moveRotate(String direction, double position){
		if(direction == "right"){
			frontLeftMotor.setTargetPosition((int)Math.ceil(position));
			backRightMotor.setTargetPosition((int)Math.ceil(position));
		} else {
			frontRightMotor.setTargetPosition((int)Math.ceil(position));
			backLeftMotor.setTargetPosition((int)Math.ceil(position));
		}
		
		frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		
		if(direction == "right"){
			frontLeftMotor.setPower(-0.75);
			backRightMotor.setPower(0.75);
		} else {
			frontRightMotor.setPower(-0.75);
			backLeftMotor.setPower(0.75);
		}
		
		frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
	}
	
	public static void moveStrafe(String direction, double position){
		if(direction == "right"){
			frontLeftMotor.setTargetPosition((int)Math.ceil(position));
			frontRightMotor.setTargetPosition((int)Math.ceil(position));
			backRightMotor.setTargetPosition((int)Math.ceil(position));
		} else {
			frontRightMotor.setTargetPosition((int)Math.ceil(position));
			backLeftMotor.setTargetPosition((int)Math.ceil(position));
		}
		
		frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		
		if(direction == "right"){
			frontLeftMotor.setPower(-0.75);
			backRightMotor.setPower(0.75);
		} else {
			frontRightMotor.setPower(-0.75);
			backLeftMotor.setPower(0.75);
		}
		
		frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
	}
}