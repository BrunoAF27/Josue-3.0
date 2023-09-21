package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAlternateEncoder.Type;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.*;
public class SparkSwingSubsystem extends SubsystemBase {
    //First we declare the master motors which are the front motors
    private final CANSparkMax leftFMotor;
    private final CANSparkMax rightFMotor;
    //The Back motors are the follower motors.
    private final CANSparkMax leftBMotor;
    private final CANSparkMax rightBMotor;
    private RelativeEncoder leftFEncoder;
    private RelativeEncoder leftBEncoder;
    private RelativeEncoder rightFEncoder;
    private RelativeEncoder rightBEncoder;
    //PID values:
    private static final double kP = 0.1; 
    private static final double kI = 0.0;
    private static final double kD = 0.0;

    private final static double encoderCounts = 42.0;
    
    //Define your position setpoints
    private static final double angle1 = 51;
    private static final double angle2 = 55;
    private static final double maxAngle = 190;
    private static final double[] positionSetpoints = {0.0,setAngle(angle1), setAngle(angle2)};
    //Min and Max velocity
    private static final double minOutput = -0.5;
    private static final double maxOutput = 0.5;
    //TargetAngle and target position represent the current objective of the subsytem.
    private double targetAngle = 0.0;
    private double targetPosition = 0;
    
    public SparkSwingSubsystem(int leftFId, int leftBId, int rightFId, int rightBId) {
        leftFMotor = new CANSparkMax(leftFId, CANSparkMax.MotorType.kBrushless);
        leftBMotor = new CANSparkMax(leftBId, CANSparkMax.MotorType.kBrushless);
        rightFMotor = new CANSparkMax(rightFId, CANSparkMax.MotorType.kBrushless);
        rightBMotor = new CANSparkMax(rightBId, CANSparkMax.MotorType.kBrushless);
        leftFMotor.setInverted(false);
        leftBMotor.setInverted(false);
        rightFMotor.setInverted(true);
        rightBMotor.setInverted(true);

        leftFEncoder = leftFMotor.getEncoder();
        rightFEncoder = rightFMotor.getEncoder();
        leftBEncoder = leftFMotor.getEncoder();
        rightBEncoder = rightFMotor.getEncoder();
        //Pid Settings.
        leftFMotor.getPIDController().setP(kP);
        leftFMotor.getPIDController().setI(kI);
        leftFMotor.getPIDController().setD(kD);

        leftBMotor.getPIDController().setP(kP);
        leftBMotor.getPIDController().setI(kI);
        leftBMotor.getPIDController().setD(kD);

        rightFMotor.getPIDController().setP(kP);
        rightFMotor.getPIDController().setI(kI);
        rightFMotor.getPIDController().setD(kD);

        rightBMotor.getPIDController().setP(kP);
        rightBMotor.getPIDController().setI(kI);
        rightBMotor.getPIDController().setD(kD);
        // Set the output range of the PID controller
        leftFMotor.getPIDController().setOutputRange(minOutput, maxOutput);
        leftBMotor.getPIDController().setOutputRange(minOutput, maxOutput);
        rightFMotor.getPIDController().setOutputRange(minOutput, maxOutput);
        rightBMotor.getPIDController().setOutputRange(minOutput, maxOutput);
    }

    public void setPosition(int positionIndex) {
        if (positionIndex >= 0 && positionIndex < positionSetpoints.length) {
            targetPosition = positionSetpoints[positionIndex];
            //leftFMotor.set(ControlMode.Position, targetPosition);
            //rightFMotor.set(ControlMode.Position, targetPosition);
        }
    }
    public void setNegativePosition(int positionIndex) {
        if (positionIndex >= 0 && positionIndex < positionSetpoints.length) {
            targetPosition = -positionSetpoints[positionIndex];
            //leftFMotor.set(ControlMode.Position, targetPosition);
            //rightFMotor.set(ControlMode.Position, targetPosition);
        }
    }
    public void resetEncoders(){
        leftFEncoder.setPosition(0);
        leftBEncoder.setPosition(0);
        rightFEncoder.setPosition(0);
        rightBEncoder.setPosition(0);
    }
    // Move the Swing in a straight line
    public void PIDStraightPosition() {
        leftFMotor.getPIDController().setReference(targetPosition, CANSparkMax.ControlType.kPosition); 
        leftBMotor.follow(leftFMotor);
        rightFMotor.follow(leftFMotor);
        rightBMotor.follow(rightFMotor);
    }

    public static double setAngle(double degrees){
        degrees = 180-degrees;

        if(degrees > maxAngle){   
        return (degrees*20.0*((14/48)*(18/48))*(12/65));
        }
        else if(degrees < 0){
            return 0; 
        }
        
        return degrees*20*((14/48)*(18/48))*(12/65);
    }
}