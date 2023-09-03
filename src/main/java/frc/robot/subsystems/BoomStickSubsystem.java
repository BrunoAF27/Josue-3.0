package frc.robot.subsystems;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAlternateEncoder.Type;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BoomStickSubsystem extends SubsystemBase {
    //Controlling Devices:
    private CANSparkMax boomstickMotor;
    private RelativeEncoder boomstickEncoder;

    //PID values:
    private static final double kP = 0.1; 
    private static final double kI = 0.0;
    private static final double kD = 0.0;

    private final static double encoderCounts = 42.0;
    //Important values
    private static final double targetPositions[] = {0.0, encoderCounts, encoderCounts*2}; // Define your target positions here
    private double targetPosition = 0.0; // Store the current target position
    private static final double positionTolerance = 2.0; // Tolerance for position reaching
    
    public BoomStickSubsystem(int motorId) {
        boomstickMotor = new CANSparkMax(motorId, CANSparkMax.MotorType.kBrushless); // Replace with actual device ID
        //boomstickEncoder = boomstickMotor.getAlternateEncoder(Type.kQuadrature, 4096);
        boomstickEncoder = boomstickMotor.getEncoder();
        // Configure the integrated PID controller
        boomstickMotor.getPIDController().setP(kP);
        boomstickMotor.getPIDController().setI(kI);
        boomstickMotor.getPIDController().setD(kD);
        
        // Set the output range of the PID controller
        boomstickMotor.getPIDController().setOutputRange(-0.5, 0.5);
    }
    
    public void setTargetPosition(int positionIndex) {
        if (positionIndex >= 0 && positionIndex < targetPositions.length) {
            targetPosition = targetPositions[positionIndex];
        }
    }

    public void setNegativeTargetPosition(int positionIndex) {
        if (positionIndex >= 0 && positionIndex < targetPositions.length) {
            targetPosition = -targetPositions[positionIndex];
        }
    }

    public void resetEncoders() {
        boomstickEncoder.setPosition(0);
    }
    public void setPIDPosition(){
        boomstickMotor.getPIDController().setReference(targetPosition, CANSparkMax.ControlType.kPosition); 
    }
    
    public boolean isAtTargetPosition() {
        double currentPosition = boomstickEncoder.getPosition();        
        return Math.abs(currentPosition - targetPosition) < positionTolerance;
    }
}