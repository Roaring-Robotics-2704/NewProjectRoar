package frc.robot.subsystems.superstructure.turret;

import org.littletonrobotics.junction.AutoLog;

public interface TurretIO {
    
    @AutoLog public static class TurretIOInputs {

    }

    public default void updateInputs(TurretIOInputs inputs) {
    }
}