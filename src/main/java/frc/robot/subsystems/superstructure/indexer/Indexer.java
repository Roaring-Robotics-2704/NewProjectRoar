package frc.robot.subsystems.superstructure.indexer;

import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.superstructure.indexer.IndexerIO.IndexerIOInputs;

import org.littletonrobotics.junction.Logger;

public class Indexer extends SubsystemBase {
    private final IndexerIO indexerIO;
    private final IndexerIOInputsAutoLogged inputs = new IndexerIOInputsAutoLogged();

    public Indexer(IndexerIO indexerIO) {
        this.indexerIO = indexerIO;
    }

    public void updateInputs(IndexerIOInputs inputs) {
        indexerIO.updateInputs(inputs);
    }

    public void setMotorVoltage(Voltage voltage) {
        indexerIO.setMotorVoltage(voltage);
    }

    public void setMotorVelocity(double velocity) {
        indexerIO.setMotorVelocity(velocity);
    }

    public void stopMotor() {
        indexerIO.stopMotor();
    }

    public void setPID(double kP, double kI, double kD) {
        indexerIO.setPID(kP, kI, kD);
    }

    @Override
    public void periodic() {
        indexerIO.updateInputs(inputs);
        Logger.processInputs("Indexer", inputs);
    }
}