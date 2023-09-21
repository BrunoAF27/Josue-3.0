package frc.robot.subsystems;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAlternateEncoder.Type;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class WristSubsystem extends SubsystemBase {
    //Controlling Devices:
    private CANSparkMax wristMotor;
    private RelativeEncoder wristEncoder;

    //PID values:
    private static final double kP = 0.1; 
    private static final double kI = 0.0;
    private static final double kD = 0.0;

    private final static double encoderCounts = 42.0;
    //Important values
    private static final double targetPositions[] = {0.0, 45, 90}; // Define your target positions here
    private double targetPosition = 0.0; // Store the current target position
    private static final double positionTolerance = 2.0; // Tolerance for position reaching
    
    public WristSubsystem(int motorId) {
        wristMotor = new CANSparkMax(motorId, CANSparkMax.MotorType.kBrushless); // Replace with actual device ID
        //boomstickEncoder = boomstickMotor.getAlternateEncoder(Type.kQuadrature, 4096);
        wristEncoder = wristMotor.getEncoder();
        // Configure the integrated PID controller
        wristMotor.getPIDController().setP(kP);
        wristMotor.getPIDController().setI(kI);
        wristMotor.getPIDController().setD(kD);
        
        // Set the output range of the PID controller
        wristMotor.getPIDController().setOutputRange(-0.5, 0.5);
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
        wristEncoder.setPosition(0);
    }
    public void setPIDPosition(){
        wristMotor.getPIDController().setReference(targetPosition, CANSparkMax.ControlType.kPosition); 
    }
    
    public boolean isAtTargetPosition() {
        double currentPosition = wristEncoder.getPosition();        
        return Math.abs(currentPosition - targetPosition) < positionTolerance;
    }
}