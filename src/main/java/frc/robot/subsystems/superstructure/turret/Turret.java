package frc.robot.subsystems.superstructure.turret;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.superstructure.turret.TurretIO.TurretIOInputs;

import org.littletonrobotics.junction.Logger;

public class Turret extends SubsystemBase {
    private final TurretIO turretIO;
    private final TurretIOInputsAutoLogged inputs = new TurretIOInputsAutoLogged();

    public Turret(TurretIO turretIO) {
        this.turretIO = turretIO;
    }
}