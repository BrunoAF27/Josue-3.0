package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
public class RollerSubsystem extends SubsystemBase {
    //Controlling Devices:
    private TalonSRX rollerMotor;
    private double rollerSpeed; 
    public RollerSubsystem(int motorId) {
        rollerMotor = new TalonSRX(motorId); // Replace with actual device ID
        rollerMotor.configFactoryDefault();
        //boomstickEncoder = boomstickMotor.getAlternateEncoder(Type.kQuadrature, 4096);
        rollerMotor.setNeutralMode(NeutralMode.Brake);
    }
    
    public void setSpeed(double speed1, double speed2) {
        rollerSpeed = speed1 - speed2;
    }
    public void move(){
        rollerMotor.set(ControlMode.PercentOutput, rollerSpeed); 
    }
    
}