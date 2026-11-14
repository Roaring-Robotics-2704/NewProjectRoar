package frc.robot.subsystems.superstructure.intake;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.Volts;
import edu.wpi.first.units.measure.MutAngularVelocity;
import edu.wpi.first.units.measure.MutCurrent;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.util.SparkUtil;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class IntakeIOReal implements IntakeIO{

    public MutVoltage rollerAppliedVoltage = Volts.mutable(0);
    public MutCurrent rollerCurrentDraw = Amps.mutable(0);
    public MutAngularVelocity rollerVelocity = RadiansPerSecond.mutable(0);

    public IntakeIOReal() {
        intakeMotor = new SparkMax(IntakeConstants.ROLLER_MOTOR_ID, MotorType.kBrushless);

        intakeMotorConfig = new SparkMaxConfig();
        intakeMotorConfig.smartCurrentLimit(IntakeConstants.ROLLER_CURRENT_LIMIT);
        //intakeMotorConfig.inverted(false); change if necessary
        intakeMotorConfig.idleMode(IdleMode.kCoast);
        SparkUtil.tryUntilOk(intakeMotor, 5,
                () -> intakeMotor.configure(intakeMotorConfig, ResetMode.kResetSafeParameters,
                        PersistMode.kPersistParameters));
    }

    private SparkMax intakeMotor;
    private SparkMaxConfig intakeMotorConfig;

    @Override
    public void setMotorVoltage(Voltage voltage) {
        intakeMotor.setVoltage(voltage);
    }

    @Override
    public void stopMotor() {
        intakeMotor.stopMotor();
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        inputs.rollerCurrentDraw.mut_replace(intakeMotor.getOutputCurrent(), Amps);
        inputs.rollerVelocity.mut_replace(intakeMotor.getEncoder().getVelocity(), RotationsPerSecond);
    }

    @Override
    public void setPID(double kP, double kI, double kD) {
        ClosedLoopConfig config = new ClosedLoopConfig();
        config.p(kP);
        config.i(kI);
        config.d(kD);
        intakeMotorConfig.apply(config);
        SparkUtil.tryUntilOk(intakeMotor, 5, () -> intakeMotor.configure(intakeMotorConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters));
    }
}
