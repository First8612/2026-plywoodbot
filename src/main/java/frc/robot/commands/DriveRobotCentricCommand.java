package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Drivebase;

public class DriveRobotCentricCommand extends Command {
  private Drivebase drivebase;
  private CommandXboxController driveController;

  public DriveRobotCentricCommand(Drivebase drivebase, CommandXboxController driveController) {
    super();
    this.drivebase = drivebase;
    this.driveController = driveController;

    addRequirements(drivebase);
  }

  @Override
  public void execute() {
    var ySpeed = MathUtil.applyDeadband(
        Math.pow(-driveController.getLeftX(), 3),
        0.1) * Drivebase.kMaxSpeed;
    var xSpeed = MathUtil.applyDeadband(
        Math.pow(-driveController.getLeftY(), 3),
        0.1) * Drivebase.kMaxSpeed;
    var rotSpeed = driveController.getRightX() * Drivebase.kMaxAngularSpeed;

    drivebase.driveRobotRelative(new ChassisSpeeds(xSpeed, ySpeed, rotSpeed));
  }
}
