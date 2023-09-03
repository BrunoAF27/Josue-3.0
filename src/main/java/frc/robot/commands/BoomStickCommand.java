package frc.robot.commands;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.*;

public class BoomStickCommand extends CommandBase {
    private final BoomStickSubsystem boomStickSubsystem;
    private int targetPositionIndex = 0;
    public BoomStickCommand(BoomStickSubsystem boomStickSubsystem) {
        this.boomStickSubsystem = boomStickSubsystem;
        addRequirements(boomStickSubsystem);
    }
    
    public void setPosition(int targetPositionIndex){
        this.targetPositionIndex = targetPositionIndex;
    }
    
    @Override
    public void initialize() {
        boomStickSubsystem.resetEncoders();
    }
    @Override
    public void execute() {
        boomStickSubsystem.setPIDPosition();
    }    
    @Override
    public boolean isFinished() {
        return false;
    }
}
