package frc.robot.subsystems.superstructure.indexer;

import static edu.wpi.first.units.Units.*;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.units.measure.Voltage;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.subsystems.superstructure.indexer.IndexerConstants.*;
import frc.robot.subsystems.superstructure.turret.TurretConstants;
import frc.robot.util.SparkUtil;

public class IndexerIOReal implements IndexerIO {
    private SparkMax indexerMotor;
    private SparkMaxConfig indexerMotorConfig;

    public IndexerIOReal() {
        indexerMotor = new SparkMax(IndexerConstants.INDEXER_MOTOR_CAN_ID, MotorType.kBrushless);

        indexerMotorConfig = new SparkMaxConfig();
        indexerMotorConfig.smartCurrentLimit(TurretConstants.AIM_MOTOR_CURRENT_LIMIT);
        //intakeMotorConfig.inverted(false); change if necessary
        indexerMotorConfig.idleMode(IdleMode.kCoast);
        SparkUtil.tryUntilOk(indexerMotor, 5,
                () -> indexerMotor.configure(indexerMotorConfig, ResetMode.kResetSafeParameters,
                        PersistMode.kPersistParameters));
    }

    @Override
    public void updateInputs(IndexerIOInputs inputs) {
        inputs.currentDraw.mut_replace(indexerMotor.getOutputCurrent(), Amps);
        inputs.appliedVoltage.mut_replace(indexerMotor.getAppliedOutput(), Volts);
        inputs.motorVelocity.mut_replace(indexerMotor.getEncoder().getVelocity() / 60, RotationsPerSecond);
        inputs.motorTemp.mut_replace(
            Celsius.of(indexerMotor.getMotorTemperature()).in(Fahrenheit),
            Fahrenheit);
    }

    @Override
    public void setMotorVoltage(Voltage voltage) {
        indexerMotor.setVoltage(voltage);
    }

    @Override
    public void setMotorVelocity(double velocity) {
        indexerMotor.getClosedLoopController().setSetpoint(velocity, ControlType.kVelocity);
    }

    @Override
    public void stopMotor() {
        indexerMotor.stopMotor();
    }

    @Override
    public void setPID(double kP, double kI, double kD) {
        ClosedLoopConfig config = new ClosedLoopConfig();
        config.p(kP);
        config.i(kI);
        config.d(kD);
        indexerMotorConfig.apply(config);
        SparkUtil.tryUntilOk(indexerMotor, 5, () -> indexerMotor.configure(indexerMotorConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters));
    }
}