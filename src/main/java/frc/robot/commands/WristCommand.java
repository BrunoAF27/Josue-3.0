package frc.robot.commands;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.*;

public class WristCommand extends CommandBase {
    private final WristSubsystem wristSubsystem;
    private int targetPositionIndex = 0;
    public WristCommand(WristSubsystem wristSubsystem) {
        this.wristSubsystem = wristSubsystem;
        addRequirements(wristSubsystem);
    }
    
    public void setPosition(int targetPositionIndex){
        this.targetPositionIndex = targetPositionIndex;
    }
    
    @Override
    public void initialize() {
        wristSubsystem.resetEncoders();
    }
    @Override
    public void execute() {
        wristSubsystem.setPIDPosition();
    }    
    @Override
    public boolean isFinished() {
        return false;
    }
}
