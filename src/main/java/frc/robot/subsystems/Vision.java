package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import frc.robot.utilities.LimelightHelpers;
import frc.robot.subsystems.Drivebase;

public class Vision {
    
    private StructPublisher<Pose2d> poseMT1Publisher = NetworkTableInstance.getDefault().getStructTopic("Poses/Pose_WpiBlue" , Pose2d.struct).publish();; 
    private StructPublisher<Pose2d> poseMT2Publisher = NetworkTableInstance.getDefault().getStructTopic("Poses/Pose_MT2_WpiBlue" , Pose2d.struct).publish();; 
    
    public Vision(Drivebase drivebase) {
        super();
    }

    public void periodic() {

        var poseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue("limelight");


        poseMT1Publisher.set(poseEstimate.pose);

    }
}
