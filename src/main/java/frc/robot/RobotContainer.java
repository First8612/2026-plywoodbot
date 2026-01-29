// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.DriveRobotCentricCommand;
import frc.robot.commands.DriveTankCommand;
import frc.robot.commands.AimTowardAprilTag;
import frc.robot.commands.AprilTagSquareUp;
import frc.robot.commands.DriveFieldCentricCommand;
import frc.robot.commands.HonkCommand;
import frc.robot.commands.MagicYeet;
import frc.robot.subsystems.Drivebase;
import frc.robot.subsystems.Vision;

public class RobotContainer {
  private final SendableChooser<Command> autoChooser;
  private Drivebase drivebase = new Drivebase();
  private CommandXboxController controller = new CommandXboxController(0);
  private Command[] driveCommands = new Command[] {
    new DriveFieldCentricCommand(drivebase, controller),
    new DriveRobotCentricCommand(drivebase, controller),
    new DriveTankCommand(drivebase, controller)
  };
  private int driveModeIndex = 0;
  private Vision vision = new Vision(drivebase);

  public RobotContainer() {
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);
    NamedCommands.registerCommand("Honker", new HonkCommand());
    configureBindings();

    SmartDashboard.putData(drivebase);
  }

  private void configureBindings() {
    controller.a().whileTrue(new HonkCommand());

    controller.y().whileTrue(new AimTowardAprilTag(drivebase, false));

    controller.rightBumper().whileTrue(new AprilTagSquareUp(drivebase));

    controller.leftBumper().whileTrue(new MagicYeet(drivebase));

    controller.b().onTrue(Commands.runOnce(() -> {
      driveModeIndex++;
      if (driveModeIndex > driveCommands.length - 1) {
        driveModeIndex = 0;
      }

      drivebase.setDefaultCommand(driveCommands[driveModeIndex]);
    }, drivebase));

    drivebase.setDefaultCommand(driveCommands[driveModeIndex]);
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  public void robotPeriodic() {
    vision.periodic();
  }
}
