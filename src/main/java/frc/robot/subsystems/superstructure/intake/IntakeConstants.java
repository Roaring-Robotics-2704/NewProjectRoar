package frc.robot.subsystems.superstructure.intake;

import edu.wpi.first.math.system.plant.DCMotor;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Pounds;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Mass;

public class IntakeConstants {
    public static final int ROLLER_MOTOR_ID = 999; //not reak motor id

    public static final int ROLLER_CURRENT_LIMIT = 10; //dummy current limit

    public static final DCMotor ROLLER_MOTOR_TYPE = DCMotor.getNeo550(1);
}
