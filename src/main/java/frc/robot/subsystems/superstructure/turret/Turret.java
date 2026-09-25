package frc.robot.subsystems.superstructure.turret;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

//import org.littletonrobotics.junction.Logger;

public class Turret extends SubsystemBase {
    private final TurretIO turretIO;

    public Turret(TurretIO turretIO) {
        this.turretIO = turretIO;
    }
}