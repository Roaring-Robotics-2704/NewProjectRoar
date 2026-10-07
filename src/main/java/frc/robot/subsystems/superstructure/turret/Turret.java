package frc.robot.subsystems.superstructure.turret;

import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.littletonrobotics.junction.Logger;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

public class Turret extends SubsystemBase {
    private final TurretIO turretIO;
    private final TurretIOInputsAutoLogged inputs = new TurretIOInputsAutoLogged();

    public Turret(TurretIO turretIO) {
        this.turretIO = turretIO;
    }

    public void setFlywheelVoltage(Voltage voltage) {
        turretIO.setFlywheelVoltage(voltage);
    }

    public void stopFlywheel() {
        turretIO.stopFlywheel();
    }
    
    /**
     * Sets the hood to the desired angle IN DEGREES.
     * @param angle The angle IN DEGREES.
    */
    public void setHoodAngle(double angle) {
        turretIO.setHoodAngle(angle);
    }

    public void stopHoodMotor() {
        turretIO.stopHoodMotor();
    }

    /**
     * Sets the turret to the desired angle IN RADIANS.
     * @param angle The angle IN RADIANS.
    */
    public void setTurretAngle(double angle) {
        turretIO.setTurretAngle(angle);
    }

    public void stopTurretAngleMotor() {
        turretIO.stopTurretAngleMotor();
    }

    public void setFlywheelVelocity(double velocity) {
        turretIO.setFlywheelVelocity(velocity);
    }

    public void setPID(SparkMax motor, SparkMaxConfig motorConfig, double kP, double kI, double kD, double kS, double kV, double kA) {
        turretIO.setPID(motor, motorConfig, kP, kI, kD, kS, kV, kA);
    }

    @Override
    public void periodic() {
        turretIO.updateInputs(inputs);
        Logger.processInputs("Turret", inputs);
    }
}