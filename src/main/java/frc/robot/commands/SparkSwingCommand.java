package frc.robot.commands;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.*;

public class SparkSwingCommand extends CommandBase {
    private final SparkSwingSubsystem swingSubsystem;
    private int targetPositionIndex = 0;
    public SparkSwingCommand(SparkSwingSubsystem swingSubsystem) {
        this.swingSubsystem = swingSubsystem;
        addRequirements(swingSubsystem);
    }
    
    public void setPosition(int targetPositionIndex){
        this.targetPositionIndex = targetPositionIndex;
    }
    
    @Override
    public void initialize() {
        swingSubsystem.resetEncoders();
    }
    @Override
    public void execute() {
        swingSubsystem.PIDStraightPosition();
    }    
    @Override
    public boolean isFinished() {
        return false;
    }
}

