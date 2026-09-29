package frc.robot.subsystems.superstructure.indexer;

import edu.wpi.first.math.system.plant.DCMotor;
import static edu.wpi.first.units.Units.Volts;
import edu.wpi.first.units.measure.Voltage;

public class IndexerConstants {
    public static final Voltage INDEXER_MAX_VOLTAGE = Volts.of(10); //TODO get an actual value

    public static final int INDEXER_MOTOR_CAN_ID = 1010; //TODO update to a real CAN ID

    public static final DCMotor INDEXER_MOTOR_TYPE = DCMotor.getNEO(1);
}