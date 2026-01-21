package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.Drivebase;

public class AprilTagSquareUp extends Command {
    private Drivebase drivebase;
    private PIDController rotPid = new PIDController(0.3, 0, 0);

    public AprilTagSquareUp(Drivebase drivebase) {
        super();
        this.drivebase = drivebase;
        SmartDashboard.putData("AprilTagSquareUp/RotPID", rotPid);
    }

    @Override
    public void initialize() {
        rotPid.reset();
    }

    @Override
    public void execute() {
        super.execute();

        var limelightName = "limelight-right";
        if (LimelightHelpers.getFiducialID(limelightName) > -1) {
            var targetPose = LimelightHelpers.getTargetPose3d_RobotSpace("limelight-right");
            var targetRotation = targetPose.getRotation().getMeasureY().magnitude();



            var strafe = rotPid.calculate(targetRotation);
            SmartDashboard.putNumber("AprilTagSquareUp/targetRotation", targetRotation);
            SmartDashboard.putNumber("AprilTagSquareUp/strafe", strafe);

            drivebase.drive(0, -strafe, 0);
        }
    }

}
