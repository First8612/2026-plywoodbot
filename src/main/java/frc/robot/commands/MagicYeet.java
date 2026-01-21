package frc.robot.commands;

import static edu.wpi.first.units.Units.Rotation;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.Drivebase;

public class MagicYeet extends Command {
    private Drivebase drivebase;
    private PIDController rotPidController = new PIDController(0.02, 0, 0);
    private PIDController strafePidController = new PIDController(0.3, 0, 0);
    private PIDController forwardPidController = new PIDController(0.001, 0.1, 0);
    private double desiredDistanceMeters = 1.0;

    public MagicYeet (Drivebase drivebase) {
        super();
        this.drivebase = drivebase;
        SmartDashboard.putData("AprilTagSquareUp/RotPID", rotPidController);
        SmartDashboard.putData("AimTowardAprilTag/RotPID", rotPidController);
        SmartDashboard.putData("desiredDistanceMeters", forwardPidController);
    }

    @Override
    public void initialize() {
        rotPidController.reset();
    }
 @Override
    public void execute() {
        super.execute();
        final Pose3d limes = LimelightHelpers.getBotPose3d("limelight-right");
        var limelightName = "limelight-right";

        if (LimelightHelpers.getFiducialID(limelightName) > -1) {
            var targetPose = LimelightHelpers.getTargetPose3d_RobotSpace("limelight-right");
            var tx = LimelightHelpers.getTX("limelight-right");
            var ty = LimelightHelpers.getTY("limelight-right");
            var targetRotation = targetPose.getRotation().getMeasureY().magnitude();
            var rot = rotPidController.calculate(tx);
            var strafe = strafePidController.calculate(targetRotation);
            var robotpose = LimelightHelpers.getTargetPose_RobotSpace("limelight-right");
            Double distance = limes.getY();
            var forwardLineUp = forwardPidController.calculate(distance - desiredDistanceMeters);
            SmartDashboard.putNumber("AprilTagSquareUp/targetRotation", targetRotation);
            SmartDashboard.putNumber("AprilTagSquareUp/strafe", strafe);
            SmartDashboard.putNumber("AimTowardAprilTag/rot", rot);
            drivebase.drive(forwardLineUp, -strafe, rot);
            SmartDashboard.putNumber("distance", distance);
        }

    }
    
} 

