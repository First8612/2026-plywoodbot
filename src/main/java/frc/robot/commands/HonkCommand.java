package frc.robot.commands;

import com.ctre.phoenix6.controls.MusicTone;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;

public class HonkCommand extends Command{
    private final TalonFX controller1 = new TalonFX(11);
    private final TalonFX controller2 = new TalonFX(15);
    private final TalonFX controller3 = new TalonFX(13);
    private final TalonFX controller4 = new TalonFX(12);

    private final MusicTone honkTone = new MusicTone(300);
    private final MusicTone stopHonk = new MusicTone(0);

    public HonkCommand() {
        super();
    }

    @Override
    public void execute() {
        sendTone(honkTone);
    }

    @Override
    public void end(boolean interrupted) {
        sendTone(stopHonk);
        System.out.print("stopped");
    }

    private void sendTone(MusicTone tone)
    {
        controller1.setControl(tone);
        controller2.setControl(tone);
        controller3.setControl(tone);
        controller4.setControl(tone);
    }
}

